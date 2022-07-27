package pt.uc.dei.paj.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@NamedQueries({
		@NamedQuery(name = "Interest.findInterestByDescription", query = "SELECT i FROM Interest i WHERE i.description = :description"),
		@NamedQuery(name = "Interest.findInterestsbyWordSearch", query = "SELECT i FROM Interest i WHERE i.description LIKE CONCAT('%', :description,'%') AND i.deletedInterest=false"),
})
public class Interest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idInterest;

	@Column(name = "Description")
	private String description;

	@CreationTimestamp
	@Column(name = "Created_at", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;

	@Column(name = "deletedInterest")
	private boolean deletedInterest;

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(mappedBy = "userInterests")
	private List<User> usersList;

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(mappedBy = "interestAssociatedList")
	private List<IdeaNecessity> ideaNecessityAssociatedList;

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(mappedBy = "interestAssociatedList")
	private List<Project> projectAssociatedList;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private List<GroupVisibility> groupListInterest;
	
		
	public List<GroupVisibility> getGroupListInterest() {
		return groupListInterest;
	}

	public void setGroupListInterest(List<GroupVisibility> groupListInterest) {
		this.groupListInterest = groupListInterest;
	}

	public int getIdInterest() {
		return idInterest;
	}

	public void setIdInterest(int idInterest) {
		this.idInterest = idInterest;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDeletedInterest() {
		return deletedInterest;
	}

	public void setDeletedInterest(boolean deletedInterest) {
		this.deletedInterest = deletedInterest;
	}

	public List<User> getUsersList() {
		return usersList;
	}

	public void setUsersList(List<User> usersList) {
		this.usersList = usersList;
	}

	public List<IdeaNecessity> getIdeaNecessityAssociatedList() {
		return ideaNecessityAssociatedList;
	}

	public void setIdeaNecessityAssociatedList(List<IdeaNecessity> ideaNecessityAssociatedList) {
		this.ideaNecessityAssociatedList = ideaNecessityAssociatedList;
	}

	public List<Project> getProjectAssociatedList() {
		return projectAssociatedList;
	}

	public void setProjectAssociatedList(List<Project> projectAssociatedList) {
		this.projectAssociatedList = projectAssociatedList;
	}

	@Override
	public String toString() {
		return "Interest [idInterest=" + idInterest + ", creationTime=" + creationTime + ", description=" + description
				+ ", deletedInterest=" + deletedInterest + ", usersList=" + usersList + ", ideaNecessityAssociatedList="
				+ ideaNecessityAssociatedList + ", projectAssociatedList=" + projectAssociatedList + "]";
	}

}
