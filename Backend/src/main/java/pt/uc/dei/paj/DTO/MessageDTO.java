package pt.uc.dei.paj.DTO;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private int idMessage;
	private Date messageTime;
	private String message;
	private boolean readMessage;
	private boolean deletedMessage;
	private int userReceiver;
	private int userSender;
	private String nameSender;
	private String nameReceiver;

	public MessageDTO() {
	}

	public int getIdMessage() {
		return idMessage;
	}

	public void setIdMessage(int idMessage) {
		this.idMessage = idMessage;
	}

	public Date getMessageTime() {
		return messageTime;
	}

	public void setMessageTime(Date messageTime) {
		this.messageTime = messageTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isReadMessage() {
		return readMessage;
	}

	public void setReadMessage(boolean readMessage) {
		this.readMessage = readMessage;
	}

	public boolean isDeletedMessage() {
		return deletedMessage;
	}

	public void setDeletedMessage(boolean deletedMessage) {
		this.deletedMessage = deletedMessage;
	}

	public int getUserReceiver() {
		return userReceiver;
	}

	public void setUserReceiver(int userReceiver) {
		this.userReceiver = userReceiver;
	}

	public int getUserSender() {
		return userSender;
	}

	public void setUserSender(int userSender) {
		this.userSender = userSender;
	}

	public String getNameSender() {
		return nameSender;
	}

	public void setNameSender(String nameSender) {
		this.nameSender = nameSender;
	}

	public String getNameReceiver() {
		return nameReceiver;
	}

	public void setNameReceiver(String nameReceiver) {
		this.nameReceiver = nameReceiver;
	}

	@Override
	public String toString() {
		return "MessageDTO [idMessage=" + idMessage + ", messageTime=" + messageTime + ", message=" + message
				+ ", readMessage=" + readMessage + ", deletedMessage=" + deletedMessage + ", userReceiver="
				+ userReceiver + ", userSender=" + userSender + ", nameSender=" + nameSender + ", nameReceiver="
				+ nameReceiver + "]";
	}

}
