package pt.uc.dei.paj.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import pt.uc.dei.paj.DTO.ParticipationDTO;
import pt.uc.dei.paj.DTO.ProjectDTO;
import pt.uc.dei.paj.entity.IdeaNecessity;
import pt.uc.dei.paj.entity.Interest;
import pt.uc.dei.paj.entity.Participation;
import pt.uc.dei.paj.entity.Project;
import pt.uc.dei.paj.entity.Skill;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.general.ProjectStatus;
import pt.uc.dei.paj.general.Role;
import pt.uc.dei.paj.general.SaveFile;

@Stateless
public class ProjectDao extends AbstractDao<Project> {
	private static final long serialVersionUID = 1L;
	@Inject
	UserDao userDao;
	@Inject
	IdeaNecessityDao ideaNecessitytDao;
	@Inject
	SkillDao skillDao;
	@Inject
	InterestDao interestDao;
	@Inject
	ParticipationDao participationDao;

	public ProjectDao() {
		super(Project.class);
	}

	public Role switchRole(String role) {
		switch (role) {
		case "admin":
			return Role.ADMIN;
		case "member":
			return Role.MEMBER;
		default:
			return null;

		}
	}

	public Project convertDtoToEntity(ProjectDTO projectDto) {
		Project projectEntity = new Project();

		projectEntity.setNumberMaxMembers(projectDto.getNumberMaxMembers());

		if (projectDto.getTitle() != null) {
			projectEntity.setTitle(projectDto.getTitle());
		}

		if (projectDto.getProjectContent() != null) {
			projectEntity.setProjectContent(projectDto.getProjectContent());
		}

		if (projectDto.getProjectResources() != null) {
			projectEntity.setProjectResources(projectDto.getProjectResources());
		}

		if (projectDto.getProjectPlan() != null) {
			projectEntity.setProjectPlan(projectDto.getProjectPlan());
		}
		if (projectDto.getImageProject() != null) {
			projectEntity.setImageProject(projectDto.getImageProject());
		}

	
		if (projectDto.getSkillAssociatedList() != null && projectDto.getSkillAssociatedList().size() > 0) {
			for (int skillId : projectDto.getSkillAssociatedList()) {
				if (skillDao.find(skillId) != null) {
					projectEntity.getSkillAssociatedList().add(skillDao.find(skillId));
				}
			}
		}

		
		if (projectDto.getInterestAssociatedList() != null && projectDto.getInterestAssociatedList().size() > 0) {
			for (int interestId : projectDto.getInterestAssociatedList()) {
				if (interestDao.find(interestId) != null) {
					projectEntity.getInterestAssociatedList().add(interestDao.find(interestId));
				}
			}
		}

		projectEntity.setDeletedProject(projectDto.isDeletedProject());
		return projectEntity;
	}

	public Project editProjectDTOtoEntity(ProjectDTO projectDto, Project projectEntity) {

		projectEntity.setNumberMaxMembers(projectDto.getNumberMaxMembers());

		if (projectDto.getTitle() != null) {
			projectEntity.setTitle(projectDto.getTitle());
		}

		if (projectDto.getProjectContent() != null) {
			projectEntity.setProjectContent(projectDto.getProjectContent());
		}

		if (projectDto.getProjectResources() != null) {
			projectEntity.setProjectResources(projectDto.getProjectResources());
		}

		if (projectDto.getProjectPlan() != null) {
			projectEntity.setProjectPlan(projectDto.getProjectPlan());
		}
		if (projectDto.getProjectStatus() != null) {
			if (projectDto.getProjectStatus().equalsIgnoreCase("concluded")) {
				projectEntity.setProjectStatus(ProjectStatus.CONCLUDED);
			} else {
				projectEntity.setProjectStatus(ProjectStatus.INPROGRESS);
			}
		}

		projectEntity.setDeletedProject(projectDto.isDeletedProject());
		return projectEntity;

	}

