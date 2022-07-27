package pt.uc.dei.paj.DTO;

import java.util.Date;

public class NotificationDTO {

	private Date notificationDate;
	public int idProject;
	public int idNotif;
	public int idUserThatNotificationIsAbout;
	private String description;
	private boolean read;
	private boolean deleted;

	
	public int getIdNotif() {
		return idNotif;
	}

	public void setIdNotif(int idNotif) {
		this.idNotif = idNotif;
	}

	public int getIdProject() {
		return idProject;
	}

	public void setIdProject(int idProject) {
		this.idProject = idProject;
	}

	public int getIdUserThatNotificationIsAbout() {
		return idUserThatNotificationIsAbout;
	}

	public void setIdUserThatNotificationIsAbout(int idUserThatNotificationIsAbout) {
		this.idUserThatNotificationIsAbout = idUserThatNotificationIsAbout;
	}

	public Date getNotificationDate() {
		return notificationDate;
	}

	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
