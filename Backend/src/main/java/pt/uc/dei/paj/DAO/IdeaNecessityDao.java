package pt.uc.dei.paj.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import pt.uc.dei.paj.DTO.IdeaNecessityDTO;
import pt.uc.dei.paj.entity.IdeaNecessity;
import pt.uc.dei.paj.entity.Interest;
import pt.uc.dei.paj.entity.Justification;
import pt.uc.dei.paj.entity.Project;
import pt.uc.dei.paj.entity.Skill;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.general.SaveFile;

@Stateless
public class IdeaNecessityDao extends AbstractDao<IdeaNecessity> {
	private static final long serialVersionUID = 1L;

	@Inject
	JustificationDao justificationDao;

	public IdeaNecessityDao() {
		super(IdeaNecessity.class);
	}

	/**
	 * Method that converts IdeaNecessity of the type DTO to IdeaNecessity of the type Entity
	 * 
	 * @param ideaNecessityDto
	 * @param user
	 * @return IdeaNecessity
	 */
	public IdeaNecessity convertDtoToEntity(IdeaNecessityDTO ideaNecessityDto, User user) {
		IdeaNecessity ideaNecessity = new IdeaNecessity();

		if (ideaNecessityDto.getTitle() != null && !ideaNecessityDto.getTitle().isEmpty()) {
			ideaNecessity.setTitle(ideaNecessityDto.getTitle());
		}

		if (ideaNecessityDto.getDescription() != null && !ideaNecessityDto.getDescription().isEmpty()) {
			ideaNecessity.setDescription(ideaNecessityDto.getDescription());
		}
		if (ideaNecessityDto.getIdeaOrNecessity() != null && !ideaNecessityDto.getIdeaOrNecessity().equals("")) {
			ideaNecessity.setIdeaOrNecessity(ideaNecessityDto.getIdeaOrNecessity());
		}
		if (ideaNecessityDto.getImageIdeaNecessity() != null) {
			ideaNecessity.setImageIdeaNecessity(ideaNecessityDto.getImageIdeaNecessity());
		}
		if (user != null) {
			ideaNecessity.setUserJoinIdeaNecessity(user);
		}
		return ideaNecessity;
	}

	/**
	 * Method that converts IdeaNecessity of the type DTO to IdeaNecessity of the type Entity
	 * 
	 * @param ideaNecessityDto
	 * @param ideaNecessity
	 * @return IdeaNecessity
	 */
	public IdeaNecessity convertDtoToEntity(IdeaNecessityDTO ideaNecessityDto, IdeaNecessity ideaNecessity) {

		if (ideaNecessityDto.getTitle() != null && !ideaNecessityDto.getTitle().isEmpty()) {
			ideaNecessity.setTitle(ideaNecessityDto.getTitle());
		}
		if (ideaNecessityDto.getDescription() != null && !ideaNecessityDto.getDescription().isEmpty()) {
			ideaNecessity.setDescription(ideaNecessityDto.getDescription());
		}
		if (ideaNecessityDto.getIdeaOrNecessity() != null && !ideaNecessityDto.getIdeaOrNecessity().equals("")) {
			ideaNecessity.setIdeaOrNecessity(ideaNecessityDto.getIdeaOrNecessity());
		}
		if (ideaNecessityDto.getImageIdeaNecessity() != null) {
			ideaNecessity.setImageIdeaNecessity(ideaNecessityDto.getImageIdeaNecessity());
		}
		return ideaNecessity;
	}

	/**
	 * Method that converts a IdeaNecessity list of the type Entity to IdeaNecessity list of the type DTO
	 * 
	 * @param ideaNecessitylList
	 * @return IdeaNecessityDTO List
	 */
	public List<IdeaNecessityDTO> convertEntityToDto(List<IdeaNecessity> ideaNecessitylList) {
		List<IdeaNecessityDTO> ideaNecessityDtoList = new ArrayList<IdeaNecessityDTO>();
		for (IdeaNecessity ideaNecessity : ideaNecessitylList) {
			ideaNecessityDtoList.add(convertEntityToDto(ideaNecessity));
		}
		return ideaNecessityDtoList;
	}

