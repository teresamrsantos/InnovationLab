package pt.uc.dei.paj.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import pt.uc.dei.paj.general.UserType;
import pt.uc.dei.paj.general.Visibility;
import pt.uc.dei.paj.general.Workplace;

@XmlRootElement
public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String email;
	private String pictureUrl;
	private String biography;
	private int availablehours;
	private Workplace workplace;
	private UserType userType;
	private Visibility visibility;
	private int numberOfActiveProjects;
	private List<UserDTO> listOfUsersAllowedToSeeProfile = new ArrayList<UserDTO>();
	private List<GroupVisibilityDTO> groupList = new ArrayList<GroupVisibilityDTO>();
	private List<SkillDTO> skillsList = new ArrayList<SkillDTO>();
	private List<InterestDTO> interestsList = new ArrayList<InterestDTO>();
	private boolean deleted;
	private int idUser;

	public UserDTO() {
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public Workplace getWorkplace() {
		return workplace;
	}

	public void setWorkplace(Workplace workplace) {
		this.workplace = workplace;
	}

	public int getAvailablehours() {
		return availablehours;
	}

	public void setAvailablehours(int availablehours) {
		this.availablehours = availablehours;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	public List<SkillDTO> getSkillsList() {
		return skillsList;
	}

	public void setSkillsList(List<SkillDTO> skillsList) {
		this.skillsList = skillsList;
	}

	public List<InterestDTO> getInterestsList() {
		return interestsList;
	}

	public void setInterestsList(List<InterestDTO> interestsList) {
		this.interestsList = interestsList;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public List<GroupVisibilityDTO> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<GroupVisibilityDTO> groupList) {
		this.groupList = groupList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<UserDTO> getListOfUsersAllowedToSeeProfile() {
		return listOfUsersAllowedToSeeProfile;
	}

	public void setListOfUsersAllowedToSeeProfile(List<UserDTO> listOfUsersAllowedToSeeProfile) {
		this.listOfUsersAllowedToSeeProfile = listOfUsersAllowedToSeeProfile;
	}

	public int getNumberOfActiveProjects() {
		return numberOfActiveProjects;
	}

	public void setNumberOfActiveProjects(int numberOfActiveProjects) {
		this.numberOfActiveProjects = numberOfActiveProjects;
	}

	@Override
	public String toString() {
		return "UserDTO [firstName=" + firstName + ", lastName=" + lastName + ", username=" + username + ", password="
				+ password + ", email=" + email + ", pictureUrl=" + pictureUrl + ", biography=" + biography
				+ ", availablehours=" + availablehours + ", workplace=" + workplace + ", userType=" + userType
				+ ", visibility=" + visibility + ", skillsList=" + skillsList + ", interestsList=" + interestsList
				+ ", deleted=" + deleted + ", idUser=" + idUser + "]";
	}

}
