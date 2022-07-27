package pt.uc.dei.paj.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import pt.uc.dei.paj.DAO.IdeaNecessityDao;
import pt.uc.dei.paj.DAO.InterestDao;
import pt.uc.dei.paj.DAO.ParticipationDao;
import pt.uc.dei.paj.DAO.ProjectDao;
import pt.uc.dei.paj.DAO.SkillDao;
import pt.uc.dei.paj.DAO.UserDao;
import pt.uc.dei.paj.DTO.IdeaNecessityDTO;
import pt.uc.dei.paj.DTO.InterestDTO;
import pt.uc.dei.paj.DTO.ParticipationDTO;
import pt.uc.dei.paj.DTO.ProjectDTO;
import pt.uc.dei.paj.DTO.SkillDTO;
import pt.uc.dei.paj.DTO.UserDTO;
import pt.uc.dei.paj.entity.IdeaNecessity;
import pt.uc.dei.paj.entity.Interest;
import pt.uc.dei.paj.entity.Participation;
import pt.uc.dei.paj.entity.Project;
import pt.uc.dei.paj.entity.Skill;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.general.NotificationType;
import pt.uc.dei.paj.general.ProjectStatus;
import pt.uc.dei.paj.general.Role;

@RequestScoped
public class ProjectBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ProjectBean.class.getName());
	@Inject
	UserDao userDao;
	@Inject
	ProjectDao projectDao;
	@Inject
	IdeaNecessityDao ideaNecessityDao;
	@Inject
	ParticipationDao participationDao;
	@Inject
	SkillDao skillDao;
	@Inject
	InterestDao interestDao;

	/**
	 * Returns the project according to the id received if not deleted
	 * 
	 * @param id
	 * @return
	 */
	public Project findProjectIfNotDeleted(int id) {
		try {
			List<Project> projectToFind = projectDao.findEntityIfNotDeleted(id);
			if (projectToFind != null && !projectToFind.isEmpty() && projectToFind.get(0) != null) {
				return projectToFind.get(0);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("ERROR IN: findProjectIfNotDeleted");
			return null;
		}
		return null;
	}

	/**
	 * Returns the project according to the id received
	 * 
	 * @param id
	 * @return
	 */
	public Project findProjectById(int id) {
		try {
			Project projectToFind = projectDao.find(id);
			if (projectToFind != null) {
				return projectToFind;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("ERROR IN: findProjectById");
			return null;
		}
		return null;
	}

	/**
	 * Returns the projectDTO according to the id received
	 * 
	 * @param id
	 * @return ProjectDTO
	 */
	public ProjectDTO findProjectDTOIfNotDeleted(int id) {
		try {
			List<Project> projectToFind = projectDao.findEntityIfNotDeleted(id);
			if (projectToFind != null && !projectToFind.isEmpty() && projectToFind.get(0) != null) {
				return projectDao.convertEntityToDTO(projectToFind.get(0));
			}
			return null;
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("ERROR IN: findProjectDTOIfNotDeleted");
			return null;
		}
	}

	/**
	 * Finds AllProjects
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ProjectDTO> findAllProjects() {
		try {
			List<Project> projectList = projectDao.findAllProjectsOrdered();
			if (!projectList.isEmpty() && projectList.get(0) != null) {
				return projectDao.convertListEntityToDTO(projectList);
			}
			return new ArrayList<ProjectDTO>();
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("ERROR IN: findAllProjects");
			return null;
		}

	}

	/**
	 * Finds findAllProjectsConcluded
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ProjectDTO> findAllProjectsConcluded() {
		try {
			List<Project> projectList = projectDao.findAllProjectsConcluded();
			if (!projectList.isEmpty() && projectList.get(0) != null) {
				return projectDao.convertListEntityToDTO(projectList);
			}
			return new ArrayList<ProjectDTO>();
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("ERROR IN: findAllProjectsConcluded");
			return null;
		}

	}

	/**
	 * Finds findAllProjectsInProgress
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ProjectDTO> findAllProjectsInProgress() {
		try {
			List<Project> projectList = projectDao.findAllProjectsInProgress();
			if (!projectList.isEmpty() && projectList.get(0) != null) {
				return projectDao.convertListEntityToDTO(projectList);
			}
			return new ArrayList<ProjectDTO>();
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("ERROR IN: findAllProjectsInProgress");
			return null;
		}

	}

	public List<ParticipationDTO> findAllMembersOfProject(int idProject) {
		try {
			List<Participation> userList = participationDao.listOfMembersAndAdminsProject(idProject);

			if (userList != null && !userList.isEmpty()) {
				List<ParticipationDTO> participationDtoList = new ArrayList<ParticipationDTO>();
				for (Participation participation : userList) {

					participationDtoList.add(participationDao.convertEntityToDTO(participation));
				}
				return participationDtoList;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("ERROR IN: findAllMembersOfProject");

			return new ArrayList<ParticipationDTO>();
		}
		return new ArrayList<ParticipationDTO>();
	}

	public boolean deleteParticipationFromUser(int idProject, String userEmail) {

		List<Participation> participationList = participationDao.participationsUserCertainProject(userEmail, idProject);

		if (participationList != null && participationList.size() == 1 && participationList.get(0) != null) {
			try {
				participationDao.remove(participationList.get(0));
				return true;
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}

		}
		return false;

	}

	public boolean changeParticipationFromUser(int idProject, String userEmail) {

		List<Participation> participationList = participationDao.participationsUserCertainProject(userEmail, idProject);

		if (participationList != null && participationList.size() == 1 && participationList.get(0) != null) {
			try {
				participationList.get(0).setRole(Role.NOTPARTICIPATINGANYMORE);
				participationDao.merge(participationList.get(0));
				return true;
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}

		}
		return false;

	}

	/**
	 * Returns the projectDTO according to the id received
	 * 
	 * @param id
	 * @return ProjectDTO
	 */
	public List<ProjectDTO> findAllProjectsWhereUserIsMember(String email) {

		try {
			List<ProjectDTO> listToReturn = new ArrayList<ProjectDTO>();
			List<Participation> projectList = projectDao.findAllProjectsWhereUserIsMember(email);
			if (!projectList.isEmpty() && projectList.get(0) != null) {
				for (Participation participation : projectList) {
					listToReturn.add(projectDao.convertEntityToDTO(participation.getProjectParticipation()));
				}

				return listToReturn;
			}
			return new ArrayList<ProjectDTO>();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR IN: findAllProjectsWhereUserIsMember");
			return null;
		}
	}

	/**
	 * Returns the projectDTO find all the pending requests from project
	 * 
	 * @param id
	 * @return ProjectDTO
	 */
	public List<ProjectDTO> findAllProjectsWithPendingInvites(String email) {

		try {
			List<ProjectDTO> listToReturn = new ArrayList<ProjectDTO>();
			List<Participation> projectList = participationDao.pendingParticipationsFromAUserInvites(email);
			if (!projectList.isEmpty() && projectList.get(0) != null) {
				for (Participation participation : projectList) {
					listToReturn.add(projectDao.convertEntityToDTO(participation.getProjectParticipation()));
				}

				return listToReturn;
			}
			return new ArrayList<ProjectDTO>();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR IN: findAllProjectsWithPendingInvites");
			return null;
		}
	}

	/**
	 * Returns the projectDTO find all the pending requests from project
	 * 
	 * @param id
	 * @return ProjectDTO
	 */
	public List<UserDTO> findAllUsersWithPendingRequestsToAProjectPending(int idProject) {

		try {

			List<UserDTO> listToReturn = new ArrayList<UserDTO>();
			List<Participation> projectList = participationDao.pendingParticipationsFromAProject(idProject);

			if (!projectList.isEmpty() && projectList.get(0) != null) {

				for (Participation participation : projectList) {

					if (participationDao
							.participationsUserProjectActive(participation.getUserParticipation().getEmail())
							.isEmpty()) {
						listToReturn.add(userDao.convertEntitytoDTO(participation.getUserParticipation()));
					}
				}
				return listToReturn;
			}
			return new ArrayList<UserDTO>();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR IN: findAllUsersWithPendingRequestsToAProjectPending");
			return null;
		}
	}

	/**
	 * Returns the projectDTO find all the pending requests from project
	 * 
	 * @param id
	 * @return ProjectDTO
	 */
	public List<UserDTO> findAllUsersWithPendingInvitesToAProjectPending(int idProject) {

		try {

			List<UserDTO> listToReturn = new ArrayList<UserDTO>();
			List<Participation> projectList = participationDao.pendingInvitesFromAProject(idProject);
			if (!projectList.isEmpty() && projectList.get(0) != null) {
				for (Participation participation : projectList) {
					if (participationDao
							.participationsUserProjectActive(participation.getUserParticipation().getEmail())
							.isEmpty()) {
						;
						listToReturn.add(userDao.convertEntitytoDTO(participation.getUserParticipation()));
					}
				}
				return listToReturn;
			}
			return new ArrayList<UserDTO>();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR IN: findAllUsersWithPendingInvitesToAProjectPending");
			return null;
		}
	}

	/**
	 * Finds AllProjects Marked as favorite by User
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ProjectDTO> findAllProjectsMarkedAsFavoriteByUser(String email) {
		try {
			List<Project> projectList = projectDao.findAllProjectsUserMarkedAsFavorite(email);
			if (!projectList.isEmpty() && projectList.get(0) != null) {
				return projectDao.convertListEntityToDTO(projectList);

			}
			return new ArrayList<ProjectDTO>();
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("ERROR IN: findAllProjectsMarkedAsFavoriteByUser");
			return null;
		}

	}

	/**
	 * Finds All Skills from a Project
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SkillDTO> findAllSkillAssociatedProject(int idProject) {
		try {
			List<SkillDTO> listToReturn = new ArrayList<SkillDTO>();
			List<Skill> skillList = projectDao.findAllSkillAssociatedProject(idProject);
			if (!skillList.isEmpty() && skillList.get(0) != null) {
				for (Skill skill : skillList) {
					listToReturn.add(skillDao.convertEntityToDto(skill));
				}

				return listToReturn;
			}
			return new ArrayList<SkillDTO>();
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("ERROR IN: findAllSkillAssociatedProject");
			return null;
		}

	}

	/**
	 * Finds All Interests from a Project
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<InterestDTO> findAllInterestsAssociatedProject(int idProject) {
		try {
			List<InterestDTO> listToReturn = new ArrayList<InterestDTO>();
			List<Interest> interestList = projectDao.findAllInterestsAssociatedProject(idProject);
			if (!interestList.isEmpty() && interestList.get(0) != null) {
				for (Interest interest : interestList) {
					listToReturn.add(interestDao.convertEntityToDto(interest));
				}

				return listToReturn;
			}
			return new ArrayList<InterestDTO>();
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("ERROR IN: findAllInterestsAssociatedProject");
			return null;
		}

	}

	/**
	 * Finds All IdeaNecessity from a Project
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<IdeaNecessityDTO> findAllIdeasNecessityAssociatedProject(int idProject) {
		try {
			List<IdeaNecessityDTO> listToReturn = new ArrayList<IdeaNecessityDTO>();
			List<IdeaNecessity> ideaNecessityList = projectDao.findAllIdeasNecessityAssociatedProject(idProject);
			if (!ideaNecessityList.isEmpty() && ideaNecessityList.get(0) != null) {
				for (IdeaNecessity ideaNecessity : ideaNecessityList) {
					listToReturn.add(ideaNecessityDao.convertEntityToDto(ideaNecessity));
				}

				return listToReturn;
			}
			return new ArrayList<IdeaNecessityDTO>();
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("ERROR IN: findAllIdeasNecessityAssociatedProject");
			return null;
		}

	}

	/**
	 * Verifies if the user has a participation
	 * 
	 * @param idProject
	 * @param email
	 * @return
	 */
	public boolean verifyIfTheUserIsParticipatingOnThProjectEvenIfAsInviteRequest(int idProject, String email) {
		try {
			List<Participation> participation = participationDao.participationFromUserAndFromProject(idProject, email);

			if (participation != null && participation.size() == 1 && participation.get(0) != null) {
				return true;
			}

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("ERROR IN: verifyIfTheUserIsParticipatingOnThProjectEvenIfAsInviteRequest");
			return false;
		}
		return false;
	}

	/**
	 * Verifies if the user is already a member
	 * 
	 * @param idProject
	 * @param email
	 * @return
	 */
	public boolean verifyIfTheUserIsMemberOfAProject(int idProject, String email) {
		try {
			List<Participation> participation = participationDao.verifyIfTheUserIsMemberOfAProject(email, idProject);

			if (participation != null && participation.size() == 1 && participation.get(0) != null) {
				return true;
			}

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("ERROR IN: verifyIfTheUserIsParticipatingOnThProjectEvenIfAsInviteRequest");
			return false;
		}
		return false;
	}

	/**
	 * Method that adds a project to the DB
	 * 
	 * @param newProjectDTO
	 * @param userProject
	 * @param idProjectIdeas
	 * @return
	 */
	public boolean addProject(ProjectDTO newProjectDTO, User userProject, String idProjectIdeas) {
		try {
			Project newProject = projectDao.convertDtoToEntity(newProjectDTO);
			List<IdeaNecessity> listIdeaNecessities = getIdeasNecessity(idProjectIdeas);
			Set<IdeaNecessity> set = new HashSet<IdeaNecessity>(listIdeaNecessities);
			listIdeaNecessities.clear();
			listIdeaNecessities.addAll(set);

			if (userProject != null && newProject != null && listIdeaNecessities != null
					&& !listIdeaNecessities.isEmpty()) {
				newProject.setUserJoinProject(userProject);
				newProject.setProjectStatus(ProjectStatus.INPROGRESS);

				projectDao.persist(newProject);
				ParticipationDTO participationDTO = new ParticipationDTO();
				participationDTO.setEmailMember(userProject.getEmail());
				participationDTO.setIdProject(newProject.getIdProject());
				participationDTO.setRole("admin");

				createParticipation(participationDTO, newProject);
				if (associateIdeaNecessityToProject(listIdeaNecessities, newProject)
						&& deleteRequestParticipations(userProject)) {
					return true;
				}
			}

		} catch (

		Exception e) {
			e.printStackTrace();
			System.out.println("AddProject - addIdea");
			return false;
		}
		return false;
	}

	/**
	 * Method that alters the state of the project
	 * 
	 * @param idProject
	 * @return
	 */
	public boolean concludeProject(Project projectAux) {

		try {
			if (projectAux.getProjectStatus() != ProjectStatus.CONCLUDED) {
				projectAux.setProjectStatus(ProjectStatus.CONCLUDED);
				projectDao.merge(projectAux);

				return true;
			}
			return false;

		} catch (Exception e) {
			System.out.println("ERROR ON: concludeProject");
			return false;
		}
	}

	/**
	 * Method that edits a project on the the DB
	 * 
	 * @param newProjectDTO
	 * @param userProject
	 * @param idProjectIdeas
	 * @return
	 */
	public boolean editProject(ProjectDTO editProjectDTO, Project projecToEdit) {
		try {

			Project editProject = projectDao.editProjectDTOtoEntity(editProjectDTO, projecToEdit);
			if (projecToEdit.getUserJoinProject() != null && editProject != null) {

				if (editProjectDTO.getSkillAssociatedList() != null
						&& !editProjectDTO.getSkillAssociatedList().isEmpty()) {
					for (int skillElement : editProjectDTO.getSkillAssociatedList()) {
						associateSkillToProject(skillElement, projecToEdit);
					}
				}

				if (editProjectDTO.getInterestAssociatedList() != null
						&& !editProjectDTO.getInterestAssociatedList().isEmpty()) {
					for (int interestElement : editProjectDTO.getInterestAssociatedList()) {
						associateInterestToProject(interestElement, projecToEdit);
					}
				}
				if (editProjectDTO.getImageProject() != null) {
					projecToEdit.setImageProject(editProjectDTO.getImageProject());
				}


				
				projectDao.merge(editProject);
				if (editProjectDTO.getIdeaNecessityAssociatedList() != null
						&& !editProjectDTO.getIdeaNecessityAssociatedList().isEmpty()) {
					associateIdeaNecessityToEditProject(editProjectDTO.getIdeaNecessityAssociatedList(), editProject);
				}

				return true;
			}

		} catch (Exception e) {
			System.out.println("ERROR ON editProject");
			return false;
		}
		return false;
	}

	/**
	 * Method that associates a Project to an idea
	 * 
	 * @param list
	 * @param newProject
	 * @return
	 */
	public boolean associateIdeaNecessityToEditProject(List<Integer> list, Project editProject) {
		try {

			List<IdeaNecessity> ideaNecessityList = new ArrayList<IdeaNecessity>();

			for (Integer integer : list) {
				ideaNecessityList.add(ideaNecessityDao.find(integer));
			}
			for (IdeaNecessity ideaNecessity2 : editProject.getIdeaNecessityAssociatedList()) {
				ideaNecessityList.add(ideaNecessity2);
			}
			Set<IdeaNecessity> set = new HashSet<>(ideaNecessityList);
			ideaNecessityList.clear();
			ideaNecessityList.addAll(set);

			editProject.getIdeaNecessityAssociatedList().clear();

			for (IdeaNecessity idElement : ideaNecessityList) {
				List<Project> projectList = ideaNecessityDao.checkIProjectAlreadyAdded(editProject.getIdProject(),
						idElement.getId());
				if (projectList == null || projectList.isEmpty()) {
					idElement.getProjectAssociatedList().add(editProject);
					editProject.getIdeaNecessityAssociatedList().add(idElement);
					
		
					projectDao.merge(editProject);
					ideaNecessityDao.merge(idElement);
					return true;
				}
			}

		} catch (Exception e) {
			System.out.println("ERRO EM associateIdeaNecessityToProject");
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * Method that associates a Project to an idea
	 * 
	 * @param idProjectIdeas
	 * @param newProject
	 * @return
	 */
	public boolean associateIdeaNecessityToProject(List<IdeaNecessity> listIdeaNecessities, Project newProject) {
		try {

			// List<IdeaNecessity> listIdeaNecessities = getIdeasNecessity(idProjectIdeas);
			List<IdeaNecessity> listIdeaNecessitiesProject = newProject.getIdeaNecessityAssociatedList();

			for (IdeaNecessity ideaNecessity : listIdeaNecessities) {
				if (listIdeaNecessitiesProject != null) {
					if (ideaNecessity != null) {
						ideaNecessity.getProjectAssociatedList().add(newProject);
						newProject.getIdeaNecessityAssociatedList().add(ideaNecessity);

						projectDao.merge(newProject);
						ideaNecessityDao.merge(ideaNecessity);

					}
				}
			}
			return true;

		} catch (Exception e) {
			System.out.println("ERRO EM associateIdeaNecessityToProject");
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * Method that associates a Project to an idea
	 * 
	 * @param idProjectIdeas
	 * @param newProject
	 * @return
	 */
	public List<IdeaNecessityDTO> associateIdeaNecessityToProject(int idProjectIdeas, Project newProject) {
		try {

			IdeaNecessity ideaNecessity = ideaNecessityDao.find(idProjectIdeas);
			if (ideaNecessity != null) {
				List<IdeaNecessity> listIdeaNecessitiesProject = newProject.getIdeaNecessityAssociatedList();
				List<Project> project = projectDao.verifyIfIdeaNecessityAlreadyAssociated(newProject.getIdProject(),
						idProjectIdeas);
				if (project != null && project.isEmpty()) {
					listIdeaNecessitiesProject.add(ideaNecessity);
					projectDao.merge(newProject);
					ideaNecessity.getProjectAssociatedList().add(newProject);
					ideaNecessityDao.merge(ideaNecessity);
					return ideaNecessityDao.convertEntityToDto(newProject.getIdeaNecessityAssociatedList());
				}
			}

		} catch (Exception e) {
			System.out.println("ERRO EM associateIdeaNecessityToProject");
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * Method that associates a Project to an idea
	 * 
	 * @param idProjectIdeas
	 * @param newProject
	 * @return
	 */
	public List<IdeaNecessityDTO> disassociateIdeaNecessityToProject(int idProjectIdeas, Project newProject) {
		try {

			IdeaNecessity ideaNecessity = ideaNecessityDao.find(idProjectIdeas);
			List<IdeaNecessity> listIdeaNecessitiesProject = newProject.getIdeaNecessityAssociatedList();
			List<Project> project = projectDao.verifyIfIdeaNecessityAlreadyAssociated(newProject.getIdProject(),
					idProjectIdeas);

			if (ideaNecessity != null && listIdeaNecessitiesProject.size() > 1 && !project.isEmpty()) {

				for (int i = 0; i < listIdeaNecessitiesProject.size(); i++) {
					if (listIdeaNecessitiesProject.get(i).getId() == ideaNecessity.getId()) {
						listIdeaNecessitiesProject.remove(i);
						projectDao.merge(newProject);

					}
				}
				for (int i = 0; i < ideaNecessity.getProjectAssociatedList().size(); i++) {
					if (ideaNecessity.getProjectAssociatedList().get(i).getIdProject() == newProject.getIdProject()) {
						ideaNecessity.getProjectAssociatedList().remove(i);
						ideaNecessityDao.merge(ideaNecessity);
					}
				}

				return ideaNecessityDao.convertEntityToDto(newProject.getIdeaNecessityAssociatedList());
			}

		} catch (Exception e) {
			System.out.println("ERRO EM disassociateIdeaNecessityToProject");
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * Method that receives a string of ids separated by ',' and gets the necessity
	 * or ideas entity if they exist
	 * 
	 * @param idProjectIdeas
	 * @return
	 */

	public List<IdeaNecessity> getIdeasNecessity(String idProjectIdeas) {
		String[] ideasNecessityArray = idProjectIdeas.split(",");
		List<IdeaNecessity> ideaNecessitiesList = new ArrayList<IdeaNecessity>();
		for (int i = 0; i < ideasNecessityArray.length; i++) {
			List<IdeaNecessity> ideaNecessity = ideaNecessityDao.findEntityIfNotDeleted(ideasNecessityArray[i]);

			if (ideaNecessity != null && ideaNecessity.size() == 1 && ideaNecessity.get(0) != null) {

				ideaNecessitiesList.add(ideaNecessity.get(0));
			}
		}
		return ideaNecessitiesList;
	}

	public ArrayList<ParticipationDTO> verifyIfTheMemberExistsAndTheyreNotAlreadyAddedToTheProject(
			ArrayList<ParticipationDTO> participationDTOList, Project projectToAssociateMembers) {

		for (int i = 0; i < participationDTOList.size(); i++) {
			List<User> userAux = userDao.findEntityIfNotDeleted(participationDTOList.get(i).getEmailMember());
			if (userAux == null || userAux.isEmpty() || userAux.get(0) == null) {
				participationDTOList.remove(i);
			}
		}

		List<Participation> participationsAlreadyOnProject = projectToAssociateMembers.getMembersList();
		for (Participation participation : participationsAlreadyOnProject) {
			for (int i = 0; i < participationDTOList.size(); i++) {
				if (verifyIfTheUserIsParticipatingOnThProjectEvenIfAsInviteRequest(
						projectToAssociateMembers.getIdProject(), participationDTOList.get(i).getEmailMember())) {
					participationDTOList.remove(i);
				}
			}
		}

		return participationDTOList;
	}

	/**
	 * Method to associate members to a project
	 * 
	 * @param emails                    (String with emails separated by","
	 * @param projectToAssociateMembers
	 */
	public boolean associateMembersToproject(ArrayList<ParticipationDTO> participationDTOList,
			Project projectToAssociateMembers) {
		try {
			Long membersOfProject = participationDao
					.countParticipationsFromProject(projectToAssociateMembers.getIdProject());
			// int membersOfProject = projectToAssociateMembers.getMembersList().size();

			if (participationDTOList.size() > 0 && membersOfProject <= projectToAssociateMembers.getNumberMaxMembers()
					&& participationDTOList.size() <= projectToAssociateMembers.getNumberMaxMembers()
					&& participationDTOList
							.size() <= (projectToAssociateMembers.getNumberMaxMembers() - membersOfProject)) {

				for (ParticipationDTO participationDTO : participationDTOList) {

					List<User> user = userDao.findEntityIfNotDeleted(participationDTO.getEmailMember());
					if (user != null && user.get(0) != null) {
						// estou a verificar se o user ja tem algum projeto activo
						List<Participation> projectInprogressWhereUserIsCurrentlyParticipating = participationDao
								.participationsUserProjectActive(user.get(0).getEmail());
						if (projectInprogressWhereUserIsCurrentlyParticipating.isEmpty()) {
							Participation participation = createParticipation(participationDTO,
									projectToAssociateMembers);
							if (participation != null) {
								projectToAssociateMembers.getMembersList().add(participation);
							}
						}
					}
				}
				projectDao.merge(projectToAssociateMembers);
				return true;
			}
		} catch (Exception e) {
			System.out.println("ERROR ON : associateMembersToproject");

			return false;
		}
		return false;
	}

	public ArrayList<ParticipationDTO> verifyIfTheMemberExistsAndTheyreAddedToTheProject(
			ArrayList<ParticipationDTO> participationDTOList, Project projectToAssociateMembers) {

		for (int i = 0; i < participationDTOList.size(); i++) {
			List<User> userAux = userDao.findEntityIfNotDeleted(participationDTOList.get(i).getIdMember());
			if (userAux == null || userAux.isEmpty() || userAux.get(0) == null) {
				participationDTOList.remove(i);
			}
		}

		for (int i = 0; i < participationDTOList.size(); i++) {
			if (!verifyIfTheUserIsParticipatingOnThProjectEvenIfAsInviteRequest(
					projectToAssociateMembers.getIdProject(), participationDTOList.get(i).getEmailMember())) {
				participationDTOList.remove(i);
			}
		}

		return participationDTOList;
	}

	/**
	 * Method to associate members to a project
	 * 
	 * @param emails                    (String with emails separated by","
	 * @param projectToAssociateMembers
	 */
	public List<ParticipationDTO> disassociateMemberFromproject(int idUser, Project projectToDisassociateMembers) {

		Long membersOfProject = participationDao
				.countParticipationsFromProject(projectToDisassociateMembers.getIdProject());
		// int membersOfProject = projectToAssociateMembers.getMembersList().size();
		List<Participation> participationsAlreadyOnProject = projectToDisassociateMembers.getMembersList();

		if (membersOfProject > 0 && participationsAlreadyOnProject.size() > 0) {
			for (int j = 0; j < participationsAlreadyOnProject.size(); j++) {

				if (idUser == participationsAlreadyOnProject.get(j).getUserParticipation().getIdUser()) {
					// if (deleteParticipation(participationsAlreadyOnProject.get(j).getIdMember()))
					// {
					// participationsAlreadyOnProject.remove(j);

					participationsAlreadyOnProject.get(j).setRole(Role.NOTPARTICIPATINGANYMORE);
					participationDao.merge(participationsAlreadyOnProject.get(j));
					return findAllMembersOfProject(projectToDisassociateMembers.getIdProject());
				}
			}
		}

		return null;
	}

	/**
	 * Method to associate members to a project
	 * 
	 * @param emails                    (String with emails separated by","
	 * @param projectToAssociateMembers
	 */
	public boolean removeMemberproject(User user, Project projectToDisassociateMembers) {

		Long membersOfProject = participationDao
				.countParticipationsFromProject(projectToDisassociateMembers.getIdProject());
		// int membersOfProject = projectToAssociateMembers.getMembersList().size();
		List<Participation> participationsAlreadyOnProject = projectToDisassociateMembers.getMembersList();

		if (membersOfProject > 0 && participationsAlreadyOnProject.size() > 0) {
			for (int j = 0; j < participationsAlreadyOnProject.size(); j++) {

				if (user.getEmail().equals(participationsAlreadyOnProject.get(j).getUserParticipation().getEmail())) {
					if (deleteParticipation(participationsAlreadyOnProject.get(j).getIdMember())) {
						participationsAlreadyOnProject.remove(j);
					}
				}
			}

			return true;
		}

		return false;

	}

	/**
	 * Removes participation from DB
	 * 
	 * @param idParticipation
	 * @return
	 */
	public boolean deleteParticipation(int idParticipation) {

		Participation participation = participationDao.find(idParticipation);
		if (participation != null) {
			try {
				participationDao.remove(participation);
				return true;
			} catch (Exception e) {
				System.out.println("ERROR ON createParticipation ");
				return false;
			}
		}
		return false;
	}

	/**
	 * Creates a participation on the DB
	 * 
	 * @param participationDTO
	 * @param projectToAssociateMembers
	 * @return
	 */
	public Participation createParticipation(ParticipationDTO participationDTO, Project projectToAssociateMembers) {
		List<User> user = userDao.findUserByEmail(participationDTO.getEmailMember());
		if (user != null && user.size() == 1 && user.get(0) != null) {
			Participation newParticipation = new Participation();
			newParticipation.setRole(switchToRole(participationDTO.getRole()));
			newParticipation.setUserParticipation(user.get(0));
			newParticipation.setProjectParticipation(projectToAssociateMembers);
			try {
				participationDao.persist(newParticipation);
				return newParticipation;
			} catch (Exception e) {
				System.out.println("ERROR ON createParticipation ");
				return null;
			}
		}
		return null;
	}

	/***
	 * * Accept or decline invitation to be part of the project
	 * 
	 * @param projectToApproveMemberOrNot
	 * @param email
	 * @param approveOrNot
	 * @return
	 */
	public boolean acceptInviteOrNot(Project projectToAcceptInviteOrNot, User user, boolean acceptOrNot) {
		try {

			if (user != null) {
				List<Participation> projectInprogressWhereUserIsCurrentlyParticipating = participationDao
						.participationsUserProjectActive(user.getEmail());

				int numberOfVacanciesOnTheProject = (int) (projectToAcceptInviteOrNot.getNumberMaxMembers()
						- (participationDao.countParticipationsFromProject(projectToAcceptInviteOrNot.getIdProject())));

				List<Participation> participationUser = participationDao.participationFromUserAndFromProject(
						projectToAcceptInviteOrNot.getIdProject(), user.getEmail());

				for (Participation participation : participationUser) {
					if (participation.getProjectParticipation().getIdProject() == projectToAcceptInviteOrNot
							.getIdProject()) {
						if (participation != null && participationUser.size() > 0 && participation != null
								&& participation.getRole() == Role.INVITE) {
							if (acceptOrNot && numberOfVacanciesOnTheProject > 0
									&& projectInprogressWhereUserIsCurrentlyParticipating.isEmpty()
									&& deleteRequestParticipations(user)) {
								participation.setRole(Role.MEMBER);
								try {
									participationDao.merge(participation);
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else if (!acceptOrNot) {
								deleteParticipation(participation.getIdMember());
							}
							return true;
						}
					}
				}
			}

		} catch (Exception e) {
			System.out.println("ERROR ON acceptInviteOrNot ");
			// e.printStackTrace();
			return false;

		}
		return false;

	}

	public boolean deleteRequestParticipations(User user) {
		try {
			List<Participation> participationsRequest = participationDao
					.pendingParticipationsFromAUserRequests(user.getEmail());

			for (Participation participation : participationsRequest) {
				participationDao.remove(participation);
			}

			return true;
		} catch (Exception e) {
			return false;
			// TODO: handle exception
		}

	}

	/**
	 * Turns the user from NotApproved to Member if approved else, Deletes the
	 * participation if not approved
	 * 
	 * @param projectToApproveMemberOrNot
	 * @param email
	 * @param approveOrNot
	 * @return
	 */
	public boolean approveMemberOrNot(Project projectToApproveMemberOrNot, String email, boolean approveOrNot) {

		List<User> user = userDao.findUserByEmail(email);

		if (user != null && user.size() == 1 && user.get(0) != null) {

			List<Participation> projectInprogressWhereUserIsCurrentlyParticipating = participationDao
					.participationsUserProjectActive(user.get(0).getEmail());

			int numberOfVacanciesOnTheProject = (int) (projectToApproveMemberOrNot.getNumberMaxMembers()
					- (participationDao.countParticipationsFromProject(projectToApproveMemberOrNot.getIdProject())));

			List<Participation> participationUser = participationDao
					.participationFromUserAndFromProject(projectToApproveMemberOrNot.getIdProject(), email);
			for (Participation participation : participationUser) {

				if (participation.getProjectParticipation().getIdProject() == projectToApproveMemberOrNot
						.getIdProject()) {

					if (participation != null && participationUser.size() > 0 && participation != null
							&& participation.getRole() == Role.REQUEST) {

						if (approveOrNot && numberOfVacanciesOnTheProject > 0
								&& projectInprogressWhereUserIsCurrentlyParticipating.isEmpty()) {
							participation.setRole(Role.MEMBER);
							try {
								participationDao.merge(participation);
								return true;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						else if (!approveOrNot) {

							try {
								deleteParticipation(participationUser.get(0).getIdMember());
								return true;
							} catch (Exception e) {
								return false;
							}
							// TODO: handle exception
						}

					}

				}

			}
		}

		return false;

	}

	/**
	 * Method that creates a participation with role 'NotApproved' on DB
	 * 
	 * @param projectToRequest
	 * @param userThatRequestedToJoinProject
	 * @return
	 */
	public List<UserDTO> requestToJoinProject(Project projectToRequest, User userThatRequestedToJoinProject,
			String roleParticipation) {

		List<Participation> participationsAlreadyOnProject = projectToRequest.getMembersList();
		Long membersOfProject = participationDao.countParticipationsFromProject(projectToRequest.getIdProject());

		int numberOfVacanciesOnTheProject = projectToRequest.getNumberMaxMembers() - membersOfProject.intValue();

		// estou a verificar se o user ja tem algum projeto activo
		List<Participation> projectInprogressWhereUserIsCurrentlyParticipating = participationDao
				.participationsUserProjectActive(userThatRequestedToJoinProject.getEmail());

		if (numberOfVacanciesOnTheProject > 0 && projectInprogressWhereUserIsCurrentlyParticipating.isEmpty()) {

			List<Participation> checkIfUserUsedToParticipateInThisProject = participationDao
					.checkIfUserUsedToParticipateInThisProject(projectToRequest.getIdProject(),
							userThatRequestedToJoinProject.getEmail());
			if (checkIfUserUsedToParticipateInThisProject != null
					&& checkIfUserUsedToParticipateInThisProject.size() > 0
					&& checkIfUserUsedToParticipateInThisProject.get(0) != null) {

				if (roleParticipation.equals("INVITE")) {
					checkIfUserUsedToParticipateInThisProject.get(0).setRole(Role.INVITE);
				}

				else if (roleParticipation.equals("REQUEST")) {
					checkIfUserUsedToParticipateInThisProject.get(0).setRole(Role.REQUEST);
				}

				try {
					participationDao.merge(checkIfUserUsedToParticipateInThisProject.get(0));
				} catch (Exception e) {
					return null;
				}

			} else {

				ParticipationDTO participationDTO = new ParticipationDTO();

				participationDTO.setEmailMember(userThatRequestedToJoinProject.getEmail());
				participationDTO.setRole(roleParticipation);
				participationDTO.setIdProject(projectToRequest.getIdProject());

				participationsAlreadyOnProject.add(createParticipation(participationDTO, projectToRequest));
				projectDao.merge(projectToRequest);
			}
			if (roleParticipation.equals("INVITE")) {
				return findAllUsersWithPendingInvitesToAProjectPending(projectToRequest.getIdProject());

			} else if (roleParticipation.equals("REQUEST")) {
				return findAllUsersWithPendingRequestsToAProjectPending(projectToRequest.getIdProject());
			}
		}

		return null;

	}

	/**
	 * Alters the member role on the project according to the DTO received
	 * 
	 * @param projectToChangeMemberRole
	 * @param participationDto
	 * @return
	 */

	public List<ParticipationDTO> changeMemberRole(Project projectToChangeMemberRole,
			ParticipationDTO participationDto) {
		try {
			List<User> user = userDao.findUserById(participationDto.getIdMember());

			if (user != null && user.size() == 1 && user.get(0) != null) {
				participationDto.setEmailMember(user.get(0).getEmail());

				List<Participation> participationsAlreadyOnProject = projectToChangeMemberRole.getMembersList();
				Long membersOfProject = participationDao
						.countParticipationsFromProject(projectToChangeMemberRole.getIdProject());

				if (membersOfProject > 0) {

					for (int j = 0; j < participationsAlreadyOnProject.size(); j++) {
						if (participationsAlreadyOnProject.get(j).getUserParticipation().getEmail()
								.equals(participationDto.getEmailMember())) {

							participationsAlreadyOnProject.get(j).setRole(switchToRole(participationDto.getRole()));
							participationDao.merge(participationsAlreadyOnProject.get(j));
						}
					}
					return findAllMembersOfProject(projectToChangeMemberRole.getIdProject());
				}

			}

		} catch (Exception e) {
			System.out.println("ERROR ON changeMemberRole ");
			return null;
		}
		return null;
	}

	/**
	 * Associates interest to a Project
	 * 
	 * @param idInterest
	 * @param projectToAddInterest
	 * @return
	 */
	public List<InterestDTO> associateInterestToProject(int idInterest, Project projectToAddInterest) {
		try {

			Interest interestToAdd = interestDao.find(idInterest);

			if (interestToAdd != null) {
				List<Project> projectList = projectDao
						.verifyIfTheInterestIsAlreadyAssociated(projectToAddInterest.getIdProject(), idInterest);

				if (projectList == null || projectList.isEmpty()) {
					projectToAddInterest.getInterestAssociatedList().add(interestToAdd);
					projectDao.merge(projectToAddInterest);
					return interestDao.convertEntityToDto(projectToAddInterest.getInterestAssociatedList());
				} else {
					return null;
				}
			}

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: getAllVisitors");
			return null;
		}
		return null;
	}

	/**
	 * Associate skills to project
	 * 
	 * @param idSkill
	 * @param projectToAddSkill
	 * @return
	 */

	public List<SkillDTO> associateSkillToProject(int idSkill, Project projectToAddSkill) {
		try {
			Skill skillToAdd = skillDao.find(idSkill);
			if (skillToAdd != null) {
				List<Project> projectList = projectDao
						.verifyIfTheSkillIsAlreadyAssociated(projectToAddSkill.getIdProject(), idSkill);
				if (projectList == null || projectList.isEmpty()) {
					projectToAddSkill.getSkillAssociatedList().add(skillToAdd);
					projectDao.merge(projectToAddSkill);

					return skillDao.convertEntityToDto(projectToAddSkill.getSkillAssociatedList());

				} else {
					return null;

				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: getAllVisitors");
			return null;

		}
		return null;

	}

	/**
	 * Disassociate skills to project
	 * 
	 * @param idSkill
	 * @param projectToRemoveSkill
	 * @return
	 */
	public List<SkillDTO> disassociateSkillToProject(int idSkill, Project projectToRemoveSkill) {
		try {
			Skill skillToAdd = skillDao.find(idSkill);
			if (skillToAdd != null) {
				for (int i = 0; i < projectToRemoveSkill.getSkillAssociatedList().size(); i++) {
					if (projectToRemoveSkill.getSkillAssociatedList().get(i).getIdSkill() == skillToAdd.getIdSkill()) {
						projectToRemoveSkill.getSkillAssociatedList().remove(i);
						projectDao.merge(projectToRemoveSkill);
						return skillDao.convertEntityToDto(projectToRemoveSkill.getSkillAssociatedList());
					}
				}

			}
			return null;

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: getAllVisitors");
			return null;
		}

	}

	/**
	 * Disassociate interests to Project
	 * 
	 * @param idInterest
	 * @param projectToRemoveInterest
	 * @return
	 */
	public List<InterestDTO> disassociateInterestToProject(int idInterest, Project projectToRemoveInterest) {
		try {
			Interest interestToAdd = interestDao.find(idInterest);
			if (interestToAdd != null) {
				for (int i = 0; i < projectToRemoveInterest.getInterestAssociatedList().size(); i++) {
					if (projectToRemoveInterest.getInterestAssociatedList().get(i).getIdInterest() == interestToAdd
							.getIdInterest()) {
						projectToRemoveInterest.getInterestAssociatedList().remove(i);
						projectDao.merge(projectToRemoveInterest);
						return interestDao.convertEntityToDto(projectToRemoveInterest.getInterestAssociatedList());
					}
				}

			}
			return null;
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: getAllVisitors");
			return null;
		}

	}

	public boolean favoriteProject(int idProject, User user) {
		try {
			List<Project> projectListAUX = projectDao.checkIfProjectisAlreadyFavoritebyUser(idProject, user.getEmail());

			if (projectListAUX == null || projectListAUX.isEmpty()) {
				List<Project> projectEntity = projectDao.findEntityIfNotDeleted(idProject);
				if (projectEntity != null && projectEntity.size() == 1 && projectEntity.get(0) != null) {
					Project projectToFavorite = projectEntity.get(0);
					List<User> userList = projectToFavorite.getUserListFavorites();
					List<Project> projectList = user.getProjectListFavorites();

					userList.add(user);
					projectList.add(projectToFavorite);

					projectToFavorite.setUserListFavorites(userList);
					user.setProjectListFavorites(projectList);

					projectDao.merge(projectToFavorite);
					userDao.merge(user);
					return true;
				}

			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: favoriteProject");
			return false;
		}
		return false;
	}

	public boolean removeFavoriteProject(int idProject, User user) {
		try {
			List<Project> projectListAUX = projectDao.checkIfProjectisAlreadyFavoritebyUser(idProject, user.getEmail());

			if (projectListAUX != null && !projectListAUX.isEmpty()) {
				// devia ser uma query que devolve todos, com exceção do que se quer remover
				List<Project> projectList = new ArrayList<Project>();
				for (Project projectAux : user.getProjectListFavorites()) {
					if (projectAux.getIdProject() != idProject) {
						projectList.add(projectAux);
					}
				}
				user.setProjectListFavorites(projectList);
				userDao.merge(user);
				return true;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Error in: favoriteProject");
			return false;
		}
		return false;
	}

	/**
	 * Receives and string an returns the role accordingly
	 * 
	 * @param role
	 * @return
	 */
	public Role switchToRole(String role) {
		switch (role) {
		case "ADMIN":
			return Role.ADMIN;
		case "MEMBER":
			return Role.MEMBER;
		case "REQUEST":
			return Role.REQUEST;
		case "INVITE":
			return Role.INVITE;
		case "admin":
			return Role.ADMIN;
		case "member":
			return Role.MEMBER;
		case "request":
			return Role.REQUEST;
		case "invite":
			return Role.INVITE;
		default:
			return null;
		}

	}
}