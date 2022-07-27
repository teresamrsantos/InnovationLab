package pt.uc.dei.paj.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProjectDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private int idProject;
	private Date creationTime;
	private int numberMaxMembers;
	private String title;
	private String projectContent;
	private String projectStatus;
	private String projectResources;
	private String projectPlan;
	private boolean deletedProject;
	private UserDTO userDTOJoinProject;
	private String imageProject;
	private List<Integer> userListFavorites= new ArrayList<Integer>();
	private int numberOfMembers;
	private int numberVacancies;


	private List<ParticipationDTO> membersList = new ArrayList<ParticipationDTO>();
	private List<Integer> ideaNecessityAssociatedList = new ArrayList<Integer>();;

	private List<Integer> interestAssociatedList = new ArrayList<Integer>();

	private List<Integer> skillAssociatedList = new ArrayList<Integer>();

	public int getIdProject() {
		return idProject;
	}

	public void setIdProject(int idProject) {
		this.idProject = idProject;
	}



	public String getImageProject() {
		return imageProject;
	}

	public void setImageProject(String imageProject) {
		this.imageProject = imageProject;
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

	public List<Integer> getUserListFavorites() {
		return userListFavorites;
	}

	public void setUserListFavorites(List<Integer> userListFavorites) {
		this.userListFavorites = userListFavorites;
	}

	public List<ParticipationDTO> getMembersList() {
		return membersList;
	}

	public void setMembersList(List<ParticipationDTO> membersList) {
		this.membersList = membersList;
	}

	public List<Integer> getIdeaNecessityAssociatedList() {
		return ideaNecessityAssociatedList;
	}

	public void setIdeaNecessityAssociatedList(List<Integer> ideaNecessityAssociatedList) {
		this.ideaNecessityAssociatedList = ideaNecessityAssociatedList;
	}

	public List<Integer> getInterestAssociatedList() {
		return interestAssociatedList;
	}

	public void setInterestAssociatedList(List<Integer> interestAssociatedList) {
		this.interestAssociatedList = interestAssociatedList;
	}

	public List<Integer> getSkillAssociatedList() {
		return skillAssociatedList;
	}

	public void setSkillAssociatedList(List<Integer> skillAssociatedList) {
		this.skillAssociatedList = skillAssociatedList;
	}

	public UserDTO getUserDTOJoinProject() {
		return userDTOJoinProject;
	}

	public void setUserDTOJoinProject(UserDTO userDTOJoinProject) {
		this.userDTOJoinProject = userDTOJoinProject;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	@Override
	public String toString() {
		return "ProjectDTO [idProject=" + idProject + ", creationTime=" + creationTime + ", numberMaxMembers="
				+ numberMaxMembers + ", title=" + title + ", projectContent=" + projectContent + ", projectResources="
				+ projectResources + ", projectPlan=" + projectPlan + ", deletedProject=" + deletedProject
				+ ", userDTOJoinProject=" + userDTOJoinProject + ", userListFavorites=" + userListFavorites
				+ ", membersList=" + membersList + ", ideaNecessityAssociatedList=" + ideaNecessityAssociatedList
				+ ", interestAssociatedList=" + interestAssociatedList + ", skillAssociatedList=" + skillAssociatedList
				+ "]";
	}

	public int getNumberOfMembers() {
		return numberOfMembers;
	}

	public void setNumberOfMembers(int numberOfMembers) {
		this.numberOfMembers = numberOfMembers;
	}

	public int getNumberVacancies() {
		return numberVacancies;
	}

	public void setNumberVacancies(int numberVacancies) {
		this.numberVacancies = numberVacancies;
	}

}
