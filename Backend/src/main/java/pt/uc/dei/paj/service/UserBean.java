package pt.uc.dei.paj.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.json.JSONObject;

import pt.uc.dei.paj.DAO.GroupVisibilityDao;
import pt.uc.dei.paj.DAO.IdeaNecessityDao;
import pt.uc.dei.paj.DAO.InterestDao;
import pt.uc.dei.paj.DAO.SkillDao;
import pt.uc.dei.paj.DAO.UserDao;
import pt.uc.dei.paj.DTO.IdeaNecessityDTO;
import pt.uc.dei.paj.DTO.InterestDTO;
import pt.uc.dei.paj.DTO.SkillDTO;
import pt.uc.dei.paj.DTO.UserDTO;
import pt.uc.dei.paj.entity.GroupVisibility;
import pt.uc.dei.paj.entity.IdeaNecessity;
import pt.uc.dei.paj.entity.Interest;
import pt.uc.dei.paj.entity.Skill;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.general.SendEmail;
import pt.uc.dei.paj.general.UserType;
import pt.uc.dei.paj.general.Visibility;

@RequestScoped
public class UserBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	UserDao userDao;
	@Inject
	SkillDao skillDao;
	@Inject
	InterestDao interestDao;
	@Inject
	IdeaNecessityDao ideaNecessityDao;

	@Inject
	GroupVisibilityDao groupDao;

	/**
	 * Method that returns all users
	 * 
	 * @return list of users
	 */
	public List<User> userList() {
		return userDao.findAll();
	}


	/**
	 * Method that sends the email to the user to reset the user password
	 * 
	 * @param userToReset
	 * @return boolean (operation success)
	 */
	public boolean resetUserPasswordSendEmail(User userToReset) {
		if (userToReset != null) {
			String resetToken = createActivition(userToReset.getEmail());
			userToReset.setActivation(resetToken);

			try {
				userDao.merge(userToReset);
				SendEmail.emailResetPassword(userToReset, resetToken);
				return true;
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("Error in: resetUserPasswordSendEmail");
				return false;
			}
		}
		return false;
	}

	/**
	 * Method that resets the password from the user
	 * 
	 * @param activition
	 * @return boolean (operation success)
	 */
	public boolean resetUserPassword(String resetToken, String newPassword) {
		try {
			List<User> user = userDao.findUserByActivation(resetToken);
			if (!user.isEmpty() && user.get(0) != null && user.size() == 1) {
				ArrayList<String> listStringsPassword = userDao.protectUserPassword(newPassword);
				user.get(0).setSalt(listStringsPassword.get(0));
				user.get(0).setEncriptedPassword(listStringsPassword.get(1));

				userDao.merge(user.get(0));
				return true;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: resetUserPassword");
			return false;
		}
		return false;
	}

	/**
	 *  Method that register new user
	 *  
	 * @param newUser
	 * @return new User if is able to add it to DB else, returns null
	 */
	public UserDTO registerNewUser(UserDTO newUser) {
		if (newUser != null) {
			if (newUser.getUserType() == null) {
				newUser.setUserType(UserType.VISITOR);
			}

			if (newUser.getVisibility() == null) {
				newUser.setVisibility(Visibility.EVERYONE);
			}

			User user_dataBase = userDao.convertDtoToEntity(newUser);

			if (user_dataBase.getUsername().isEmpty() || user_dataBase.getUsername() == null) {
				user_dataBase.setUsername(user_dataBase.getFirstName() + user_dataBase.getLastName());
			}

			String activation = createActivition(newUser.getEmail());
			user_dataBase.setActivation(activation);

			try {
				userDao.persist(user_dataBase);
				SendEmail.emailRegister(user_dataBase, activation);
				return newUser;
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("Error in: registerNewUser");
				return null;
			}
		}
		return new UserDTO();
	}

	/**
	 * Method that creates the activation key
	 * 
	 * @param email
	 * @return activation key
	 */
	public String createActivition(String email) {
		Date date = new Date();
		String plainCredentials = email + ":" + date.getTime();
		String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
		return base64Credentials;
	}

	/**
	 * Method that activates the user account
	 * 
	 * @param activition
	 * @return boolean (operation success)
	 */
	public boolean activationCount(String activition) {
		try {
			List<User> user = userDao.findUserByActivation(activition);
			if (!user.isEmpty() && user.get(0) != null && user.size() == 1) {
				user.get(0).setUserType(UserType.MEMBER);
				userDao.merge(user.get(0));
				return true;
			}
			return false;
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: resetUserPassword");
			return false;
		}
	}

	/**
	 * Method that find a user to activate
	 * 
	 * @param activition
	 * @return user
	 */
	public User findUserByActivation(String activition) {
		try {
			List<User> user = userDao.findUserByActivation(activition);
			if (user != null && user.size() == 1 && user.get(0) != null) {
				return user.get(0);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that gets password and username and returns a token if the user and
	 * password are valid and the user is not deleted
	 * 
	 * @param username
	 * @param password
	 * @return token String
	 */
	public JSONObject login(String email, String password) {
		try {
			User userLoggado = verifyPasswordAndEmail(password, email);
			if (userLoggado != null && !userLoggado.isDeleted()) {

				String token = userDao.createToken();
				
				userLoggado.setToken(token);
				userDao.merge(userLoggado);

				JSONObject json = new JSONObject();
				json.put("token", userLoggado.getToken());
				json.put("username", userLoggado.getUsername());
				json.put("userType", userLoggado.getUserType());
				return json;
			} else
				return null;
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: login");
			return null;
		}
	}

	/**
	 * Method that check if email and password of a user is valid
	 * @param password
	 * @param email
	 * @return user if the email and passwod matches
	 */
	public User verifyPasswordAndEmail(String password, String email) {
		try {
			return userDao.returnUserIfLoginIsValid(password, email);
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: verifyPasswordAndEmail");

		}
		return null;
	}

	/**
	 * Method that check if token of a user is valid
	 * @param token
	 * @return user if the token is valid
	 */
	public User checkIfTokenIsValid(String token) {
		try {
			User user = userDao.returnUserIfTokenIsValid(token);
			if (user != null)
				return user;
			else
				return null;
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: checkIfTokenIsValid");
			return null;
		}

	}

	/**
	 * Method that find a user by email and is not deleted
	 * @param email
	 * @return user if the email exists and the entity it is not deleted
	 */
	public User findUserByEmail(String email) {
		try {
			List<User> user = userDao.findEntityIfNotDeleted(email);

			if (!user.isEmpty() && user.get(0) != null && user.size() == 1) {
				return user.get(0);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: findUserByEmail");
		}

		return null;
	}

	/**
	 * Method that find a user by id
	 * @param id
	 * @return user dto
	 */
	public UserDTO findUserById(int id) {
		try {
			User user = userDao.find(id);
			if (user != null) {
				return userDao.convertEntitytoDTO(user);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: findUserById");
		}
		return null;
	}

	/**
	 * Method that find user by email
	 * @param email
	 * @return user if the email exists
	 */
	public User findUserByID(int id) {
		try {
			List<User> user = userDao.findUserById(id);

			if (!user.isEmpty() && user.get(0) != null && user.size() == 1) {
				return user.get(0);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: findUserByEmail");
		}
		return null;
	}

	/**
	 * Method that logs out a user
	 * @param token
	 * @return true if it's able to set the token to null on the db else, returns false
	 */
	public boolean logout(String token) {
		try {
			User userLoggado = userDao.returnUserIfTokenIsValid(token);

			if (userLoggado != null) {
				userLoggado.setToken(null);
				userDao.merge(userLoggado);
				return true;
			}
			return false;
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: logout");
			return false;
		}
	}

	/**
	 * Method that change password of a user
	 * @param newPassword
	 * @return boolean (operation success)
	 */
	public boolean changeUserPassword(User userAux, String newPassword) {
		try {

			ArrayList<String> listStringsPassword = userDao.protectUserPassword(newPassword);
			userAux.setSalt(listStringsPassword.get(0));
			userAux.setEncriptedPassword(listStringsPassword.get(1));

			userDao.merge(userAux);
			return true;
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: changeUserPassword");

		}
		return false;

	}

	/**
	 * Method that changes user details
	 * 
	 * @param userAux
	 * @param updatedUserProfile
	 * @return true if it's able to edit the user's info on the database false if an error occurs.
	 */

	public boolean changeUserProfile(User userAux, UserDTO updatedUserProfile) {
		
		try {
			if (updatedUserProfile.getEmail().equals(userAux.getEmail())) {
				if (userAux.getUsername().equals(updatedUserProfile.getUsername())
						|| !usernameExists(updatedUserProfile.getUsername()))

				if (updatedUserProfile.getFirstName() != null) {
					userAux.setFirstName(updatedUserProfile.getFirstName());
				}
				
				if (updatedUserProfile.getLastName() != null) {
					userAux.setLastName(updatedUserProfile.getLastName());
				}
				
				if (updatedUserProfile.getUsername() != null && !usernameExists(updatedUserProfile.getUsername())) {
					userAux.setUsername(updatedUserProfile.getUsername());
				}

				if (updatedUserProfile.getPictureUrl() != null) {
					userAux.setPictureUrl(updatedUserProfile.getPictureUrl());
				}
				
				if (updatedUserProfile.getBiography() != null) {
					userAux.setBiography(updatedUserProfile.getBiography());
				}
				
				if (updatedUserProfile.getWorkplace() != null) {
					userAux.setWorkplace(updatedUserProfile.getWorkplace());
				}
				
				userAux.setAvailability((updatedUserProfile.getAvailablehours()));
				userAux.setDeleted(updatedUserProfile.isDeleted());
				userDao.merge(userAux);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * Method check if username exists
	 * @param username
	 * @return true if the email already exists, false if it does not
	 */
	public boolean usernameExists(String username) {
		List<User> allUsers = userDao.findAll();
		for (int i = 0; i < allUsers.size(); i++) {
			if (allUsers.get(i).getUsername().equalsIgnoreCase(username)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method that find users by emails
	 * @param emails
	 * @return the list of users if found on DB according to its emails
	 */
	public List<User> userListAccordingToEmail(String emails) {
		List<User> usersList = new ArrayList<User>();
		String[] usersEmails = emails.split(",");
		for (int i = 0; i < usersEmails.length; i++) {
			User userAux = findUserByEmail(usersEmails[i]);
			if (userAux != null) {
				usersList.add(userAux);
			}
		}
		return usersList;
	}

	/**
	 * Method that find users by ids
	 * @param emails
	 * @return the list of users if found on DB according to its emails
	 */
	public List<User> userListAccordingToId(String ids) {
		List<User> usersList = new ArrayList<User>();

		String[] usersIds = ids.split(",");
		for (int i = 0; i < usersIds.length; i++) {
			User userAux = findUserByID(Integer.parseInt(usersIds[i]));
			if (userAux != null) {
				usersList.add(userAux);
			}
		}
		return usersList;
	}

	/**
	 *  Method that adding Users with Specific Visibility to a user's profile
	 * @param emails
	 * @param userAux
	 * @return the list of users that can see the Users profile after adding theuser
	 */
	public UserDTO addingUsersSpecificVisibility(String ids, User userAux) {
		try {
			List<UserDTO> usersDTOList = new ArrayList<UserDTO>(10);
			List<User> usersToAdd = userListAccordingToId(ids);
			List<User> usersAlreadyOnTheListOfUsersAllowedToSeeProfile = userAux
					.getListOfUserwhoHavePermissionToSeeProfile();

			if (usersToAdd != null && usersToAdd.size() > 0) {
				for (int i = 0; i < usersAlreadyOnTheListOfUsersAllowedToSeeProfile.size(); i++) {
					for (int j = 0; j < usersToAdd.size(); j++) {
						if (usersAlreadyOnTheListOfUsersAllowedToSeeProfile.get(i).getEmail().equals(
								usersToAdd.get(j).getEmail()) && usersToAdd.get(j).getUserType() != UserType.VISITOR) {
							usersToAdd.remove(j);
						}
					}
				}
			}

			if (usersToAdd != null && usersToAdd.size() > 0) {
				for (int j = 0; j < usersToAdd.size(); j++) {
					userAux.getListOfUserwhoHavePermissionToSeeProfile().add(usersToAdd.get(j));
				}
			}

			for (User user : userAux.getListOfUserwhoHavePermissionToSeeProfile()) {
				usersDTOList.add(userDao.convertEntitytoDTO(user));
			}
			userDao.merge(userAux);

			return userDao.convertEntitytoDTO(usersToAdd.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in: addingUsersSpecificVisibility");
		}
		return null;
	}

	/**
	 * Method that removing Users with Specific Visibility to a user's profile
	 * @param emails
	 * @param userAux
	 * @return the list of users that can see the Users profile after removing the user
	 */
	public UserDTO removingUsersSpecificVisibility(String ids, User userAux) {
		try {
			List<UserDTO> usersDTOList = new ArrayList<UserDTO>(10);
			List<User> usersToRemove = userListAccordingToId(ids);
			List<User> usersAlreadyOnTheListOfUsersAllowedToSeeProfile = userAux.getListOfUserwhoHavePermissionToSeeProfile();

			if (usersToRemove != null && usersToRemove.size() > 0) {
				for (int j = 0; j < usersToRemove.size(); j++) {
					for (int i = 0; i < usersAlreadyOnTheListOfUsersAllowedToSeeProfile.size(); i++) {

						if (usersToRemove.get(j).getEmail().equals(usersAlreadyOnTheListOfUsersAllowedToSeeProfile.get(i).getEmail())) {
							usersAlreadyOnTheListOfUsersAllowedToSeeProfile.remove(i);
						}
					}
				}
			}

			for (User user : userAux.getListOfUserwhoHavePermissionToSeeProfile()) {
				usersDTOList.add(userDao.convertEntitytoDTO(user));
			}

			userDao.merge(userAux);

			return userDao.convertEntitytoDTO(usersToRemove.get(0));
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: removingUsersSpecificVisibility");
		}
		return null;
	}

	/**
	 * method that changes visibility of a user's profile
	 * @param userAux
	 * @param visibility
	 * @return true if its able to change the profile visibility false, otherwise.
	 */
	public boolean changeUserProfileVisibility(User userAux, Visibility visibility) {
		try {
			userAux.setVisibility(visibility);
			userDao.merge(userAux);
			return true;
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: changeUserProfileVisibility");
		}
		return false;
	}

	/**
	 * Finds user by username and converts it to DTO
	 * 
	 * @param username
	 * @return userDTO
	 */
	public UserDTO returnUserDTO(String email) {
		try {
			List<User> userEntity = userDao.findEntityIfNotDeleted(email);
			if (userEntity != null && userEntity.size() == 1 && userEntity.get(0) != null) {
				UserDTO userDTO = userDao.convertEntitytoDTO(userEntity.get(0));
				return userDTO;
			}

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: returnUserDTO");
			return null;
		}

		return null;
	}

	/**
	 * 
	 * @param userType
	 * @return list with the DTO Users according to Type and if deleted or not
	 */
	public List<InterestDTO> getUsersInterest(User user) {
		try {

			return interestDao.convertEntityToDto(user.getUserInterests());

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: getAllVisitors");
			return null;
		}
	}

	/**
	 * 
	 * @param userType
	 * @return list with the DTO Users according to Type and if deleted or not
	 */
	public List<SkillDTO> getUsersSkills(User user) {
		try {

			return skillDao.convertEntityToDto(user.getUserSkills());

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: getAllVisitors");
			return null;
		}
	}

	/**
	 * Receives a user and a usertype and changes the db accordingly
	 * 
	 * @param userToEdit
	 * @param userType
	 * @return true if everything goes accordingly else, returns false.
	 */
	public List<UserDTO> editUserRole(User userToEdit, UserType userType, User userAux) {
		try {
			userToEdit.setUserType(userType);
			userToEdit.setToken(null);
			userDao.merge(userToEdit);

			List<User> userList = userDao.findAllUserExceptHimself(userAux.getEmail());
			if (!userList.isEmpty() && userList.size() > 0) {
				return userDao.convertEntityListtoDTOList(userList);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: editUserRole");
			return null;
		}
		return null;
	}

	/**
	 * Method that associates skill to a user
	 * 
	 * @param idSkill
	 * @param userAux
	 * @return
	 */
	public List<SkillDTO> associateSkillToUser(int idSkill, User userAux) {
		try {
			Skill skillToAdd = skillDao.find(idSkill);
			if (skillToAdd != null) {
				userAux.getUserSkills().add(skillToAdd);
				userDao.merge(userAux);

			}
			return skillDao.convertEntityToDto(userAux.getUserSkills());

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: getAllVisitors");
			return null;
		}
	}

	/**
	 * Method that disassociates Skill to a user
	 * 
	 * @param idSkill
	 * @param userAux
	 * @return
	 */
	public List<SkillDTO> disassociateSkillToUser(int idSkill, User userAux) {
		try {
			Skill skillToAdd = skillDao.find(idSkill);
			if (skillToAdd != null) {
				for (int i = 0; i < userAux.getUserSkills().size(); i++) {
					if (userAux.getUserSkills().get(i).getIdSkill() == skillToAdd.getIdSkill()) {
						userAux.getUserSkills().remove(i);
					}
				}
				userDao.merge(userAux);

			}
			return skillDao.convertEntityToDto(userAux.getUserSkills());

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: getAllVisitors");
			return null;
		}
	}

	/**
	 * Method that associate interests to a User
	 * 
	 * @param idInterest
	 * @param userAux
	 * @return
	 */
	public List<InterestDTO> associateInterestToUser(int idInterest, User userAux) {
		try {
			Interest interestToAdd = interestDao.find(idInterest);
			if (interestToAdd != null) {
				userAux.getUserInterests().add(interestToAdd);
				userDao.merge(userAux);
			}
			return interestDao.convertEntityToDto(userAux.getUserInterests());

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: getAllVisitors");
			return null;
		}
	}

	/**
	 * Method that disassociate interest from user
	 * 
	 * @param idInterest
	 * @param userAux
	 * @return
	 */
	public List<InterestDTO> disassociateInterestToUser(int idInterest, User userAux) {
		try {
			Interest interestToAdd = interestDao.find(idInterest);
			if (interestToAdd != null) {
				for (int i = 0; i < userAux.getUserInterests().size(); i++) {
					if (userAux.getUserInterests().get(i).getIdInterest() == interestToAdd.getIdInterest()) {
						userAux.getUserInterests().remove(i);
					}
				}
				userDao.merge(userAux);
			}
			return interestDao.convertEntityToDto(userAux.getUserInterests());

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: getAllVisitors");
			return null;
		}
	}

	/**
	 * Method that returns true or false if the user can or not see profile
	 * 
	 * @param userThatWantsToSeeProfile
	 * @param userOwnerProfile
	 * @return
	 */
	public boolean isUserAllowedToSeeProfile(User userThatWantsToSeeProfile, User userOwnerProfile) {

		if (userOwnerProfile != null) {
			if (userOwnerProfile.getVisibility() == Visibility.PRIVATE) {
				return false;
			} else if (userOwnerProfile.getVisibility() == Visibility.EVERYONE) {
				return true;
			} else if (isUserIncludedOnAGroupAllowedToSeeProfile(userThatWantsToSeeProfile, userOwnerProfile)) {
				return true;
			} else if (userOwnerProfile.getVisibility() == Visibility.SPECIFIC
					&& userOwnerProfile.getListOfUserwhoHavePermissionToSeeProfile() != null
					&& userOwnerProfile.getListOfUserwhoHavePermissionToSeeProfile().size() > 0) {
				for (User userElement : userOwnerProfile.getListOfUserwhoHavePermissionToSeeProfile()) {
					if (userElement.getEmail().equals(userThatWantsToSeeProfile.getEmail())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Method that check if User is Included On A Group Allowed To See Profile of a user 
	 * @param userThatWantsToSeeProfile
	 * @param userOwnerProfile
	 * @return true if have permission, false if haven't permission
	 */
	public boolean isUserIncludedOnAGroupAllowedToSeeProfile(User userThatWantsToSeeProfile, User userOwnerProfile) {
		List<GroupVisibility> groupList = userOwnerProfile.getGroupList();
		List<User> usersListAll = new ArrayList<User>();
		for (GroupVisibility groupVisibility : groupList) {
			if (!groupVisibility.isDeleted()) {
				List<User> usersList = listUsersAllowedToSeeProfile(groupVisibility);
				if (usersList != null && !usersList.isEmpty()) {
					usersListAll.addAll(usersList);
				}
			}
		}
		
		Set<User> set = new HashSet<>(usersListAll);
		usersListAll.clear();
		usersListAll.addAll(set);
		
		for (User user : usersListAll) {
			if (user.getIdUser() == userThatWantsToSeeProfile.getIdUser()) {
				return true;
			}
		}
	return false;
	}

	/**
	 * Method that find users allowed to see profile
	 * @param groupVisibility
	 * @return users list
	 */
	public List<User> listUsersAllowedToSeeProfile(GroupVisibility groupVisibility) {
		
		if (groupVisibility.getWorkplace() != null && groupVisibility.getInterestList().isEmpty()
				&& groupVisibility.getSkillList().isEmpty()) {
			return groupDao.findUsersgroupVisibilityWorkPlace(groupVisibility.getWorkplace());

		} else if (groupVisibility.getWorkplace() == null && !groupVisibility.getInterestList().isEmpty()
				&& groupVisibility.getSkillList().isEmpty()) {
			return groupDao.findUsersgroupVisibilityInterest(getIdsInterest(groupVisibility.getInterestList()));

		} else if (groupVisibility.getWorkplace() == null && groupVisibility.getInterestList().isEmpty()
				&& !groupVisibility.getSkillList().isEmpty()) {
			return groupDao.findUsersgroupVisibilitySkill(getIdsSkill(groupVisibility.getSkillList()));

		} else if (groupVisibility.getWorkplace() != null && groupVisibility.getInterestList().isEmpty()
				&& !groupVisibility.getSkillList().isEmpty()) {
			return groupDao.findUsersgroupVisibilitySkillWorkPlace(getIdsSkill(groupVisibility.getSkillList()),
					groupVisibility.getWorkplace());

		} else if (groupVisibility.getWorkplace() != null && !groupVisibility.getInterestList().isEmpty()
				&& groupVisibility.getSkillList().isEmpty()) {
			return groupDao.findUsersgroupVisibilityInterestWorkPlace(getIdsInterest(groupVisibility.getInterestList()),
					groupVisibility.getWorkplace());

		} else if (groupVisibility.getWorkplace() == null && groupVisibility.getInterestList().isEmpty()
				&& groupVisibility.getSkillList().isEmpty()) {
			return groupDao.findUsersgroupVisibilityInterestSkill(getIdsSkill(groupVisibility.getSkillList()),
					getIdsInterest(groupVisibility.getInterestList()));

		} else if (groupVisibility.getWorkplace() != null && !groupVisibility.getInterestList().isEmpty()
				&& !groupVisibility.getSkillList().isEmpty()) {
			return groupDao.findUsersgroupVisibilityInterestSkillWorkPlace((getIdsSkill(groupVisibility.getSkillList())),
					getIdsInterest(groupVisibility.getInterestList()), groupVisibility.getWorkplace());
		}
		return null;
	}

	/**
	 * Method that get list of interest ids
	 * @param list
	 * @return list of ids
	 */
	public List<Integer> getIdsInterest(List<Interest> list) {
		List<Integer> listToReturn = new ArrayList<Integer>();
		for (Interest interest : list) {
			listToReturn.add(interest.getIdInterest());
		}
		return listToReturn;
	}

	/**
	 * Method that get list of skills ids
	 * @param list
	 * @return list of ids
	 */
	public List<Integer> getIdsSkill(List<Skill> list) {
		List<Integer> listToReturn = new ArrayList<Integer>();
		for (Skill skill : list) {
			listToReturn.add(skill.getIdSkill());
		}
		return listToReturn;
	}

	/**
	 * Method that return list of all IdeaNecessities that User is author
	 * 
	 * @param email user author
	 * @return list IdeaNecessities
	 */
	public List<IdeaNecessityDTO> findIdeaNecessitiesUserIsAuthor(int idUser) {
		try {
			List<IdeaNecessity> ideaNecessityList = userDao.findIdeaNecessitiesUserIsAuthor(idUser);
			if (!ideaNecessityList.isEmpty() && ideaNecessityList.get(0) != null) {
				return ideaNecessityDao.convertEntityToDto(ideaNecessityList);
			}
			return new ArrayList<IdeaNecessityDTO>();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that return list of all IdeaNecessities no deleted that User is author
	 * 
	 * @param email user author
	 * @return list IdeaNecessities
	 */
	public List<IdeaNecessityDTO> findIdeaNecessitiesUserIsAuthorMember(int idUser) {
		try {
			List<IdeaNecessity> ideaNecessityList = userDao.findIdeaNecessitiesUserIsAuthorMember(idUser);
			if (!ideaNecessityList.isEmpty() && ideaNecessityList.get(0) != null) {
				return ideaNecessityDao.convertEntityToDto(ideaNecessityList);
			}
			return new ArrayList<IdeaNecessityDTO>();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that return list of all IdeaNecessities that are favorite of the user
	 * 
	 * @param email user author
	 * @return list IdeaNecessities
	 */
	public List<IdeaNecessityDTO> findIdeaNecessitiesFavoriteOfTheUser(int idUser) {
		try {
			List<IdeaNecessity> ideaNecessityList = userDao.findIdeaNecessitiesFavoriteOfTheUser(idUser);
			if (!ideaNecessityList.isEmpty() && ideaNecessityList.get(0) != null) {
				return ideaNecessityDao.convertEntityToDto(ideaNecessityList);
			}
			return new ArrayList<IdeaNecessityDTO>();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that return list of all IdeaNecessities no deleted that are favorite of the user
	 * 
	 * @param email user author
	 * @return list IdeaNecessities
	 */
	public List<IdeaNecessityDTO> findIdeaNecessitiesFavoriteOfTheUserMember(int idUser) {
		try {
			List<IdeaNecessity> ideaNecessityList = userDao.findIdeaNecessitiesFavoriteOfTheUserMember(idUser);
			if (!ideaNecessityList.isEmpty() && ideaNecessityList.get(0) != null) {
				return ideaNecessityDao.convertEntityToDto(ideaNecessityList);
			}
			return new ArrayList<IdeaNecessityDTO>();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that finds users without project participation
	 * @param idProject
	 * @return
	 */
	public List<UserDTO> findUsersNotPaticipatingInProject(String idProject) {
		try {
			List<UserDTO> listToReturn = new ArrayList<UserDTO>();
			List<User> userList = userDao.findUsersNotPaticipatingInProject(Integer.parseInt(idProject));
			listToReturn = (userDao.convertEntityListtoDTOList(userList));
			return listToReturn;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Method that find all users with except himself
	 * @param email
	 * @return
	 */
	public List<UserDTO> findAllUserExceptHimself(String email){
		try {
			List<User> userList = userDao.findAllUserExceptHimself(email);
			if(!userList.isEmpty()&&userList.size()>0) {
				return userDao.convertEntityListtoDTOList(userList);
			}
			return new ArrayList<UserDTO>(); 
		}catch (Exception e) {
			return null;
		}
	}
}
