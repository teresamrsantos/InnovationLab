package pt.uc.dei.paj.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import pt.uc.dei.paj.DTO.UserDTO;
import pt.uc.dei.paj.entity.IdeaNecessity;
import pt.uc.dei.paj.entity.Interest;
import pt.uc.dei.paj.entity.Skill;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.general.PasswordUtils;
import pt.uc.dei.paj.general.SaveFile;

@Stateless
public class UserDao extends AbstractDao<User> {
	private static final long serialVersionUID = 1L;

	@Inject
	SkillDao skillDao;
	@Inject
	InterestDao interestDao;
	@Inject
	GroupVisibilityDao groupDao;
	@Inject
	ParticipationDao participationDao;

	public UserDao() {
		super(User.class);
	}

	/**
	 * Method that creates a random token
	 * 
	 * @return random token
	 */
	public String createToken() {
		UUID id = UUID.randomUUID();
		String token = id.toString();
		return token;
	}

	/**
	 * Method that converts userDTO to user Entity
	 * 
	 * @param user_dto
	 * @return user Entity
	 */
	public User convertDtoToEntity(UserDTO userDto) {
		User userEntity = new User();

		ArrayList<String> listStringsPassword = protectUserPassword(userDto.getPassword());
		userEntity.setSalt(listStringsPassword.get(0));
		userEntity.setEncriptedPassword(listStringsPassword.get(1));

		userEntity.setAvailability(userDto.getAvailablehours());

		if (userDto.getVisibility() != null) {
			userEntity.setVisibility(userDto.getVisibility());
		}

		if (userDto.getWorkplace() != null) {
			userEntity.setWorkplace(userDto.getWorkplace());
		}

		if (userDto.getEmail() != null) {
			userEntity.setEmail(userDto.getEmail());
		}

		if (userDto.getUsername() != null) {
			userEntity.setUsername(userDto.getUsername());
		}

		if (userDto.getFirstName() != null) {
			userEntity.setFirstName(userDto.getFirstName());
		}

		if (userDto.getLastName() != null) {
			userEntity.setLastName(userDto.getLastName());
		}

		if (userDto.getPictureUrl() != null) {
			userEntity.setPictureUrl(userDto.getPictureUrl());
		}

		if (userDto.getUserType() != null) {
			userEntity.setUserType(userDto.getUserType());
		}

		if (userDto.getBiography() != null) {
			userEntity.setBiography(userDto.getBiography());
		}
		userEntity.setDeleted(userDto.isDeleted());


		return userEntity;
	}

	public List<UserDTO> convertEntityListtoDTOList(List<User> userList) {
		List<UserDTO> listToReturn = new ArrayList<UserDTO>(10);
		for (User user : userList) {
			listToReturn.add(convertEntitytoDTO(user));
		}

		return listToReturn;
	}

	/**
	 * Method that converts user Entity to userDTO
	 * 
	 * @param user_ent
	 * @return userDTO
	 */
	public UserDTO convertEntitytoDTO(User userEntity) {

		try {
			UserDTO userDTO = new UserDTO();

			if (userEntity.getUserSkills() != null && userEntity.getUserSkills().size() > 0) {
				for (Skill skill : userEntity.getUserSkills()) {
					userDTO.getSkillsList().add(skillDao.convertEntityToDto(skill));
				}
			}

			if (userEntity.getUserInterests() != null && userEntity.getUserInterests().size() > 0) {
				for (Interest interest : userEntity.getUserInterests()) {
					userDTO.getInterestsList().add(interestDao.convertEntityToDto(interest));
				}
			}
			userDTO.setId(userEntity.getIdUser());
			userDTO.setEmail(userEntity.getEmail());
			userDTO.setUsername(userEntity.getUsername());
			userDTO.setFirstName(userEntity.getFirstName());
			userDTO.setIdUser(userEntity.getIdUser());
			userDTO.setNumberOfActiveProjects(participationDao.participationsUserProjectActive(userEntity.getEmail()).size());
			if (userEntity.getLastName() != null) {
				userDTO.setLastName(userEntity.getLastName());
			}

			if (userEntity.getPictureUrl() != null) {
				userDTO.setPictureUrl(SaveFile.convertFileToFrontEnd(userEntity.getPictureUrl()));
			}

			if (userEntity.getBiography() != null) {
				userDTO.setBiography(userEntity.getBiography());
			}

			userDTO.setAvailablehours(userEntity.getAvailability());
			userDTO.setVisibility(userEntity.getVisibility());
			userDTO.setWorkplace(userEntity.getWorkplace());
			userDTO.setUserType(userEntity.getUserType());
			userDTO.setDeleted(userEntity.isDeleted());

			if (userEntity.getGroupList() != null && !userEntity.getGroupList().isEmpty()) {
						userDTO.setGroupList(groupDao.convertEntitytoDTO(userEntity.getGroupList()));
			}
			
			if(userEntity.getListOfUserwhoHavePermissionToSeeProfile()!=null &&  !userEntity.getListOfUserwhoHavePermissionToSeeProfile().isEmpty()) {
				userDTO.setListOfUsersAllowedToSeeProfile(convertEntityListtoDTOList(userEntity.getListOfUserwhoHavePermissionToSeeProfile()));
			}

			return userDTO;
		} catch (Exception e) {
			System.out.println("Error in: convertEntitytoDTO ");
			return null;
		}
	}

