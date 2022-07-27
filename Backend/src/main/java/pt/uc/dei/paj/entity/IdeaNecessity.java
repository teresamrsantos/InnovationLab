package pt.uc.dei.paj.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.hibernate.annotations.UpdateTimestamp;

import pt.uc.dei.paj.general.IdeaOrNecessity;

@Entity
@NamedQueries({
		@NamedQuery(name = "IdeaNecessity.checkIfTheSkillIsAlreadyAssociated", query = "SELECT p FROM IdeaNecessity p JOIN p.skillAssociatedList pu WHERE pu.idSkill = :idSkill AND p.id=:id"),
		@NamedQuery(name = "IdeaNecessity.checkIfTheInterestIsAlreadyAssociated", query = "SELECT p FROM IdeaNecessity p JOIN p.interestAssociatedList pu WHERE pu.idInterest = :idInterest AND p.id=:id"),
		@NamedQuery(name = "IdeaNecessity.checkIfUserAlreadyVote", query = "SELECT p FROM IdeaNecessity p INNER JOIN p.userListVote pu WHERE pu.email = :email AND p.id=:id"),
		@NamedQuery(name = "IdeaNecessity.checkIfAlreadyIsFavoriteUser", query = "SELECT p FROM IdeaNecessity p INNER JOIN p.userFavoriteList pu WHERE pu.email = :email AND p.id=:id"),
		@NamedQuery(name = "IdeaNecessity.checkIfUserAlreadyIsAvailability", query = "SELECT p FROM IdeaNecessity p INNER JOIN p.userAvailabilityList pu WHERE pu.email = :email AND p.id=:id"),
		@NamedQuery(name = "IdeaNecessity.checkIProjectAlreadyAdded", query = "SELECT p FROM IdeaNecessity p INNER JOIN p.projectAssociatedList pu WHERE pu.idProject = :idProject AND p.id=:id"),
		@NamedQuery(name = "IdeaNecessity.findIdOfLastIdeaNecessityPostedByUser", query = "SELECT MAX(id) FROM IdeaNecessity p WHERE p.userJoinIdeaNecessity.email=:email"),
		@NamedQuery(name = "IdeaNecessity.findAllSkillAssociateIdIdeaNecessity", query = "SELECT f FROM IdeaNecessity i JOIN i.skillAssociatedList f WHERE i.id = :id"),
		@NamedQuery(name = "IdeaNecessity.findAllInterestsAssociateIdIdeaNecessity", query = "SELECT f FROM IdeaNecessity i JOIN i.interestAssociatedList f WHERE i.id = :id"),
		@NamedQuery(name = "IdeaNecessity.findAllIdeaNecessitysUserIsAuthor", query = "SELECT f FROM IdeaNecessity f WHERE f.userJoinIdeaNecessity.email = :email"),
		@NamedQuery(name = "IdeaNecessity.findAllIdeaNecessitiesNoDeletedUserIsAuthor", query = "SELECT f FROM IdeaNecessity f WHERE  f.deleted=false AND f.userJoinIdeaNecessity.email = :email"),
		@NamedQuery(name = "IdeaNecessity.findAllUserWithAvailableToWorkIdIdeaNecessity", query = "SELECT f FROM IdeaNecessity i JOIN i.userAvailabilityList f WHERE i.id = :id AND f.deleted=false"),
		@NamedQuery(name = "IdeaNecessity.findProjectsAssociateIdIdeaNecessity", query = "SELECT p FROM IdeaNecessity i JOIN i.projectAssociatedList p WHERE i.id = :id"),
})
		