	public Participation createParticipation(ParticipationDTO participationDTO, User userAux, int idProject) {
		Participation participation = new Participation();
		participation.setUserParticipation(userAux);
		participation.setProjectParticipation(find(idProject));
		participation.setRole(switchRole(participationDTO.getRole()));

		try {
			participationDao.persist(participation);
			return participation;
		} catch (Exception e) {
			System.out.println("errorCreatePublication");
			return null;
		}
	}

	public List<ProjectDTO> convertListEntityToDTO(List<Project> projectEntityList) {
		List<ProjectDTO> listToReturn = new ArrayList<ProjectDTO>();
		for (Project project : projectEntityList) {
			listToReturn.add(convertEntityToDTO(project));
		}
		return listToReturn;
	}

	public ProjectDTO convertEntityToDTO(Project projectEntity) {
		ProjectDTO projectDTO = new ProjectDTO();

		projectDTO.setTitle(projectEntity.getTitle());

		projectDTO.setUserDTOJoinProject(userDao.convertEntitytoDTO(projectEntity.getUserJoinProject()));

		projectDTO.setCreationTime(projectEntity.getCreationTime());

		projectDTO.setDeletedProject(projectEntity.isDeletedProject());

		projectDTO.setIdProject(projectEntity.getIdProject());

		projectDTO.setNumberMaxMembers(projectEntity.getNumberMaxMembers());

		projectDTO.setProjectStatus(projectEntity.getProjectStatus().toString());

		if (projectEntity.getImageProject() != null) {
				projectDTO.setImageProject(SaveFile.convertFileToFrontEnd(projectEntity.getImageProject()));
			}

		
		Long numberOfMembers = participationDao.countParticipationsFromProject(projectEntity.getIdProject());
		projectDTO.setNumberOfMembers( (numberOfMembers).intValue() );
		projectDTO.setNumberVacancies( (projectEntity.getNumberMaxMembers()-numberOfMembers.intValue()));
		
		if (projectEntity.getProjectContent() != null) {
			projectDTO.setProjectContent(projectEntity.getProjectContent());
		}

		
		if (projectEntity.getUserListFavorites() != null && projectEntity.getUserListFavorites().size() > 0) {
			for (User user : projectEntity.getUserListFavorites()) {
				projectDTO.getUserListFavorites().add(user.getIdUser());
			}
		} 

		
		if (projectEntity.getProjectResources() != null) {
			projectDTO.setProjectResources(projectEntity.getProjectResources());
		}

		if (projectEntity.getProjectPlan() != null) {
			projectDTO.setProjectPlan(projectEntity.getProjectPlan());
		}

		if (projectEntity.getSkillAssociatedList() != null && projectEntity.getSkillAssociatedList().size() > 0) {
			for (Skill skill : projectEntity.getSkillAssociatedList()) {
				projectDTO.getSkillAssociatedList().add(skill.getIdSkill());
			}
		}

		if (projectEntity.getInterestAssociatedList() != null && projectEntity.getInterestAssociatedList().size() > 0) {
			for (Interest interest : projectEntity.getInterestAssociatedList()) {
				projectDTO.getInterestAssociatedList().add(interest.getIdInterest());
			}
		}

		if (projectEntity.getMembersList() != null && projectEntity.getMembersList().size() > 0) {
			for (Participation participation : projectEntity.getMembersList()) {
				projectDTO.getMembersList().add(participationDao.convertEntityToDTO(participation));
			}
		}
		if (projectEntity.getIdeaNecessityAssociatedList() != null
				&& projectEntity.getIdeaNecessityAssociatedList().size() > 0) {
			for (IdeaNecessity ideaNecessity : projectEntity.getIdeaNecessityAssociatedList()) {
				projectDTO.getIdeaNecessityAssociatedList().add(ideaNecessity.getId());
			}
		}

		return projectDTO;

	}
	
