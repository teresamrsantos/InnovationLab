package pt.uc.dei.paj.entity;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import pt.uc.dei.paj.general.NotificationType;

@Entity
@NamedQueries({
		@NamedQuery(name = "Notification.findAllNotificationsFromUser", query = "SELECT n FROM Notification n WHERE n.userNotified.email = :email ORDER BY n.notificationTime DESC"),
		@NamedQuery(name = "Notification.countNotificationsToRead", query = "SELECT COUNT(n) FROM Notification n WHERE n.readNotification=false AND n.deletedNotification=false  and  n.userNotified.email = :email "),
})

public class Notification implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idNotification;

	@CreationTimestamp
	@Column(name = "NotificationTime", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date notificationTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "NotificationType")
	private NotificationType notificationType;

	@Column(name = "readNotification")
	private boolean readNotification;

	@Column(name = "deletedNotification")
	private boolean deletedNotification;

	private String usernameUserInvolved;
	
	private int projectInvolvedID;
	
	@ManyToOne
	private User userNotified;
	
	public int getIdNotification() {
		return idNotification;
	}

	public void setIdNotification(int idNotification) {
		this.idNotification = idNotification;
	}

	public Date getNotificationTime() {
		return notificationTime;
	}

	public void setNotificationTime(Date notificationTime) {
		this.notificationTime = notificationTime;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public boolean isReadNotification() {
		return readNotification;
	}

	public void setReadNotification(boolean readNotification) {
		this.readNotification = readNotification;
	}

	public boolean isDeletedNotification() {
		return deletedNotification;
	}

	public void setDeletedNotification(boolean deletedNotification) {
		this.deletedNotification = deletedNotification;
	}

	public User getUserNotified() {
		return userNotified;
	}

	public void setUserNotified(User userNotified) {
		this.userNotified = userNotified;
	}

	public String getUsernameUserInvolved() {
		return usernameUserInvolved;
	}

	public void setUsernameUserInvolved(String usernameUserInvolved) {
		this.usernameUserInvolved = usernameUserInvolved;
	}

	public int getProjectInvolvedID() {
		return projectInvolvedID;
	}

	public void setProjectInvolvedID(int projectInvolvedID) {
		this.projectInvolvedID = projectInvolvedID;
	}

	@Override
	public String toString() {
		return "Notification [idNotification=" + idNotification + ", notificationTime=" + notificationTime
				+ ", notificationType=" + notificationType + ", readNotification=" + readNotification
				+ ", deletedNotification=" + deletedNotification + ", userNotified=" + userNotified + "]";
	}

}
