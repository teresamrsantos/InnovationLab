package pt.uc.dei.paj.entity;

import java.io.Serializable;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import pt.uc.dei.paj.general.SkillType;

@Entity
@NamedQueries({
	@NamedQuery(name = "Skill.findSkillById", query = "SELECT s FROM Skill s WHERE s.idSkill=:idSkill"),
	@NamedQuery(name = "Skill.findSkillByDescription", query = "SELECT s FROM Skill s WHERE s.description = :description"),
	@NamedQuery(name = "Skill.findSkillsbyWordSearch", query = "SELECT s FROM Skill s WHERE s.description LIKE CONCAT('%',:description,'%') AND deletedSkill=false"),
})

public class Skill implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idSkill;

	@Column(name = "Description")
	private String description;
	
	@CreationTimestamp
	@Column(name = "Created_at", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "SkillType")
	private SkillType skillType;
	
	@Column(name = "deletedSkill")
	private boolean deletedSkill;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(mappedBy = "userSkills")
	private List<User> usersList;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(mappedBy = "skillAssociatedList")
	private List<IdeaNecessity> ideaNecessityAssociatedList;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(mappedBy = "skillAssociatedList")
	private List<Project> projectAssociatedList;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private List<GroupVisibility> groupListSkill;
	
	public List<GroupVisibility> getGroupListSkill() {
		return groupListSkill;
	}

	public void setGroupListSkill(List<GroupVisibility> groupListSkill) {
		this.groupListSkill = groupListSkill;
	}

	public int getIdSkill() {
		return idSkill;
	}

	public void setIdSkill(int idSkill) {
		this.idSkill = idSkill;
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

	public SkillType getSkillType() {
		return skillType;
	}

	public void setSkillType(SkillType skillType) {
		this.skillType = skillType;
	}

	public boolean isDeletedSkill() {
		return deletedSkill;
	}

	public void setDeletedSkill(boolean deletedSkill) {
		this.deletedSkill = deletedSkill;
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
		return "Skill [idSkill=" + idSkill + ", creationTime=" + creationTime + ", description=" + description
				+ ", skillType=" + skillType + ", deletedSkill=" + deletedSkill + ", usersList=" + usersList
				+ ", ideaNecessityAssociatedList=" + ideaNecessityAssociatedList + ", projectAssociatedList="
				+ projectAssociatedList + "]";
	}

}
