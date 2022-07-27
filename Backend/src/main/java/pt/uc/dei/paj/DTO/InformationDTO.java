package pt.uc.dei.paj.DTO;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InformationDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private int idUser;
	private Date dateLastMessage;
	private Long messageToRead;
	private String photo;
	private String name;
	private String username;

	public InformationDTO() {
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public Date getDateLastMessage() {
		return dateLastMessage;
	}

	public void setDateLastMessage(Date dateLastMessage) {
		this.dateLastMessage = dateLastMessage;
	}

	public Long getMessageToRead() {
		return messageToRead;
	}

	public void setMessageToRead(Long messageToRead) {
		this.messageToRead = messageToRead;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "InformationDTO [idUser=" + idUser + ", dateLastMessage=" + dateLastMessage + ", messageToRead="
				+ messageToRead + ", photo=" + photo + ", name=" + name + ", username=" + username + "]";
	}
	
}