	public List<Project> findAllProjectsOrdered() {
		List<Project> resultList = em.createNamedQuery("Project.findAllProjects")
				.getResultList();
		return resultList;
	}
	
	
	public List<Project> checkIfProjectisAlreadyFavoritebyUser(int idProject, String email) {
		List<Project> resultList = em.createNamedQuery("Project.verifyIfUserAlreadyMarkedAsFavorite")
				.setParameter("idProject", idProject).setParameter("email", email).getResultList();
		return resultList;
	}
	
	
	public List<Project> findAllProjectsConcluded() {
		List<Project> resultList = em.createNamedQuery("Project.findAllProjectsConcluded")
			.getResultList();
		return resultList;
	}
	public List<Project> findAllProjectsInProgress() {
		List<Project> resultList = em.createNamedQuery("Project.findAllProjectsInProgress")
			.getResultList();
		return resultList;
	}


	public List<Project> findAllProjectsUserIsAuthor(String email) {
		List<Project> resultList = em.createNamedQuery("Project.findAllProjectsUserIsAuthor")
				.setParameter("email", email).getResultList();
		return resultList;
	}

	public List<Participation> findAllMembersOfProject(int idProject) {
		List<Participation> resultList = em.createNamedQuery("Project.findAllMembersOfProject")
				.setParameter("idProject", idProject).getResultList();
		return resultList;
	}

	public List<Project> findAllProjectsUserMarkedAsFavorite(String email) {
		List<Project> resultList = em.createNamedQuery("Project.findAllIProjectsUserMarkedAsFavorite")
				.setParameter("email", email).getResultList();
		return resultList;
	}

	public List<Participation> findAllProjectsWhereUserIsMember(String email) {
		List<Participation> resultList = em.createNamedQuery("Project.findAllProjectsWhereUserIsMember")
				.setParameter("email", email).getResultList();
		return resultList;
	}

	public List<Project> verifyIfTheSkillIsAlreadyAssociated(int idProject, int idSkill) {
		List<Project> resultList = em.createNamedQuery("Project.verifyIfTheSkillIsAlreadyAssociated")
				.setParameter("idProject", idProject).setParameter("idSkill", idSkill).getResultList();
		return resultList;
	}

	public List<Project> verifyIfIdeaNecessityAlreadyAssociated(int idProject, int idIdeaNecessity) {
		List<Project> resultList = em.createNamedQuery("Project.verifyIfIdeaNecessityAlreadyAssociated")
				.setParameter("idProject", idProject).setParameter("id", idIdeaNecessity).getResultList();
		return resultList;
	}

	public List<Project> verifyIfTheInterestIsAlreadyAssociated(int idProject, int idInterest) {
		List<Project> resultList = em.createNamedQuery("Project.verifyIfTheInterestIsAlreadyAssociated")
				.setParameter("idProject", idProject).setParameter("idInterest", idInterest).getResultList();
		return resultList;
	}

	public List<Skill> findAllSkillAssociatedProject(int idProject) {
		List<Skill> resultList = em.createNamedQuery("Project.findAllSkillAssociatedProject")
				.setParameter("idProject", idProject).getResultList();
		return resultList;
	}

	public List<Interest> findAllInterestsAssociatedProject(int idProject) {
		List<Interest> resultList = em.createNamedQuery("Project.findAllInterestsAssociatedProject")
				.setParameter("idProject", idProject).getResultList();
		return resultList;
	}

	public List<IdeaNecessity> findAllIdeasNecessityAssociatedProject(int idProject) {
		List<IdeaNecessity> resultList = em.createNamedQuery("Project.findAllIdeasNecessityAssociatedProject")
				.setParameter("idProject", idProject).getResultList();
		return resultList;
	}

	public List<Project> findProjectsbyWordSearch(String title) {
		List<Project> resultList = em.createNamedQuery("Project.findProjectsbyWordSearch").setParameter("title", title)
				.getResultList();
		return resultList;
	}

	@Override
	public String getIdColumn(String string) {
		switch (string) {
		case "id":
			return "idProject";
		case "deleted":
			return "deletedProject";
		default:
			throw new IllegalArgumentException("Invalid data");

		}
	}

}
