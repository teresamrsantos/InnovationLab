package pt.uc.dei.paj.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import pt.uc.dei.paj.general.IdeaOrNecessity;
@XmlRootElement
public class IdeaNecessityDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String creationTime;
	private String updateTime;
	private String title;
	private String description;
	private IdeaOrNecessity ideaOrNecessity;
	private boolean deletedIdeaNecessity;
	private int idAuthor;
	private String nameAuthor;
	private String usernameAuthor;
	private String imageIdeaNecessity;
	private List<Integer> interestAssociatedList = new ArrayList<Integer>();
	private List<Integer> skillAssociatedList = new ArrayList<Integer>();
	private List<Integer> projectsAssociatedList = new ArrayList<Integer>();
	private List<Integer> voteList = new ArrayList<Integer>();
	private int vote;
	private List<Integer> favoriteList = new ArrayList<Integer>();
	private List<Integer> availabilityList = new ArrayList<Integer>();
	private List<JustificationDTO> ideaNecessityAssociatedList = new ArrayList<JustificationDTO>();

	public IdeaNecessityDTO() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public IdeaOrNecessity getIdeaOrNecessity() {
		return ideaOrNecessity;
	}

	public void setIdeaOrNecessity(IdeaOrNecessity ideaOrNecessity) {
		this.ideaOrNecessity = ideaOrNecessity;
	}

	public boolean isDeletedIdeaNecessity() {
		return deletedIdeaNecessity;
	}

	public void setDeletedIdeaNecessity(boolean deletedIdeaNecessity) {
		this.deletedIdeaNecessity = deletedIdeaNecessity;
	}

	public int getIdAuthor() {
		return idAuthor;
	}

	public void setIdAuthor(int idAuthor) {
		this.idAuthor = idAuthor;
	}

	public String getNameAuthor() {
		return nameAuthor;
	}

	public void setNameAuthor(String nameAuthor) {
		this.nameAuthor = nameAuthor;
	}

	public String getUsernameAuthor() {
		return usernameAuthor;
	}

	public void setUsernameAuthor(String usernameAuthor) {
		this.usernameAuthor = usernameAuthor;
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

	public List<Integer> getProjectsAssociatedList() {
		return projectsAssociatedList;
	}

	public void setProjectsAssociatedList(List<Integer> projectsAssociatedList) {
		this.projectsAssociatedList = projectsAssociatedList;
	}

	public int getVote() {
		return vote;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}

	public List<Integer> getFavoriteList() {
		return favoriteList;
	}

	public void setFavoriteList(List<Integer> favoriteList) {
		this.favoriteList = favoriteList;
	}

	public List<Integer> getAvailabilityList() {
		return availabilityList;
	}

	public void setAvailabilityList(List<Integer> availabilityList) {
		this.availabilityList = availabilityList;
	}

	public List<JustificationDTO> getIdeaNecessityAssociatedList() {
		return ideaNecessityAssociatedList;
	}

	public void setIdeaNecessityAssociatedList(List<JustificationDTO> ideaNecessityAssociatedList) {
		this.ideaNecessityAssociatedList = ideaNecessityAssociatedList;
	}

	public String getImageIdeaNecessity() {
		return imageIdeaNecessity;
	}


	public void setImageIdeaNecessity(String imageIdeaNecessity) {
		this.imageIdeaNecessity = imageIdeaNecessity;
	}

	public List<Integer> getVoteList() {
		return voteList;
	}

	public void setVoteList(List<Integer> voteList) {
		this.voteList = voteList;
	}

	@Override
	public String toString() {
		return "IdeaNecessityDTO [id=" + id + ", creationTime=" + creationTime + ", updateTime=" + updateTime
				+ ", title=" + title + ", description=" + description + ", ideaOrNecessity=" + ideaOrNecessity
				+ ", deletedIdeaNecessity=" + deletedIdeaNecessity + ", idAuthor=" + idAuthor + ", nameAuthor="
				+ nameAuthor + ", usernameAuthor=" + usernameAuthor + ", imageIdeaNecessity=" + imageIdeaNecessity
				+ ", interestAssociatedList=" + interestAssociatedList + ", skillAssociatedList=" + skillAssociatedList
				+ ", projectsAssociatedList=" + projectsAssociatedList + ", voteList=" + voteList + ", vote=" + vote
				+ ", favoriteList=" + favoriteList + ", availabilityList=" + availabilityList
				+ ", ideaNecessityAssociatedList=" + ideaNecessityAssociatedList + "]";
	}

}