	/**
	 * Method that converts IdeaNecessity of the type Entity to IdeaNecessity of the type DTO
	 * 
	 * @param ideaNecessity
	 * @return IdeaNecessityDTO
	 */
	public IdeaNecessityDTO convertEntityToDto(IdeaNecessity ideaNecessity) {
		IdeaNecessityDTO ideaNecessityDto = new IdeaNecessityDTO();
		ideaNecessityDto.setId(ideaNecessity.getId());
		ideaNecessityDto.setTitle(ideaNecessity.getTitle());
		ideaNecessityDto.setDescription(ideaNecessity.getDescription());
		ideaNecessityDto.setCreationTime(ideaNecessity.getCreationTime().toString());
		ideaNecessityDto.setUpdateTime(ideaNecessity.getUpdateTime().toString());
		ideaNecessityDto.setIdeaOrNecessity(ideaNecessity.getIdeaOrNecessity());
		ideaNecessityDto.setDeletedIdeaNecessity(ideaNecessity.isDeletedIdeaNecessity());
		ideaNecessityDto.setIdAuthor(ideaNecessity.getUserJoinIdeaNecessity().getIdUser());
		ideaNecessityDto.setNameAuthor(ideaNecessity.getUserJoinIdeaNecessity().getFirstName() + " "
				+ ideaNecessity.getUserJoinIdeaNecessity().getLastName());
		ideaNecessityDto.setUsernameAuthor(ideaNecessity.getUserJoinIdeaNecessity().getUsername());
		ideaNecessityDto.setVote(ideaNecessity.getUserListVote().size());
		if (ideaNecessity.getImageIdeaNecessity() != null) {
			ideaNecessityDto.setImageIdeaNecessity(SaveFile.convertFileToFrontEnd(ideaNecessity.getImageIdeaNecessity()));
		}
		
		if (ideaNecessity.getInterestAssociatedList() != null && ideaNecessity.getInterestAssociatedList().size() > 0) {
			for (Interest interest : ideaNecessity.getInterestAssociatedList()) {
				ideaNecessityDto.getInterestAssociatedList().add(interest.getIdInterest());
			}
		}

		if (ideaNecessity.getSkillAssociatedList() != null && ideaNecessity.getSkillAssociatedList().size() > 0) {
			for (Skill skill : ideaNecessity.getSkillAssociatedList()) {
				ideaNecessityDto.getSkillAssociatedList().add(skill.getIdSkill());
			}
		}

		if (ideaNecessity.getProjectAssociatedList() != null && ideaNecessity.getProjectAssociatedList().size() > 0) {
			for (Project project : ideaNecessity.getProjectAssociatedList()) {
				ideaNecessityDto.getProjectsAssociatedList().add(project.getIdProject());
			}
		}

		if (ideaNecessity.getUserFavoriteList() != null && ideaNecessity.getUserFavoriteList().size() > 0) {
			for (User user : ideaNecessity.getUserFavoriteList()) {
				ideaNecessityDto.getFavoriteList().add(user.getIdUser());
			}
		}

		if (ideaNecessity.getUserAvailabilityList() != null && ideaNecessity.getUserAvailabilityList().size() > 0) {
			for (User user : ideaNecessity.getUserAvailabilityList()) {
				ideaNecessityDto.getAvailabilityList().add(user.getIdUser());
			}
		}

		List<Justification> justificationList1 = ideaNecessity.getJustificationList1();
		if (!justificationList1.isEmpty() && justificationList1.get(0) != null) {
			ideaNecessityDto.getIdeaNecessityAssociatedList().addAll(justificationDao.convertEntityToDto(justificationList1));
		}

		List<Justification> justificationList2 = ideaNecessity.getJustificationList2();
		if (!justificationList2.isEmpty() && justificationList2.get(0) != null) {
			ideaNecessityDto.getIdeaNecessityAssociatedList().addAll(justificationDao.convertEntityToDto(justificationList2));
		}
		return ideaNecessityDto;
	}
	
