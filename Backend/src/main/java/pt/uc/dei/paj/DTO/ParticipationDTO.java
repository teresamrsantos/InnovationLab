package pt.uc.dei.paj.DTO;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class ParticipationDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String emailMember;
	private String usernameMember;
	private String pictureUser;
	private String role;
	private int idMember;
	private int idProject;
	

	public String getEmailMember() {
		return emailMember;
	}
	public void setEmailMember(String emailMember) {
		this.emailMember = emailMember;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public String toString() {
		return "ParticipationDTO [email=" + emailMember + ", role=" + role + "]";
	}
	public int getIdMember() {
		return idMember;
	}
	public void setIdMember(int idMember) {
		this.idMember = idMember;
	}
	public int getIdProject() {
		return idProject;
	}
	public void setIdProject(int idProject) {
		this.idProject = idProject;
	}
	public String getUsernameMember() {
		return usernameMember;
	}
	public void setUsernameMember(String username) {
		this.usernameMember = username;
	}
	public String getPictureUser() {
		return pictureUser;
	}
	public void setPictureUser(String pictureUser) {
		this.pictureUser = pictureUser;
	} 

}
