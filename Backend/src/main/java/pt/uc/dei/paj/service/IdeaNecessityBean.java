package pt.uc.dei.paj.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;

import pt.uc.dei.paj.DAO.IdeaNecessityDao;
import pt.uc.dei.paj.DAO.InterestDao;
import pt.uc.dei.paj.DAO.JustificationDao;
import pt.uc.dei.paj.DAO.ProjectDao;
import pt.uc.dei.paj.DAO.SkillDao;
import pt.uc.dei.paj.DAO.UserDao;
import pt.uc.dei.paj.DTO.IdeaNecessityDTO;
import pt.uc.dei.paj.DTO.InterestDTO;
import pt.uc.dei.paj.DTO.JustificationDTO;
import pt.uc.dei.paj.DTO.SkillDTO;
import pt.uc.dei.paj.DTO.UserDTO;
import pt.uc.dei.paj.entity.IdeaNecessity;
import pt.uc.dei.paj.entity.Interest;
import pt.uc.dei.paj.entity.Justification;
import pt.uc.dei.paj.entity.JustificationPK;
import pt.uc.dei.paj.entity.Project;
import pt.uc.dei.paj.entity.Skill;
import pt.uc.dei.paj.entity.User;

@RequestScoped
public class IdeaNecessityBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@Inject
	UserDao userDao;
	@Inject
	InterestDao interestDao;
	@Inject
	SkillDao skillDao;
	@Inject
	IdeaNecessityDao ideaNecessityDao;
	@Inject
	JustificationDao justificationDao;
	@Inject
	ProjectDao projectDao;

	/**
	 * Method that checks if interests, skills and/or projects associate are repeated
	 * 
	 * @param skills
	 * @param interests
	 * @param justification
	 * @return true if there is repetition, and false if there is no
	 */
	public boolean verificationNoRepeat(List<Integer> skills, List<Integer> interests,
			List<JustificationDTO> justification) {
		boolean verificationSkills = false;
		boolean verificationInterests = false;
		boolean verificationJustification = false;

		if (skills != null && !skills.isEmpty()) {
			verificationSkills = verificationNoInformationRepeat(skills);
		}

		if (interests != null && !interests.isEmpty()) {
			verificationInterests = verificationNoInformationRepeat(interests);
		}

		if (justification != null && !justification.isEmpty()) {
			verificationJustification = verificationNoInformationRepeatJustification(justification);
		}

		if (!verificationSkills && !verificationInterests && !verificationJustification) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Method that checks if interests and/or skills associate are repeated
	 * 
	 * @param skills
	 * @param interests
	 * @return true if there is repetition, and false if there is no
	 */
	public boolean verificationNoRepeat(List<Integer> skills, List<Integer> interests) {
		boolean verificationSkills = false;
		boolean verificationInterests = false;

		if (skills != null && !skills.isEmpty()) {
			verificationSkills = verificationNoInformationRepeat(skills);
		}

		if (interests != null && !interests.isEmpty()) {
			verificationInterests = verificationNoInformationRepeat(interests);
		}

		return (!verificationSkills && !verificationInterests) ? false : true;
	}

	/**
	 * Method that checks a list of integers if there are repeated values
	 * 
	 * @param information
	 * @return true if there is repetition, and false if there is no
	 */
	public boolean verificationNoInformationRepeat(List<Integer> information) {
		boolean verification = false;
		for (int i = 0; i < information.size(); i++) {
			for (int j = i + 1; j < information.size(); j++) {
				if (information.get(i).equals(information.get(j))) {
					verification = true;
					break;
				}
			}
		}
		return verification;
	}

	/**
	 * Method that checks a list of JustificationDTO if there are repeated ideas
	 * values
	 * 
	 * @param justificationList
	 * @return true if there is repetition, and false if there is no
	 */
	public boolean verificationNoInformationRepeatJustification(List<JustificationDTO> justificationList) {
		boolean verification = false;

		for (int i = 0; i < justificationList.size(); i++) {
			for (int j = i + 1; j < justificationList.size(); j++) {
				if (justificationList.get(i).getIdeaId2() == justificationList.get(j).getIdeaId2()) {
					verification = true;
					break;
				}
			}
		}
		return verification;
	}

	/**
	 * Method that adds a IdeaNecessity to the DB
	 * 
	 * @param user
	 * @param ideaNecessityDto
	 * @return operation success
	 * @throws Exception DB search error
	 */
	public boolean addIdeaNecessity(User user, IdeaNecessityDTO ideaNecessityDto) throws Exception {
		if (user != null) {
			IdeaNecessity ideaNecessity = ideaNecessityDao.convertDtoToEntity(ideaNecessityDto, user);
			if (ideaNecessityDto.getSkillAssociatedList() != null
					&& !ideaNecessityDto.getSkillAssociatedList().isEmpty()) {
				ideaNecessity.setSkillAssociatedList(skillList(ideaNecessityDto.getSkillAssociatedList()));
			}
			if (ideaNecessityDto.getInterestAssociatedList() != null
					&& !ideaNecessityDto.getInterestAssociatedList().isEmpty()) {
				ideaNecessity.setInterestAssociatedList(interestList(ideaNecessityDto.getInterestAssociatedList()));
			}
			ideaNecessityDao.persist(ideaNecessity);

			if (ideaNecessityDto.getIdeaNecessityAssociatedList() != null
					&& !ideaNecessityDto.getIdeaNecessityAssociatedList().isEmpty()) {
				List<Integer> result = ideaNecessityDao.findIdOfLastIdeaNecessityPostedByUser(user.getEmail());
				for (JustificationDTO justificationDto : ideaNecessityDto.getIdeaNecessityAssociatedList()) {
					associateIdeaNecessity(result.get(0), justificationDto.getIdeaId2(),
							justificationDto.getDescription(), user);
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Method that edits content a IdeaNecessity of the DB
	 * 
	 * @param user
	 * @param ideaNecessityDto
	 * @return operation success
	 * @throws Exception DB search error
	 */
	public boolean editIdeaNecessity(IdeaNecessityDTO ideaNecessityDto, IdeaNecessity ideaNecessity) {

		try {
			if (ideaNecessityDto != null && ideaNecessity != null) {
				ideaNecessity = ideaNecessityDao.convertDtoToEntity(ideaNecessityDto, ideaNecessity);
				ideaNecessityDao.merge(ideaNecessity);
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Method that find IdeaNecessity By Id
	 * @param id
	 * @return IdeaNecessity
	 */
	public IdeaNecessity findIdeaNecessityEntityById(int id) {
		try {
			IdeaNecessity ideaNecessityToFind = ideaNecessityDao.find(id);
			if (ideaNecessityToFind != null) {
				return ideaNecessityToFind;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * Method that receive a list of id of skills and searches for skills in DB by
	 * ID and returns a list of skills
	 * 
	 * @param skills list of integer contains ID skills
	 * @return list of skills
	 */
	public List<Skill> skillList(List<Integer> skills) {
		try {
			if (!skills.isEmpty()) {
				List<Skill> skillList = new ArrayList<Skill>();
				for (int i = 0; i < skills.size(); i++) {
					Skill skill = skillDao.find(skills.get(i));
					skillList.add(skill);
				}
			return skillList;
			}
			return new ArrayList<Skill>();
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that receive a list of id of interests and searches for interests in
	 * DB by ID and returns a list of interests
	 * 
	 * @param interests list of integer contains ID interests
	 * @return list of interests
	 */
	public List<Interest> interestList(List<Integer> interests) {
		try {
			if (!interests.isEmpty()) {
				List<Interest> interestList = new ArrayList<Interest>();
				for (int i = 0; i < interests.size(); i++) {
					Interest interest = interestDao.find(interests.get(i));
					interestList.add(interest);
				}
				return interestList;
			}
			return new ArrayList<Interest>();
		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to delete a IdeaNecessity or restore it
	 * 
	 * @param idIdeaNecessity
	 * @return operation sucess
	 */
	public boolean softDeleteIdeaNecessity(int idIdeaNecessity) {
		try {
			List<Project> projectList = ideaNecessityDao.findProjectsAssociateIdIdeaNecessity(idIdeaNecessity);

			if (projectList.size() == 0) {
				IdeaNecessity ideaNecessity = ideaNecessityDao.find(idIdeaNecessity);
				if (ideaNecessity.isDeletedIdeaNecessity()) {
					ideaNecessity.setDeletedIdeaNecessity(false);
				} else {
					ideaNecessity.setDeletedIdeaNecessity(true);
				}
				ideaNecessityDao.merge(ideaNecessity);
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Method to associate skills with an ideaNecessity
	 * 
	 * @param idIdeaNecessity
	 * @param idSkill
	 * @return operation success
	 * @throws Exception DB search error
	 */
	public List<SkillDTO> associateSkills(int idIdeaNecessity, int idSkill) throws Exception {
		List<IdeaNecessity> ideaNecessityList = ideaNecessityDao.checkIfTheSkillIsAlreadyAssociated(idIdeaNecessity,
				idSkill);

		if (ideaNecessityList == null || ideaNecessityList.isEmpty()) {
			IdeaNecessity ideaNecessity = ideaNecessityDao.find(idIdeaNecessity);
			List<Skill> skillList = ideaNecessity.getSkillAssociatedList();
			Skill skill = skillDao.find(idSkill);
			if (!skill.isDeletedSkill() && !ideaNecessity.isDeletedIdeaNecessity()) {
				skillList.add(skill);
				ideaNecessity.setSkillAssociatedList(skillList);
				ideaNecessityDao.merge(ideaNecessity);
				return skillDao.convertEntityToDto(skillList);
			}
		}
		return new ArrayList<SkillDTO>();
	}

	/**
	 * Method to associate interests with an ideaNecessity
	 * 
	 * @param idIdeaNecessity
	 * @param idInterest
	 * @return operation success
	 * @throws Exception DB search error
	 */
	public List<InterestDTO> associateInterests(int idIdeaNecessity, int idInterest) throws Exception {
		List<IdeaNecessity> ideaNecessityList = ideaNecessityDao.checkIfTheInterestIsAlreadyAssociated(idIdeaNecessity,
				idInterest);

		if (ideaNecessityList == null || ideaNecessityList.isEmpty()) {
			IdeaNecessity ideaNecessity = ideaNecessityDao.find(idIdeaNecessity);
			List<Interest> interestlList = ideaNecessity.getInterestAssociatedList();
			Interest interest = interestDao.find(idInterest);
			if (!interest.isDeletedInterest() && !ideaNecessity.isDeletedIdeaNecessity()) {
				interestlList.add(interest);
				ideaNecessity.setInterestAssociatedList(interestlList);
				ideaNecessityDao.merge(ideaNecessity);
				return interestDao.convertEntityToDto(interestlList);
			}
		}
		return new ArrayList<InterestDTO>();
	}

	/**
	 * Method to disassociate skills with an ideaNecessity
	 * 
	 * @param idIdeaNecessity
	 * @param idSkill
	 * @return operation success
	 * @throws Exception DB search error
	 */
	public List<SkillDTO> disassociateSkills(int idIdeaNecessity, int idSkill) throws Exception {
		IdeaNecessity ideaNecessity = ideaNecessityDao.find(idIdeaNecessity);
		List<Skill> skillList = ideaNecessity.getSkillAssociatedList();
		if (skillList != null && !skillList.isEmpty()) {
			List<Skill> skillAuxList = new ArrayList<Skill>();
			for (Skill skill : skillList) {
				if (skill.getIdSkill() != idSkill) {
					skillAuxList.add(skill);
				}
			}
			if (skillAuxList.size() < skillList.size()) {
				ideaNecessity.setSkillAssociatedList(skillAuxList);
				ideaNecessityDao.merge(ideaNecessity);
				return skillDao.convertEntityToDto(skillAuxList);
			}
		}
		return null;
	}

	/**
	 * Method to disassociate interests with an ideaNecessity
	 * 
	 * @param idIdeaNecessity
	 * @param idInterest
	 * @return operation success
	 * @throws Exception DB search error
	 */
	public List<InterestDTO> disassociateInterests(int idIdeaNecessity, int idInterest) throws Exception {
		IdeaNecessity ideaNecessity = ideaNecessityDao.find(idIdeaNecessity);
		List<Interest> interestList = ideaNecessity.getInterestAssociatedList();
		if (interestList != null && !interestList.isEmpty()) {
			List<Interest> interestAuxList = new ArrayList<Interest>();
			for (Interest interest : interestList) {
				if (interest.getIdInterest() != idInterest) {
					interestAuxList.add(interest);
				}
			}
			if (interestAuxList.size() < interestList.size()) {
				ideaNecessity.setInterestAssociatedList(interestAuxList);
				ideaNecessityDao.merge(ideaNecessity);
				return interestDao.convertEntityToDto(interestAuxList);
			}
		}
		return null;
	}

	/**
	 * Method to add a user vote to an IdeaNecessity
	 * 
	 * @param id   of the IdeaNecessity
	 * @param user
	 * @return operation success
	 * @throws Exception DB search error
	 */
	public boolean voteIdeaNecessity(int id, User user) throws Exception {
		List<IdeaNecessity> ideaNecessityList = ideaNecessityDao.checkIfUserAlreadyVote(id, user.getEmail());

		if (ideaNecessityList == null || ideaNecessityList.isEmpty()) {
			IdeaNecessity ideaNecessity = ideaNecessityDao.find(id);

			if (!ideaNecessity.isDeletedIdeaNecessity()) {
				List<User> userList = ideaNecessity.getUserListVote();
				List<IdeaNecessity> ideaNecessityAuxList = user.getIdeaNecessityListVotes();

				userList.add(user);
				ideaNecessityAuxList.add(ideaNecessity);

				ideaNecessity.setUserListVote(userList);
				user.setIdeaNecessityListVotes(ideaNecessityAuxList);

				ideaNecessityDao.merge(ideaNecessity);
				userDao.merge(user);
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to remove a user vote to an IdeaNecessity
	 * 
	 * @param id   of the IdeaNecessity
	 * @param user
	 * @return operation success
	 * @throws Exception DB search error
	 */
	public boolean removeVoteIdeaNecessity(int id, User user) throws Exception {
		List<IdeaNecessity> ideaNecessityList = ideaNecessityDao.checkIfUserAlreadyVote(id, user.getEmail());

		if (ideaNecessityList != null && !ideaNecessityList.isEmpty()) {
			// devia ser uma query que devolve todos, com exceção do que se quer remover
			List<IdeaNecessity> ideaNecessityAuxList = new ArrayList<IdeaNecessity>();
			for (IdeaNecessity ideaNecessityAux : user.getIdeaNecessityListVotes()) {
				if (ideaNecessityAux.getId() != id) {
					ideaNecessityAuxList.add(ideaNecessityAux);
				}
			}
			user.setIdeaNecessityListVotes(ideaNecessityAuxList);
			userDao.merge(user);
			return true;

		}
		return false;
	}

	/**
	 * Method to add an IdeaNecessity to the user's favorites list
	 * 
	 * @param id   of the IdeaNecessity
	 * @param user
	 * @return operation success
	 * @throws Exception DB search error
	 */
	public boolean favoriteIdeaNecessity(int id, User user) throws Exception {
		List<IdeaNecessity> ideaNecessityList = ideaNecessityDao.checkIfAlreadyIsFavoriteUser(id, user.getEmail());

		if (ideaNecessityList == null || ideaNecessityList.isEmpty()) {
			IdeaNecessity ideaNecessity = ideaNecessityDao.find(id);
			if (ideaNecessity.isDeletedIdeaNecessity() == false) {
				List<User> userList = ideaNecessity.getUserFavoriteList();
				List<IdeaNecessity> ideaNecessityAuxList = user.getIdeaNecessityListFavorites();

				userList.add(user);
				ideaNecessityAuxList.add(ideaNecessity);

				ideaNecessity.setUserFavoriteList(userList);
				user.setIdeaNecessityListFavorites(ideaNecessityAuxList);

				ideaNecessityDao.merge(ideaNecessity);
				userDao.merge(user);
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to remove an IdeaNecessity to the user's favorites list
	 * 
	 * @param idIdeaNecessity
	 * @param user
	 * @return operation success
	 * @throws Exception DB search error
	 */
	public boolean removeFavoriteIdeaNecessity(int idIdeaNecessity, User user) throws Exception {
		List<IdeaNecessity> ideaNecessityList = ideaNecessityDao.checkIfAlreadyIsFavoriteUser(idIdeaNecessity,
				user.getEmail());

		if (ideaNecessityList != null && !ideaNecessityList.isEmpty()) {
			// devia ser uma query que devolve todos, com exceção do que se quer remover
			List<IdeaNecessity> ideaNecessityAuxList = new ArrayList<IdeaNecessity>();
			for (IdeaNecessity ideaNecessityAux : user.getIdeaNecessityListFavorites()) {
				if (ideaNecessityAux.getId() != idIdeaNecessity) {
					ideaNecessityAuxList.add(ideaNecessityAux);
				}
			}
			user.setIdeaNecessityListFavorites(ideaNecessityAuxList);
			userDao.merge(user);
			return true;
		}
		return false;
	}

	/**
	 * Method to add a user as available to work on the IdeaNecessity
	 * 
	 * @param idIdeaNecessity
	 * @param user
	 * @return operation success
	 * @throws Exception DB search error
	 */
	public boolean availabilityIdeaNecessity(int idIdeaNecessity, User user) throws Exception {
		List<IdeaNecessity> ideaNecessityList = ideaNecessityDao.checkIfUserAlreadyIsAvailability(idIdeaNecessity,
				user.getEmail());

		if (ideaNecessityList == null || ideaNecessityList.isEmpty()) {
			IdeaNecessity ideaNecessity = ideaNecessityDao.find(idIdeaNecessity);
			if (!ideaNecessity.isDeletedIdeaNecessity()) {
				List<User> userList = ideaNecessity.getUserAvailabilityList();
				List<IdeaNecessity> ideaNecessityAuxList = user.getIdeaNecessityListAvailability();

				userList.add(user);
				ideaNecessityAuxList.add(ideaNecessity);

				ideaNecessity.setUserAvailabilityList(userList);
				user.setIdeaNecessityListAvailability(ideaNecessityAuxList);

				ideaNecessityDao.merge(ideaNecessity);
				userDao.merge(user);
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to remove a user as available to work on the IdeaNecessity
	 * 
	 * @param idIdeaNecessity
	 * @param user
	 * @return operation success
	 * @throws Exception DB search error
	 */
	public boolean removeAvailabilityIdeaNecessity(int idIdeaNecessity, User user) throws Exception {
		List<IdeaNecessity> ideaNecessityList = ideaNecessityDao.checkIfUserAlreadyIsAvailability(idIdeaNecessity,
				user.getEmail());

		if (ideaNecessityList != null && !ideaNecessityList.isEmpty()) {
			// devia ser uma query que devolve todos, com exceção do que se quer remover
			List<IdeaNecessity> ideaNecessityAuxList = new ArrayList<IdeaNecessity>();
			for (IdeaNecessity ideaNecessityAux : user.getIdeaNecessityListAvailability()) {
				if (ideaNecessityAux.getId() != idIdeaNecessity) {
					ideaNecessityAuxList.add(ideaNecessityAux);
				}
			}
			user.setIdeaNecessityListAvailability(ideaNecessityAuxList);
			userDao.merge(user);
			return true;
		}
		return false;
	}

	/**
	 * Method that associates a IdeaNecessity with another IdeaNecessity
	 * 
	 * @param idIdeaNecessity
	 * @param idAdd          
	 * @param description
	 * @param user
	 * @return operation success
	 * @throws Exception DB search error
	 */
	public boolean associateIdeaNecessity(int idIdeaNecessity, int idAdd, String description, User user)
			throws Exception {
		List<Justification> justificationList = justificationDao
				.checkIfIdIdeaNecessityIsAssociateOtherIdIdeaNecessity(idIdeaNecessity, idAdd, user.getEmail());

		if (justificationList == null || justificationList.isEmpty() || justificationList.size() == 0) {
			IdeaNecessity ideaNecessity = ideaNecessityDao.find(idIdeaNecessity);
			IdeaNecessity ideaNecessityAdd = ideaNecessityDao.find(idAdd);

			if (!ideaNecessity.isDeletedIdeaNecessity() && !ideaNecessityAdd.isDeletedIdeaNecessity()) {
				JustificationPK justificationPK = new JustificationPK();
				justificationPK.setIdeaId1(ideaNecessity);
				justificationPK.setIdeaId2(ideaNecessityAdd);
				justificationPK.setUserJoinJustification(user);

				Justification justification = new Justification();
				justification.setDescription(description);
				justification.setPrimaryKey(justificationPK);

				justificationDao.persist(justification);
				return true;
			}
		}
		return false;
	}

	/**
	 * Method that disassociates a IdeaNecessity with another IdeaNecessity
	 * 
	 * @param idIdeaNecessity
	 * @param idAdd
	 * @param description
	 * @param user
	 * @return operation success
	 * @throws Exception DB search error
	 */
	public boolean disassociateIdeaNecessity(int idIdeaNecessity, int idRemove, User user) throws Exception {
		List<Justification> justificationList = justificationDao
				.checkIfIdIdeaNecessityIsAssociateOtherIdIdeaNecessity(idIdeaNecessity, idRemove, user.getEmail());

		if (justificationList != null && !justificationList.isEmpty() && justificationList.size() > 0) {

			if (justificationList.get(0).getPrimaryKey().getIdeaId1().getId() == idIdeaNecessity
					&& justificationList.get(0).getPrimaryKey().getIdeaId2().getId() == idRemove) {
				Justification justification = justificationList.get(0);
				justificationDao.remove(justification);
				return true;
			} else if (justificationList.get(0).getPrimaryKey().getIdeaId1().getId() == idRemove
					&& justificationList.get(0).getPrimaryKey().getIdeaId2().getId() == idIdeaNecessity) {
				Justification justification = justificationList.get(0);
				justificationDao.remove(justification);
				return true;
			}
		}
		return false;
	}

	/**
	 * Method returns all IdeaNecessity
	 * 
	 * @return list IdeaNecessity
	 */
	public List<IdeaNecessityDTO> findAllIdeaNecessity() {
		try {
			List<IdeaNecessity> ideaNecessityList = ideaNecessityDao.findAll();
			if (!ideaNecessityList.isEmpty() && ideaNecessityList.get(0) != null) {
				return ideaNecessityDao.convertEntityToDto(ideaNecessityList);
			}
			return new ArrayList<IdeaNecessityDTO>();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method returns all IdeaNecessity non-deleted
	 * 
	 * @return list IdeaNecessity
	 */
	public List<IdeaNecessityDTO> findAllIdeaNecessityNoDelete() {
		try {
			List<IdeaNecessity> ideaNecessityList = ideaNecessityDao.findAllNotDeleted();
			if (!ideaNecessityList.isEmpty() && ideaNecessityList.get(0) != null) {
				return ideaNecessityDao.convertEntityToDto(ideaNecessityList);
			}
			return new ArrayList<IdeaNecessityDTO>();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that returns a array object of interest with all the information
	 * necessary to select box
	 * 
	 * @return JSONArray
	 */
	public JSONArray allIdeaNecessitySelect() {
		try {
			List<IdeaNecessity> ideaNecessityList = ideaNecessityDao.findAllNotDeleted();
			JSONArray jsonArray = new JSONArray();
			for (IdeaNecessity ideaNecessity : ideaNecessityList) {
				JSONObject json = new JSONObject();
				json.put("label", ideaNecessity.getTitle() + " | " + ideaNecessity.getIdeaOrNecessity());
				json.put("value", ideaNecessity.getTitle() + " | " + ideaNecessity.getIdeaOrNecessity());
				json.put("id", ideaNecessity.getId());
				jsonArray.put(json);
			}

			return jsonArray;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that return a IdeaNecessity by ID
	 * 
	 * @param idIdeaNecessity
	 * @return list IdeaNecessity
	 */
	public IdeaNecessityDTO findIdeaNecessityById(int idIdeaNecessity) {
		try {
			IdeaNecessity ideaNecessity = ideaNecessityDao.find(idIdeaNecessity);
			if (ideaNecessity != null) {
				return ideaNecessityDao.convertEntityToDtoWithListVote(ideaNecessity);
			}
			return new IdeaNecessityDTO();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that return all IdeaNecessities associate a skills by ID
	 * 
	 * @param idIdeaNecessity
	 * @return list IdeaNecessity
	 */
	public List<SkillDTO> findAllSkillsAssociateIdIdeaNecessity(int idIdeaNecessity) {
		try {
			List<Skill> skillList = ideaNecessityDao.findAllSkillAssociateIdIdeaNecessity(idIdeaNecessity);
			if (skillList != null && !skillList.isEmpty()) {
				return skillDao.convertEntityToDto(skillList);
			}
			return new ArrayList<SkillDTO>();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that return all IdeaNecessities associate a interests by ID
	 * 
	 * @param idIdeaNecessity
	 * @return list IdeaNecessity
	 */
	public List<InterestDTO> findAllInterestsAssociateIdIdeaNecessity(int idIdeaNecessity) {
		try {
			List<Interest> interestList = ideaNecessityDao.findAllInterestsAssociateIdIdeaNecessity(idIdeaNecessity);
			if (interestList != null && !interestList.isEmpty()) {
				return interestDao.convertEntityToDto(interestList);
			}
			return new ArrayList<InterestDTO>();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that return all IdeaNecessities that email is author of the a IdeaNecessity
	 * 
	 * @param email
	 * @return list IdeaNecessity
	 */
	public List<IdeaNecessityDTO> findIdeaNecessityUserIsAuthor(String email) {
		try {
			List<IdeaNecessity> ideaNecessityList = ideaNecessityDao.findAllIdeaNecessitysUserIsAuthor(email);
			if (!ideaNecessityList.isEmpty() && ideaNecessityList.get(0) != null) {
				return ideaNecessityDao.convertEntityToDto(ideaNecessityList);
			}
			return new ArrayList<IdeaNecessityDTO>();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that return all IdeaNecessities non-deleted that email is author of the a IdeaNecessity
	 * 
	 * @param email
	 * @return list IdeaNecessity
	 */
	public List<IdeaNecessityDTO> findAllIdeaNecessitiesNoDeletedUserIsAuthor(String email) {
		try {
			List<IdeaNecessity> ideaNecessityList = ideaNecessityDao.findAllIdeaNecessitiesNoDeletedUserIsAuthor(email);
			if (!ideaNecessityList.isEmpty() && ideaNecessityList.get(0) != null) {
				return ideaNecessityDao.convertEntityToDto(ideaNecessityList);
			}
			return new ArrayList<IdeaNecessityDTO>();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that return all users with Available To Work in a ideaNecessity
	 * 
	 * @param idIdeaNecessity
	 * @return list IdeaNecessity
	 */
	public List<UserDTO> findAllUserWithAvailableToWorkIdIdeaNecessity(int idIdeaNecessity) {
		try {
			List<User> userList = ideaNecessityDao.findAllUserWithAvailableToWorkIdIdeaNecessity(idIdeaNecessity);
			if (userList != null && !userList.isEmpty()) {
				List<UserDTO> userDtoList = new ArrayList<UserDTO>();
				for (User user : userList) {
					UserDTO userDto = userDao.convertEntitytoDTO(user);
					userDtoList.add(userDto);
				}
				return userDtoList;
			}
			return new ArrayList<UserDTO>();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that all ids ideaNecessities associate the a id IdeaNecessity
	 * @param id
	 * @return list of ids ideaNecessities
	 */
	public List<Integer> allIdIdeaNecessityAssociateOtherIdIdeaNecessity(int id) {
		try {
			List<Integer> list1 = justificationDao.AllIdIdeaNecessityAssociateOtherIdIdeaNecessity1(id);
			List<Integer> list2 = justificationDao.AllIdIdeaNecessityAssociateOtherIdIdeaNecessity2(id);
			list1.addAll(list2);

			if (!list1.isEmpty() && list1.get(0) != null) {
				return list1;
			}
			return new ArrayList<Integer>();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> List<T> removeDuplicates(List<T> list) {
		List<T> newList = new ArrayList<T>();
		for (T element : list) {
			if (!newList.contains(element)) {
				newList.add(element);
			}
		}
		return newList;
	}
}