	public IdeaNecessityDTO convertEntityToDtoWithListVote(IdeaNecessity ideaNecessity) {
		IdeaNecessityDTO ideaNecessityDto = new IdeaNecessityDTO();
		ideaNecessityDto.setId(ideaNecessity.getId());
		ideaNecessityDto.setTitle(ideaNecessity.getTitle());
		ideaNecessityDto.setDescription(ideaNecessity.getDescription());
		ideaNecessityDto.setCreationTime(ideaNecessity.getCreationTime().toString());
		ideaNecessityDto.setUpdateTime(ideaNecessity.getUpdateTime().toString());
		ideaNecessityDto.setIdeaOrNecessity(ideaNecessity.getIdeaOrNecessity());
		ideaNecessityDto.setDeletedIdeaNecessity(ideaNecessity.isDeletedIdeaNecessity());
		ideaNecessityDto.setIdAuthor(ideaNecessity.getUserJoinIdeaNecessity().getIdUser());
		ideaNecessityDto.setNameAuthor(ideaNecessity.getUserJoinIdeaNecessity().getFirstName() + " "+ ideaNecessity.getUserJoinIdeaNecessity().getLastName());
		ideaNecessityDto.setUsernameAuthor(ideaNecessity.getUserJoinIdeaNecessity().getUsername());
		ideaNecessityDto.setVote(ideaNecessity.getUserListVote().size());
				
		if (ideaNecessity.getImageIdeaNecessity() != null) {
			ideaNecessityDto.setImageIdeaNecessity(SaveFile.convertFileToFrontEnd(ideaNecessity.getImageIdeaNecessity()));
		}
		
		if (ideaNecessity.getUserListVote() != null && ideaNecessity.getUserListVote().size() > 0) {
			for (User user : ideaNecessity.getUserListVote()) {
				ideaNecessityDto.getVoteList().add(user.getIdUser());
			}
		}
		
		if (ideaNecessity.getInterestAssociatedList() != null && ideaNecessity.getInterestAssociatedList().size() > 0) {
			for (Interest interest : ideaNecessity.getInterestAssociatedList()) {
				ideaNecessityDto.getInterestAssociatedList().add(interest.getIdInterest());
			}
		}

		if (ideaNecessity.getSkillAssociatedList() != null && ideaNecessity.getSkillAssociatedList().size() > 0) {
			for (Skill skill : ideaNecessity.getSkillAssociatedList()) {
				ideaNecessityDto.getSkillAssociatedList().add(skill.getIdSkill());
			}
		}

		if (ideaNecessity.getProjectAssociatedList() != null && ideaNecessity.getProjectAssociatedList().size() > 0) {
			for (Project project : ideaNecessity.getProjectAssociatedList()) {
				ideaNecessityDto.getProjectsAssociatedList().add(project.getIdProject());
			}
		}

		if (ideaNecessity.getUserFavoriteList() != null && ideaNecessity.getUserFavoriteList().size() > 0) {
			for (User user : ideaNecessity.getUserFavoriteList()) {
				ideaNecessityDto.getFavoriteList().add(user.getIdUser());
			}
		}

		if (ideaNecessity.getUserAvailabilityList() != null && ideaNecessity.getUserAvailabilityList().size() > 0) {
			for (User user : ideaNecessity.getUserAvailabilityList()) {
				ideaNecessityDto.getAvailabilityList().add(user.getIdUser());
			}
		}

		List<Justification> justificationList1 = ideaNecessity.getJustificationList1();
		if (!justificationList1.isEmpty() && justificationList1.get(0) != null) {
			ideaNecessityDto.getIdeaNecessityAssociatedList().addAll(justificationDao.convertEntityToDto(justificationList1));
		}

		List<Justification> justificationList2 = ideaNecessity.getJustificationList2();
		if (!justificationList2.isEmpty() && justificationList2.get(0) != null) {
			ideaNecessityDto.getIdeaNecessityAssociatedList().addAll(justificationDao.convertEntityToDto(justificationList2));
		}

		return ideaNecessityDto;
	}

	/**
	 * Method that checks if a Skill is already associated
	 * 
	 * @param id
	 * @param idSkill
	 * @return IdeaNecessity List
	 */
	public List<IdeaNecessity> checkIfTheSkillIsAlreadyAssociated(int id, int idSkill) {
		List<IdeaNecessity> resultList = em.createNamedQuery("IdeaNecessity.checkIfTheSkillIsAlreadyAssociated")
				.setParameter("id", id).setParameter("idSkill", idSkill).getResultList();
		return resultList;
	}

	/**
	 * Method that checks if a Interest is already associated
	 * 
	 * @param id
	 * @param idInterest
	 * @return IdeaNecessity List
	 */
	public List<IdeaNecessity> checkIfTheInterestIsAlreadyAssociated(int id, int idInterest) {
		List<IdeaNecessity> resultList = em.createNamedQuery("IdeaNecessity.checkIfTheInterestIsAlreadyAssociated")
				.setParameter("id", id).setParameter("idInterest", idInterest).getResultList();
		return resultList;
	}

	/**
	 * Method that checks if the user already voted
	 * 
	 * @param id
	 * @param email
	 * @return IdeaNecessity List
	 */
	public List<IdeaNecessity> checkIfUserAlreadyVote(int id, String email) {
		List<IdeaNecessity> resultList = em.createNamedQuery("IdeaNecessity.checkIfUserAlreadyVote")
				.setParameter("id", id).setParameter("email", email).getResultList();
		return resultList;
	}