	/**
	 *  Method that check if token of user is valid 
	 * @param token
	 * @return Returns the user if it is not deleted and the token is valid
	 */
	public User returnUserIfTokenIsValid(String token) {
		try {
			final CriteriaQuery<User> criteriaQuery = em.getCriteriaBuilder().createQuery(User.class);
			Root<User> c = criteriaQuery.from(User.class);
			criteriaQuery.select(c)
					.where(em.getCriteriaBuilder().and(em.getCriteriaBuilder().equal(c.get("deleted"), false),
							em.getCriteriaBuilder().equal(c.get("token"), token)));

			return em.createQuery(criteriaQuery).getSingleResult();
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: returnUserIfTokenIsValid");
			return null;
		}

	}

	/**
	 * Method that check if password and email of a user is valid
	 * @param password
	 * @param username
	 * @return the user if the password and the email match a user that is not deleted
	 */
	public User returnUserIfLoginIsValid(String password, String email) {
		try {

			List<User> userToLogin = findUserByEmail(email);

			if (userToLogin != null && !userToLogin.isEmpty()) {
				List<String> salt = findSalt(email);
				List<String> encriptedPassword = findEncriptedPassword(email);

				if (encriptedPassword != null && salt != null && !salt.isEmpty() && !encriptedPassword.isEmpty()) {
					if (verifyProvidedPassword(password, encriptedPassword.get(0), salt.get(0))) {
						return userToLogin.get(0);
					}
				}
			}
			return null;
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: returnUserIfLoginIsValid");
			return null;
		}
	}

	/**
	 * Method that returns the encripted password on the DB
	 * 
	 * @param email
	 * @return encripted password
	 */
	public List<String> findEncriptedPassword(String email) {
		List<String> resultList = em.createNamedQuery("User.findEncriptedPassword").setParameter("email", email)
				.getResultList();
		return resultList;
	}

	/**
	 * Method that returns the SALT on the DB
	 * 
	 * @param email
	 * @return SALT
	 */
	public List<String> findSalt(String email) {
		List<String> resultList = em.createNamedQuery("User.findSalt").setParameter("email", email).getResultList();
		return resultList;
	}

	/**
	 * Method that returns the USER on the DB
	 * 
	 * @param email
	 * @return USER
	 */
	public List<User> findUserByEmail(String email) {
		List<User> resultList = em.createNamedQuery("User.findUserByEmail").setParameter("email", email)
				.getResultList();
		return resultList;
	}

	/**
	 * Method that returns the USER on the DB
	 * 
	 * @param email
	 * @return USER
	 */
	public List<User> findUserByUsername(String username) {
		List<User> resultList = em.createNamedQuery("User.findUserByUsername").setParameter("username", username)
				.getResultList();
		return resultList;
	}

	
	/**
	 * Method that returns the USER on the DB
	 * 
	 * @param email
	 * @return USER
	 */
	public List<User> findUserById(int id) {
		List<User> resultList = em.createNamedQuery("User.findUserByID").setParameter("idUser", id).getResultList();
		return resultList;
	}

	/**
	 * Method that returns the USER on the DB
	 * 
	 * @param email
	 * @return list Users
	 */
	public List<User> findUsersNotPaticipatingInProject(int id) {
		List<User> resultList = em.createNamedQuery("User.findUsersNotPaticipatingInProject").setParameter("idProject", id).getResultList();
		return resultList;
	}

	
	/**
	 * Method that returns the USER on the DB
	 * 
	 * @param activation
	 * @return User
	 */
	public List<User> findUserByActivation(String activation) {
		List<User> resultList = em.createNamedQuery("User.findUserByActivation").setParameter("activation", activation)
				.getResultList();
		return resultList;
	}
	
