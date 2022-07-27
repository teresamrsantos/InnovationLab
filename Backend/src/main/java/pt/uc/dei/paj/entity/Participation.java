package pt.uc.dei.paj.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import pt.uc.dei.paj.general.Role;

@Entity
@NamedQueries({
		@NamedQuery(name = "Participation.countProjecParticipation", query = "SELECT COUNT(p) FROM Participation p WHERE p.projectParticipation.idProject = :idProject AND p.role = pt.uc.dei.paj.general.Role.ADMIN OR p.projectParticipation.idProject = :idProject AND  p.role= pt.uc.dei.paj.general.Role.MEMBER"),
		@NamedQuery(name = "Participation.projectParticipation", query = "SELECT p FROM Participation p WHERE p.projectParticipation.idProject = :idProject AND p.role = pt.uc.dei.paj.general.Role.ADMIN OR p.projectParticipation.idProject = :idProject AND  p.role= pt.uc.dei.paj.general.Role.MEMBER"),
		@NamedQuery(name = "Participation.participationsUserCertainProject", query = "SELECT p FROM Participation p WHERE p.userParticipation.email = :email AND p.projectParticipation.idProject = :idProject "),
		@NamedQuery(name = "Participation.participationsUserProjectActive", query = "SELECT p FROM Participation p WHERE p.projectParticipation.projectStatus = pt.uc.dei.paj.general.ProjectStatus.INPROGRESS AND p.userParticipation.email = :email AND NOT p.role = pt.uc.dei.paj.general.Role.INVITE AND NOT p.role = pt.uc.dei.paj.general.Role.REQUEST AND NOT p.role = pt.uc.dei.paj.general.Role.NOTPARTICIPATINGANYMORE"),
		@NamedQuery(name = "Participation.verifyIfTheUserIsMemberOfAProject", query = "SELECT p FROM Participation p WHERE p.projectParticipation.idProject = :idProject AND p.userParticipation.email = :email AND NOT p.role = pt.uc.dei.paj.general.Role.INVITE AND NOT p.role = pt.uc.dei.paj.general.Role.REQUEST AND NOT p.role = pt.uc.dei.paj.general.Role.NOTPARTICIPATINGANYMORE"),
		@NamedQuery(name = "Participation.userAlredyMember", query = "SELECT p FROM Participation p WHERE p.projectParticipation.idProject = :idProject AND p.userParticipation.email = :email AND NOT p.role = pt.uc.dei.paj.general.Role.NOTPARTICIPATINGANYMORE"),
		@NamedQuery(name = "Participation.pendingParticipationsFromAProject", query = "SELECT p FROM Participation p WHERE p.projectParticipation.idProject = :idProject AND p.role = pt.uc.dei.paj.general.Role.REQUEST"),
		@NamedQuery(name = "Participation.pendingInvitesFromAProject", query = "SELECT p FROM Participation p WHERE p.projectParticipation.idProject = :idProject AND p.role = pt.uc.dei.paj.general.Role.INVITE"),
		@NamedQuery(name = "Participation.pendingParticipationsFromAUser", query = "SELECT p FROM Participation p WHERE p.userParticipation.email = :email AND p.role = pt.uc.dei.paj.general.Role.INVITE"),
		@NamedQuery(name = "Participation.pendingParticipationsFromAUserRequests", query = "SELECT p FROM Participation p WHERE p.userParticipation.email = :email AND p.role = pt.uc.dei.paj.general.Role.REQUEST"),
		@NamedQuery(name = "Participation.checkIfUserUsedToParticipateInThisProject", query = "SELECT p FROM Participation p WHERE p.userParticipation.email = :email AND p.projectParticipation.idProject = :idProject AND p.role = pt.uc.dei.paj.general.Role.NOTPARTICIPATINGANYMORE"), })

public class Participation implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idMember;

	@Column(name = "Role")
	@Enumerated(EnumType.STRING)
	private Role role;

	private boolean deletedParticipation;

	@ManyToOne
	private Project projectParticipation;

	@ManyToOne
	private User userParticipation;

	public int getIdMember() {
		return idMember;
	}

	public void setIdMember(int idMember) {
		this.idMember = idMember;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Project getProjectParticipation() {
		return projectParticipation;
	}

	public void setProjectParticipation(Project projectParticipation) {
		this.projectParticipation = projectParticipation;
	}

	public User getUserParticipation() {
		return userParticipation;
	}

	public void setUserParticipation(User userParticipation) {
		this.userParticipation = userParticipation;
	}

	public boolean isDeletedParticipation() {
		return deletedParticipation;
	}

	public void setDeletedParticipation(boolean deletedParticipation) {
		this.deletedParticipation = deletedParticipation;
	}

	@Override
	public String toString() {
		return "Participation [idMember=" + idMember + ", role=" + role + ", deletedParticipation="
				+ deletedParticipation + ", projectParticipation=" + projectParticipation + ", userParticipation="
				+ userParticipation + "]";
	}

}