	/**
	 * Method that checks if IdeaNecessity already is favorite user
	 * 
	 * @param id
	 * @param email
	 * @return IdeaNecessity List
	 */
	public List<IdeaNecessity> checkIfAlreadyIsFavoriteUser(int id, String email) {
		List<IdeaNecessity> resultList = em.createNamedQuery("IdeaNecessity.checkIfAlreadyIsFavoriteUser")
				.setParameter("id", id).setParameter("email", email).getResultList();
		return resultList;
	}

	/**
	 * Method that checks if User already is available for IdeaNecessity
	 * 
	 * @param id
	 * @param email
	 * @return IdeaNecessity List
	 */
	public List<IdeaNecessity> checkIfUserAlreadyIsAvailability(int id, String email) {
		List<IdeaNecessity> resultList = em.createNamedQuery("IdeaNecessity.checkIfUserAlreadyIsAvailability")
				.setParameter("id", id).setParameter("email", email).getResultList();
		return resultList;
	}

	/**
	 * Method that checks if project has already been added
	 * 
	 * @param idProject
	 * @param id
	 * @return Project List
	 */
	public List<Project> checkIProjectAlreadyAdded(int idProject, int id) {
		List<Project> resultList = em.createNamedQuery("IdeaNecessity.checkIProjectAlreadyAdded").setParameter("id", id)
				.setParameter("idProject", idProject).getResultList();
		return resultList;
	}

	/**
	 * Method that finds all Skill associate a IdeaNecessity by id
	 * 
	 * @param id
	 * @return Skill List
	 */
	public List<Skill> findAllSkillAssociateIdIdeaNecessity(int id) {
		List<Skill> resultList = em.createNamedQuery("IdeaNecessity.findAllSkillAssociateIdIdeaNecessity")
				.setParameter("id", id).getResultList();
		return resultList;
	}

	/**
	 * Method that finds all Interest associate a IdeaNecessity by id
	 * 
	 * @param id
	 * @return Interest List
	 */
	public List<Interest> findAllInterestsAssociateIdIdeaNecessity(int id) {
		List<Interest> resultList = em.createNamedQuery("IdeaNecessity.findAllInterestsAssociateIdIdeaNecessity")
				.setParameter("id", id).getResultList();
		return resultList;
	}

	/**
	 * Method that finds all IdeaNecessities that the user is author
	 * 
	 * @param email
	 * @return IdeaNecessity List
	 */
	public List<IdeaNecessity> findAllIdeaNecessitysUserIsAuthor(String email) {
		List<IdeaNecessity> resultList = em.createNamedQuery("IdeaNecessity.findAllIdeaNecessitysUserIsAuthor")
				.setParameter("email", email).getResultList();
		return resultList;
	}

	/**
	 * Method that finds all IdeaNecessities no deleted that the user is author
	 * 
	 * @param email
	 * @return IdeaNecessity List
	 */
	public List<IdeaNecessity> findAllIdeaNecessitiesNoDeletedUserIsAuthor(String email) {
		List<IdeaNecessity> resultList = em
				.createNamedQuery("IdeaNecessity.findAllIdeaNecessitiesNoDeletedUserIsAuthor")
				.setParameter("email", email).getResultList();
		return resultList;
	}

	/**
	 * Method that finds all users with available to work in an IdeaNecessity by id
	 * 
	 * @param id
	 * @return User List
	 */
	public List<User> findAllUserWithAvailableToWorkIdIdeaNecessity(int id) {
		List<User> resultList = em.createNamedQuery("IdeaNecessity.findAllUserWithAvailableToWorkIdIdeaNecessity")
				.setParameter("id", id).getResultList();
		return resultList;
	}

	/**
	 * Method that finds all projects Associated an IdeaNecessity by id
	 * 
	 * @param id
	 * @return Project List
	 */
	public List<Project> findProjectsAssociateIdIdeaNecessity(int id) {
		List<Project> resultList = em.createNamedQuery("IdeaNecessity.findProjectsAssociateIdIdeaNecessity")
				.setParameter("id", id).getResultList();
		return resultList;
	}

	/**
	 * Method that finds id of last IdeaNecessity posted by user
	 * 
	 * @param email
	 * @return Integer List
	 */
	public List<Integer> findIdOfLastIdeaNecessityPostedByUser(String email) {
		List<Integer> resultList = em.createNamedQuery("IdeaNecessity.findIdOfLastIdeaNecessityPostedByUser")
				.setParameter("email", email).getResultList();
		return resultList;
	}
}
