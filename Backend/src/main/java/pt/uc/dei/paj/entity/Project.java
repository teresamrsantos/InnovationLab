package pt.uc.dei.paj.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import pt.uc.dei.paj.general.ProjectStatus;

@Entity

@NamedQueries({
		@NamedQuery(name = "Project.findAllProjectsInProgress", query = "SELECT p FROM Project p WHERE p.projectStatus = pt.uc.dei.paj.general.ProjectStatus.INPROGRESS"),
		@NamedQuery(name = "Project.findAllProjectsConcluded", query = "SELECT p FROM Project p WHERE p.projectStatus = pt.uc.dei.paj.general.ProjectStatus.CONCLUDED"),
		@NamedQuery(name = "Project.findAllProjects", query = "SELECT p FROM Project p ORDER BY p.projectStatus DESC"),
		@NamedQuery(name = "Project.findAllProjectsUserIsAuthor", query = "SELECT p FROM Project p WHERE p.userJoinProject.email = :email"),
		@NamedQuery(name = "Project.verifyIfUserAlreadyMarkedAsFavorite", query = "SELECT p FROM Project p INNER JOIN p.userListFavorites pu WHERE pu.email = :email AND p.idProject=:idProject "),
		@NamedQuery(name = "Project.verifyIfTheSkillIsAlreadyAssociated", query = "SELECT p FROM Project p INNER JOIN p.skillAssociatedList ps WHERE ps.idSkill = :idSkill AND p.idProject=:idProject"),
		@NamedQuery(name = "Project.verifyIfTheInterestIsAlreadyAssociated", query = "SELECT p FROM Project p INNER JOIN p.interestAssociatedList pi WHERE pi.idInterest = :idInterest AND p.idProject=:idProject"),
		@NamedQuery(name = "Project.verifyIfIdeaNecessityAlreadyAssociated", query = "SELECT p FROM Project p INNER JOIN p.ideaNecessityAssociatedList i WHERE i.id = :id AND p.idProject=:idProject"),
		@NamedQuery(name = "Project.findAllMembersOfProject", query = "SELECT m FROM Project p JOIN p.membersList m WHERE p.idProject = :idProject"),
		@NamedQuery(name = "Project.findAllProjectsWhereUserIsMember", query = "SELECT m FROM Project p JOIN p.membersList m WHERE m.userParticipation.email = :email ORDER BY p.projectStatus DESC"),
		@NamedQuery(name = "Project.findAllSkillAssociatedProject", query = "SELECT s  FROM Project p JOIN p.skillAssociatedList s WHERE p.idProject = :idProject"),
		@NamedQuery(name = "Project.findAllInterestsAssociatedProject", query = "SELECT i FROM Project p JOIN p.interestAssociatedList i WHERE p.idProject = :idProject"),
		@NamedQuery(name = "Project.findAllIdeasNecessityAssociatedProject", query = "SELECT i FROM Project p JOIN p.ideaNecessityAssociatedList i WHERE p.idProject = :idProject"),
		@NamedQuery(name = "Project.findAllProjectsAccordingWithProgress", query = "SELECT p from Project p WHERE p.projectStatus= :projectStatus"),
		@NamedQuery(name = "Project.findAllUsersThatSelectedProjectAsFavorite", query = "SELECT u FROM Project p JOIN p.userListFavorites u WHERE p.idProject = :idProject"),
		@NamedQuery(name = "Project.findAllIProjectsUserMarkedAsFavorite", query = "SELECT p FROM Project p  JOIN p.userListFavorites u WHERE u.email = :email ORDER BY p.projectStatus DESC"),
		@NamedQuery(name = "Project.findProjectsbyWordSearch", query = "SELECT p FROM Project p WHERE p.title LIKE CONCAT(:title,'%')"),
})
public class Project implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idProject;

	@CreationTimestamp
	@Column(name = "Created_at", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;

	@Column(name = "NumberMemberMax")
	private int numberMaxMembers;

	@Column(name = "ProjectStatus")
	@Enumerated(EnumType.STRING)
	private ProjectStatus projectStatus;

	@Column(name = "ProjectTitle")
	private String title;
	
	@Column(name = "Image")
	private String imageProject;
	
	@Column(name = "ProjectContent")
	private String projectContent;

	@Column(name = "ProjectResources")
	private String projectResources;

	@Column(name = "ProjectPlan")
	private String projectPlan;

	@Column(name = "deletedProject")
	private boolean deletedProject;

	@ManyToOne
	private User userJoinProject;

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(mappedBy = "projectListFavorites")
	private List<User> userListFavorites = new ArrayList<User>();

	@OneToMany(mappedBy = "projectParticipation")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Participation> membersList = new ArrayList<Participation>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(mappedBy = "projectAssociatedList", cascade = CascadeType.ALL)
	private List<IdeaNecessity> ideaNecessityAssociatedList = new ArrayList<IdeaNecessity>();;

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private List<Interest> interestAssociatedList = new ArrayList<Interest>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private List<Skill> skillAssociatedList = new ArrayList<Skill>();

	public int getIdProject() {
		return idProject;
	}

	public void setIdProject(int idProject) {
		this.idProject = idProject;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public int getNumberMaxMembers() {
		return numberMaxMembers;
	}

	public void setNumberMaxMembers(int numberMaxMembers) {
		this.numberMaxMembers = numberMaxMembers;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProjectContent() {
		return projectContent;
	}

	public void setProjectContent(String projectContent) {
		this.projectContent = projectContent;
	}

	public String getProjectResources() {
		return projectResources;
	}

	public void setProjectResources(String projectResources) {
		this.projectResources = projectResources;
	}

	public String getProjectPlan() {
		return projectPlan;
	}

	public void setProjectPlan(String projectPlan) {
		this.projectPlan = projectPlan;
	}

	public boolean isDeletedProject() {
		return deletedProject;
	}

	public void setDeletedProject(boolean deletedProject) {
		this.deletedProject = deletedProject;
	}

	public User getUserJoinProject() {
		return userJoinProject;
	}

	public void setUserJoinProject(User userJoinProject) {
		this.userJoinProject = userJoinProject;
	}

	public List<User> getUserListFavorites() {
		return userListFavorites;
	}

	public void setUserListFavorites(List<User> userListFavorites) {
		this.userListFavorites = userListFavorites;
	}

	public List<Participation> getMembersList() {
		return membersList;
	}

	public void setMembersList(List<Participation> membersList) {
		this.membersList = membersList;
	}

	public List<IdeaNecessity> getIdeaNecessityAssociatedList() {
		return ideaNecessityAssociatedList;
	}

	public void setIdeaNecessityAssociatedList(List<IdeaNecessity> ideaNecessityAssociatedList) {
		this.ideaNecessityAssociatedList = ideaNecessityAssociatedList;
	}

	public List<Interest> getInterestAssociatedList() {
		return interestAssociatedList;
	}

	public void setInterestAssociatedList(List<Interest> interestAssociatedList) {
		this.interestAssociatedList = interestAssociatedList;
	}

	public List<Skill> getSkillAssociatedList() {
		return skillAssociatedList;
	}

	public void setSkillAssociatedList(List<Skill> skillAssociatedList) {
		this.skillAssociatedList = skillAssociatedList;
	}

	public ProjectStatus getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(ProjectStatus projectStatus) {
		this.projectStatus = projectStatus;
	}

	public String getImageProject() {
		return imageProject;
	}

	public void setImageProject(String imageProject) {
		this.imageProject = imageProject;
	}

	@Override
	public String toString() {
		return "Project [idProject=" + idProject + ", creationTime=" + creationTime + ", numberMaxMembers="
				+ numberMaxMembers + ", projectStatus=" + projectStatus + ", title=" + title + ", imageProject="
				+ imageProject + ", projectContent=" + projectContent + ", projectResources=" + projectResources
				+ ", projectPlan=" + projectPlan + ", deletedProject=" + deletedProject + ", userJoinProject="
				+ userJoinProject + ", userListFavorites=" + userListFavorites + ", membersList=" + membersList
				+ ", ideaNecessityAssociatedList=" + ideaNecessityAssociatedList + ", interestAssociatedList="
				+ interestAssociatedList + ", skillAssociatedList=" + skillAssociatedList + "]";
	}

}
