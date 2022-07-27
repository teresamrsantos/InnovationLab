package pt.uc.dei.paj.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import pt.uc.dei.paj.general.Workplace;

@Entity
@NamedQueries({
	@NamedQuery(name = "GroupVisibility.findAllGroupsFromUser", query = "SELECT g FROM GroupVisibility g WHERE g.userGroup.email = :email AND g.deleted=false" ),
})
public class GroupVisibility implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int idGroup;
	
	@Column(name = "Workplace")
	@Enumerated(EnumType.STRING)
	private Workplace workplace;
	
	private  boolean deleted;
	
	@ManyToOne
	private User userGroup;

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(mappedBy = "groupListSkill",  cascade = CascadeType.REMOVE)
	private List<Skill> skillList = new ArrayList<Skill>();
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(mappedBy = "groupListInterest",  cascade = CascadeType.REMOVE)
	private List<Interest> interestList = new ArrayList<Interest>();
	
	public int getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(int idGroup) {
		this.idGroup = idGroup;
	}

	public Workplace getWorkplace() {
		return workplace;
	}

	public void setWorkplace(Workplace workplace) {
		this.workplace = workplace;
	}

	public User getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(User userGroup) {
		this.userGroup = userGroup;
	}

	public List<Skill> getSkillList() {
		return skillList;
	}

	public void setSkillList(List<Skill> skillList) {
		this.skillList = skillList;
	}

	public List<Interest> getInterestList() {
		return interestList;
	}

	public void setInterestList(List<Interest> interestList) {
		this.interestList = interestList;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
	
}