	/**
	 * Method that find all IdeaNecessity on the DB that the user is the creator
	 * 
	 * @param email of the user is the creator
	 * @return list of IdeaNecessity
	 */
	public List<IdeaNecessity> findIdeaNecessitiesUserIsAuthor(int idUser) {
		List<IdeaNecessity> resultList = em.createNamedQuery("User.findIdeaNecessitiesUserIsAuthor")
				.setParameter("idUser", idUser).getResultList();
		return resultList;
	}

	/**
	 * Method that find all IdeaNecessity no deleted on the DB that the user is the
	 * creator
	 * 
	 * @param email of the user is the creator
	 * @return list of IdeaNecessity
	 */
	public List<IdeaNecessity> findIdeaNecessitiesUserIsAuthorMember(int idUser) {
		List<IdeaNecessity> resultList = em.createNamedQuery("User.findIdeaNecessitiesUserIsAuthorMember")
				.setParameter("idUser", idUser).getResultList();
		return resultList;
	}

	/**
	 * Method that find all IdeaNecessity on the DB that are user favorites
	 * 
	 * @param email of the user
	 * @return list of IdeaNecessity
	 */
	public List<IdeaNecessity> findIdeaNecessitiesFavoriteOfTheUser(int idUser) {
		List<IdeaNecessity> resultList = em.createNamedQuery("User.findIdeaNecessitiesFavoriteOfTheUser")
				.setParameter("idUser", idUser).getResultList();
		return resultList;
	}

	/**
	 * Method that find all IdeaNecessity no deleted on the DB that are user
	 * favorites
	 * 
	 * @param email of the user
	 * @return list of IdeaNecessity
	 */
	public List<IdeaNecessity> findIdeaNecessitiesFavoriteOfTheUserMember(int idUser) {
		List<IdeaNecessity> resultList = em.createNamedQuery("User.findIdeaNecessitiesFavoriteOfTheUserMember")
				.setParameter("idUser", idUser).getResultList();
		return resultList;
	}

	/**
	 * Method that return all users that the user can talk
	 * @param email
	 * @return list users
	 */
	public List<User> findAllUserToTalk(String email) {
		List<User> resultList = em.createNamedQuery("User.findAllUserToTalk").setParameter("email", email)
				.getResultList();
		return resultList;
	}

	/**
	 * Method that find all users with except himself
	 * @param email
	 * @return
	 */
	public List<User> findAllUserExceptHimself(String email) {
		List<User> resultList = em.createNamedQuery("User.findAllUserExceptHimself").setParameter("email", email)
				.getResultList();
		return resultList;
	}

	/**
	 * Method to protect User Password
	 * @param myPassword
	 * @return
	 */
	public ArrayList<String> protectUserPassword(String myPassword) {

		// Generate Salt. The generated value can be stored in DB.
		String salt = PasswordUtils.getSalt(30);

		// Protect user's password. The generated value can be stored in DB.
		String mySecurePassword = PasswordUtils.generateSecurePassword(myPassword, salt);

		ArrayList<String> listStringToReturn = new ArrayList<String>(2);

		listStringToReturn.add(salt);
		listStringToReturn.add(mySecurePassword);
		return listStringToReturn;
	}

	public boolean verifyProvidedPassword(String providedPassword, String securePassword, String salt) {
		boolean passwordMatch = PasswordUtils.verifyUserPassword(providedPassword, securePassword, salt);
		if (passwordMatch) {
			return true;
		} else {
			return false;
		}
	}

	public List<User> usersWhoHavePermissionToSeeMyProfile(User userAux) {
		/*
		 * try { CriteriaQuery<User> criteriaQuery =
		 * em.getCriteriaBuilder().createQuery(User.class); Root<User> n =
		 * criteriaQuery.from(clazz);
		 * criteriaQuery.select(n).where(em.getCriteriaBuilder()
		 * .and(em.getCriteriaBuilder().equal(n.get(
		 * "usersWithPermissionToAccessMyProfile"), userAux))); //
		 * em.getCriteriaBuilder().equal(n.get("deleted"), false)));
		 * 
		 * return em.createQuery(criteriaQuery).getResultList();
		 */
		try {
			List<User> resultList = em.createNamedQuery("User.findUserAbleToSeeProfile")
					.setParameter("email", userAux.getEmail()).getResultList();
			return resultList;
		} catch (Exception e) {
			System.out.println("Error in: usersWhoHavePermissionToSeeMyProfile");
			// e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getIdColumn(String string) {
		switch (string) {
		case "id":
			return "email";
		case "deleted":
			return "deleted";
		default:
			throw new IllegalArgumentException("Invalid data");

		}
	}

}
