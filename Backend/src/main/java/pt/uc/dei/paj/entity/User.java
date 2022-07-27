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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import pt.uc.dei.paj.general.UserType;
import pt.uc.dei.paj.general.Visibility;
import pt.uc.dei.paj.general.Workplace;

@Entity
@Table(name = "Users")
@NamedQueries({
		@NamedQuery(name = "User.findEncriptedPassword", query = "SELECT u.encriptedPassword FROM User u WHERE u.email =:email"),
		@NamedQuery(name = "User.findUserSkills", query = "SELECT s FROM User u JOIN u.userSkills s WHERE u.idUser = :idUser"),
		@NamedQuery(name = "User.findSalt", query = "SELECT u.salt FROM User u WHERE u.email =:email"),
		@NamedQuery(name = "User.findUserByEmail", query = "SELECT u FROM User u WHERE u.email =:email"),
		@NamedQuery(name = "User.findUserByUsername", query = "SELECT u FROM User u WHERE u.username =:username"),
		@NamedQuery(name = "User.findUserByID", query = "SELECT u FROM User u WHERE u.idUser =:idUser"),
		@NamedQuery(name = "User.findUserByActivation", query = "SELECT u FROM User u WHERE u.activation =:activation"),
		@NamedQuery(name = "User.findIdeaNecessitiesUserIsAuthor", query = "SELECT i FROM User u JOIN u.ideaNecessityList i WHERE u.idUser = :idUser"),
		@NamedQuery(name = "User.findIdeaNecessitiesUserIsAuthorMember", query = "SELECT i FROM User u JOIN u.ideaNecessityList i WHERE u.idUser = :idUser AND i.deleted=false"),
		@NamedQuery(name = "User.findUsersNotPaticipatingInProject", query = "SELECT u FROM User u where not u.email IN "
				+ "(select u.email FROM User u Join u.userParticipationList p  WHERE p.projectParticipation.idProject = :idProject AND NOT p.role = pt.uc.dei.paj.general.Role.NOTPARTICIPATINGANYMORE) "
				+ "AND NOT u.userType  = pt.uc.dei.paj.general.UserType.VISITOR  "
				+ "AND NOT u.email IN (select u.email FROM User u Join u.userParticipationList p WHERE p.projectParticipation.projectStatus = pt.uc.dei.paj.general.ProjectStatus.INPROGRESS AND  p.role = pt.uc.dei.paj.general.Role.ADMIN OR  p.projectParticipation.projectStatus = pt.uc.dei.paj.general.ProjectStatus.INPROGRESS AND p.role = pt.uc.dei.paj.general.Role.MEMBER)"),
		@NamedQuery(name = "User.findIdeaNecessitiesFavoriteOfTheUser", query = "SELECT f FROM User u JOIN u.ideaNecessityListFavorites f WHERE u.idUser = :idUser"),
		@NamedQuery(name = "User.findIdeaNecessitiesFavoriteOfTheUserMember", query = "SELECT f FROM User u JOIN u.ideaNecessityListFavorites f WHERE u.idUser = :idUser AND f.deleted=false"),
		@NamedQuery(name = "User.findAllUserToTalk", query = "SELECT u FROM User u WHERE u.email!=:email AND  u.deleted=false AND u.userType!=pt.uc.dei.paj.general.UserType.VISITOR"),
		@NamedQuery (name ="User.groupVisibilityInterestSkillWorkPlace", query = "SELECT u FROM User u  WHERE u.email IN (SELECT u.email FROM User u JOIN u.userSkills s WHERE s.idSkill IN :idSkillList) AND  u.email IN (SELECT u.email FROM User u JOIN u.userInterests i WHERE i.idInterest IN :idInterestList) AND u.workplace =:workplace"),
		@NamedQuery (name ="User.groupVisibilityWorkPlace", query = "SELECT u FROM User u  WHERE u.workplace =:workplace"),
		@NamedQuery (name ="User.groupVisibilityInterest", query = "SELECT u FROM User u  WHERE u.email IN  (SELECT u.email FROM User u JOIN u.userInterests i WHERE i.idInterest IN :idInterestList)"),
		@NamedQuery (name ="User.groupVisibilitySkill", query = "SELECT u FROM User u  WHERE u.email IN (SELECT u.email FROM User u JOIN u.userSkills s WHERE s.idSkill IN :idSkillList)"),
		@NamedQuery (name ="User.groupVisibilityInterestWorkPlace", query = "SELECT u FROM User u  WHERE u.email IN (SELECT u.email FROM User u JOIN u.userInterests i WHERE i.idInterest IN :idInterestList) AND u.workplace =:workplace"),
		@NamedQuery (name ="User.groupVisibilitySkillWorkPlace", query = "SELECT u FROM User u  WHERE u.email IN (SELECT u.email FROM User u JOIN u.userSkills s WHERE s.idSkill IN :idSkillList) AND u.workplace =:workplace"),
		@NamedQuery (name ="User.groupVisibilityInterestSkill", query = "SELECT u FROM User u  WHERE u.email IN (SELECT u.email FROM User u JOIN u.userSkills s WHERE s.idSkill IN :idSkillList) AND  u.email IN (SELECT u.email FROM User u JOIN u.userInterests i WHERE i.idInterest IN :idInterestList)"),
		@NamedQuery(name = "User.findAllUserExceptHimself", query = "SELECT u FROM User u WHERE NOT u.email=:email AND u.deleted=false"),	
})
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column (name="idUser")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idUser;
	
	@Column(name = "Token")
	private String token;
	
	@Column(name = "Activation")
	private String activation;

	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	@Column(name = "registered_at", nullable = false, updatable = false)
	private Date userRegisterTime;

	@Column(name = "FirstName", nullable = false)
	private String firstName;

	@Column(name = "LastName", nullable = false)
	private String lastName;

	@Column(name = "Username")
	private String username;

	@Column(name = "Password", nullable = false)
	private String encriptedPassword;

	@Column(name = "Salt", nullable = false)
	private String salt;

	@Column(name = "Email", nullable = false, unique = true)
	private String email;

	@Column(name = "Picture")
	private String pictureUrl;

	@Column(name = "Biography")
	private String biography;

	@Column(name = "HoursAvailable")
	private int availability;

	@Enumerated(EnumType.STRING)
	@Column(name = "ProfileVisibility")
	private Visibility visibility;

	@Enumerated(EnumType.STRING)
	@Column(name = "Workplace")
	private Workplace workplace;

	@Enumerated(EnumType.STRING)
	@Column(name = "UserType")
	private UserType userType;

	@Column(name = "deleted")
	private boolean deleted;

	@ManyToMany
	@JoinTable(name = "PermissionToSeeProfile")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<User> listOfUserwhoHavePermissionToSeeProfile = new ArrayList<User>();

	@ManyToMany(mappedBy = "listOfUserwhoHavePermissionToSeeProfile")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<User> listOfUserwhoGavePermissionToSeeProfile = new ArrayList<User>();

	@OneToMany(mappedBy = "userJoinPost")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Post> postList = new ArrayList<Post>();

	@OneToMany(mappedBy = "primaryKey.userJoinJustification")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Justification> justificationList = new ArrayList<Justification>();

	@ManyToMany
	@JoinTable(name = "IdeaNecessity_Vote")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<IdeaNecessity> ideaNecessityListVotes = new ArrayList<IdeaNecessity>();

	@OneToMany(mappedBy = "userJoinIdeaNecessity")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<IdeaNecessity> ideaNecessityList = new ArrayList<IdeaNecessity>();

	@ManyToMany
	@JoinTable(name = "IdeaNecessity_Favorites")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<IdeaNecessity> ideaNecessityListFavorites = new ArrayList<IdeaNecessity>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	@JoinTable(name = "IdeaNecessity_Availability")
	private List<IdeaNecessity> ideaNecessityListAvailability = new ArrayList<IdeaNecessity>();

	@OneToMany(mappedBy = "userJoinProject")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Project> projectList = new ArrayList<Project>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	@JoinTable(name = "Project_Favorites")
	private List<Project> projectListFavorites = new ArrayList<Project>();

	@OneToMany(mappedBy = "userParticipation")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Participation> userParticipationList = new ArrayList<Participation>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private List<Interest> userInterests;

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private List<Skill> userSkills;

	@OneToMany(mappedBy = "userReceiver")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Message> messageReceivedList = new ArrayList<Message>();

	@OneToMany(mappedBy = "userSender")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Message> messageSentList = new ArrayList<Message>();

	@OneToMany(mappedBy = "userNotified")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Notification> notificationList = new ArrayList<Notification>();

	
	@OneToMany(mappedBy = "userGroup", cascade = CascadeType.REMOVE)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<GroupVisibility> groupList = new ArrayList<GroupVisibility>();
	
	public User() {
	}

	public List<GroupVisibility> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<GroupVisibility> groupList) {
		this.groupList = groupList;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getActivation() {
		return activation;
	}

	public void setActivation(String activation) {
		this.activation = activation;
	}

	public Date getUserRegisterTime() {
		return userRegisterTime;
	}

	public void setUserRegisterTime(Date userRegisterTime) {
		this.userRegisterTime = userRegisterTime;
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

	public String getEncriptedPassword() {
		return encriptedPassword;
	}

	public void setEncriptedPassword(String encriptedPassword) {
		this.encriptedPassword = encriptedPassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
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

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public int getAvailability() {
		return availability;
	}

	public void setAvailability(int availability) {
		this.availability = availability;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	public Workplace getWorkplace() {
		return workplace;
	}

	public void setWorkplace(Workplace workplace) {
		this.workplace = workplace;
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

	public List<User> getListOfUserwhoHavePermissionToSeeProfile() {
		return listOfUserwhoHavePermissionToSeeProfile;
	}

	public void setListOfUserwhoHavePermissionToSeeProfile(List<User> listOfUserwhoHavePermissionToSeeProfile) {
		this.listOfUserwhoHavePermissionToSeeProfile = listOfUserwhoHavePermissionToSeeProfile;
	}

	public List<User> getListOfUserwhoGavePermissionToSeeProfile() {
		return listOfUserwhoGavePermissionToSeeProfile;
	}

	public void setListOfUserwhoGavePermissionToSeeProfile(List<User> listOfUserwhoGavePermissionToSeeProfile) {
		this.listOfUserwhoGavePermissionToSeeProfile = listOfUserwhoGavePermissionToSeeProfile;
	}

	public List<IdeaNecessity> getIdeaNecessityListVotes() {
		return ideaNecessityListVotes;
	}

	public void setIdeaNecessityListVotes(List<IdeaNecessity> ideaNecessityListVotes) {
		this.ideaNecessityListVotes = ideaNecessityListVotes;
	}

	public List<Post> getPostList() {
		return postList;
	}

	public void setPostList(List<Post> postList) {
		this.postList = postList;
	}

	public List<Justification> getJustificationList() {
		return justificationList;
	}

	public void setJustificationList(List<Justification> justificationList) {
		this.justificationList = justificationList;
	}

	public List<IdeaNecessity> getIdeaNecessityList() {
		return ideaNecessityList;
	}

	public void setIdeaNecessityList(List<IdeaNecessity> ideaNecessityList) {
		this.ideaNecessityList = ideaNecessityList;
	}

	public List<IdeaNecessity> getIdeaNecessityListFavorites() {
		return ideaNecessityListFavorites;
	}

	public void setIdeaNecessityListFavorites(List<IdeaNecessity> ideaNecessityListFavorites) {
		this.ideaNecessityListFavorites = ideaNecessityListFavorites;
	}

	public List<IdeaNecessity> getIdeaNecessityListAvailability() {
		return ideaNecessityListAvailability;
	}

	public void setIdeaNecessityListAvailability(List<IdeaNecessity> ideaNecessityListAvailability) {
		this.ideaNecessityListAvailability = ideaNecessityListAvailability;
	}

	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

	public List<Project> getProjectListFavorites() {
		return projectListFavorites;
	}

	public void setProjectListFavorites(List<Project> projectListFavorites) {
		this.projectListFavorites = projectListFavorites;
	}

	public List<Participation> getUserParticipationList() {
		return userParticipationList;
	}

	public void setUserParticipationList(List<Participation> userParticipationList) {
		this.userParticipationList = userParticipationList;
	}

	public List<Interest> getUserInterests() {
		return userInterests;
	}

	public void setUserInterests(List<Interest> userInterests) {
		this.userInterests = userInterests;
	}

	public List<Skill> getUserSkills() {
		return userSkills;
	}

	public void setUserSkills(List<Skill> userSkills) {
		this.userSkills = userSkills;
	}

	public List<Message> getMessageReceivedList() {
		return messageReceivedList;
	}

	public void setMessageReceivedList(List<Message> messageReceivedList) {
		this.messageReceivedList = messageReceivedList;
	}

	public List<Message> getMessageSentList() {
		return messageSentList;
	}

	public void setMessageSentList(List<Message> messageSentList) {
		this.messageSentList = messageSentList;
	}

	public List<Notification> getNotificationList() {
		return notificationList;
	}

	public void setNotificationList(List<Notification> notificationList) {
		this.notificationList = notificationList;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	@Override
	public String toString() {
		return "User [idUser=" + idUser + ", token=" + token + ", activation=" + activation + ", userRegisterTime="
				+ userRegisterTime + ", firstName=" + firstName + ", lastName=" + lastName + ", username=" + username
				+ ", encriptedPassword=" + encriptedPassword + ", salt=" + salt + ", email=" + email + ", pictureUrl="
				+ pictureUrl + ", biography=" + biography + ", availability=" + availability + ", visibility="
				+ visibility + ", workplace=" + workplace + ", userType=" + userType + ", deleted=" + deleted
				+ ", listOfUserwhoHavePermissionToSeeProfile=" + listOfUserwhoHavePermissionToSeeProfile
				+ ", listOfUserwhoGavePermissionToSeeProfile=" + listOfUserwhoGavePermissionToSeeProfile + ", postList="
				+ postList + ", justificationList=" + justificationList + ", ideaNecessityListVotes="
				+ ideaNecessityListVotes + ", ideaNecessityList=" + ideaNecessityList + ", ideaNecessityListFavorites="
				+ ideaNecessityListFavorites + ", ideaNecessityListAvailability=" + ideaNecessityListAvailability
				+ ", projectList=" + projectList + ", projectListFavorites=" + projectListFavorites
				+ ", userParticipationList=" + userParticipationList + ", userInterests=" + userInterests
				+ ", userSkills=" + userSkills + ", messageReceivedList=" + messageReceivedList + ", messageSentList="
				+ messageSentList + ", notificationList=" + notificationList + ", groupList=" + groupList + "]";
	}
	
}