public class IdeaNecessity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@CreationTimestamp
	@Column(name = "Created_at", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;

	@UpdateTimestamp
	@Column(name = "Update_Date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;
	
	@Column(name = "IdeaNecessityTitle")
	private String title;

	@Column(name = "Image")
	private String imageIdeaNecessity;
	
	@Column(name = "Description")
	private String description;

	@Column(name = "IdeaOrNecessity")
	@Enumerated(EnumType.STRING)
	private IdeaOrNecessity ideaOrNecessity;

	@Column(name = "deletedIdeaNecessity")
	private boolean deleted;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "postJoin")
	private List<Post> postList = new ArrayList<Post>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "primaryKey.ideaId1")
	private List<Justification> justificationList1 = new ArrayList<Justification>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "primaryKey.ideaId2")
	private List<Justification> justificationList2 = new ArrayList<Justification>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(mappedBy = "ideaNecessityListVotes")
	private List<User> userListVote = new ArrayList<User>();

	@ManyToOne
	private User userJoinIdeaNecessity;

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(mappedBy = "ideaNecessityListFavorites")
	private List<User> userFavoriteList = new ArrayList<User>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(mappedBy = "ideaNecessityListAvailability")
	private List<User> userAvailabilityList = new ArrayList<User>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private List<Project> projectAssociatedList = new ArrayList<Project>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private List<Interest> interestAssociatedList = new ArrayList<Interest>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private List<Skill> skillAssociatedList = new ArrayList<Skill>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
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
		return deleted;
	}

	public void setDeletedIdeaNecessity(boolean deletedIdeaNecessity) {
		this.deleted = deletedIdeaNecessity;
	}

	public List<Post> getPostList() {
		return postList;
	}

	public void setPostList(List<Post> postList) {
		this.postList = postList;
	}

	public List<Justification> getJustificationList1() {
		return justificationList1;
	}

	public void setJustificationList1(List<Justification> justificationList1) {
		this.justificationList1 = justificationList1;
	}

	public List<Justification> getJustificationList2() {
		return justificationList2;
	}

	public void setJustificationList2(List<Justification> justificationList2) {
		this.justificationList2 = justificationList2;
	}

	public List<User> getUserListVote() {
		return userListVote;
	}

	public void setUserListVote(List<User> userListVote) {
		this.userListVote = userListVote;
	}

	public User getUserJoinIdeaNecessity() {
		return userJoinIdeaNecessity;
	}

	public void setUserJoinIdeaNecessity(User userJoinIdeaNecessity) {
		this.userJoinIdeaNecessity = userJoinIdeaNecessity;
	}

	public List<User> getUserFavoriteList() {
		return userFavoriteList;
	}

	public void setUserFavoriteList(List<User> userFavoriteList) {
		this.userFavoriteList = userFavoriteList;
	}

	public List<User> getUserAvailabilityList() {
		return userAvailabilityList;
	}

	public void setUserAvailabilityList(List<User> userAvailabilityList) {
		this.userAvailabilityList = userAvailabilityList;
	}

	public List<Project> getProjectAssociatedList() {
		return projectAssociatedList;
	}

	public void setProjectAssociatedList(List<Project> projectAssociatedList) {
		this.projectAssociatedList = projectAssociatedList;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getImageIdeaNecessity() {
		return imageIdeaNecessity;
	}

	public void setImageIdeaNecessity(String imageIdeaNecessity) {
		this.imageIdeaNecessity = imageIdeaNecessity;
	}

	@Override
	public String toString() {
		return "IdeaNecessity [id=" + id + ", creationTime=" + creationTime + ", updateTime=" + updateTime + ", title="
				+ title + ", description=" + description + ", ideaOrNecessity=" + ideaOrNecessity + ", deleted="
				+ deleted + ", postList=" + postList + ", justificationList1=" + justificationList1
				+ ", justificationList2=" + justificationList2 + ", userListVote=" + userListVote
				+ ", userJoinIdeaNecessity=" + userJoinIdeaNecessity + ", userFavoriteList=" + userFavoriteList
				+ ", userAvailabilityList=" + userAvailabilityList + ", projectAssociatedList=" + projectAssociatedList
				+ ", interestAssociatedList=" + interestAssociatedList + ", skillAssociatedList=" + skillAssociatedList
				+ "]";
	}

}
