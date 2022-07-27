package pt.uc.dei.paj.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@NamedQueries({
		@NamedQuery(name = "Message.findConversationSender", query = "SELECT DISTINCT m.userReceiver.idUser FROM Message m WHERE m.userSender.email=:email"),
		@NamedQuery(name = "Message.findConversationReceiver", query = "SELECT DISTINCT m.userSender.idUser FROM Message m WHERE m.userReceiver.email=:email"),
		@NamedQuery(name = "Message.findLastMessageUser", query = "SELECT MAX(messageTime) FROM Message m WHERE m.userReceiver.email=:email1 AND m.userSender.idUser=:idUser2 OR m.userReceiver.idUser=:idUser2 AND m.userSender.email=:email1"),
		@NamedQuery(name = "Message.findMessageToRead", query = "SELECT COUNT(m) FROM Message m WHERE m.readMessage=false AND m.userReceiver.email=:email1 AND m.userSender.idUser=:idUser2"),
		@NamedQuery(name = "Message.findConversationBetweenTwoUsers", query = "SELECT m FROM Message m WHERE m.userReceiver.idUser=:idUser1 AND m.userSender.idUser=:idUser2 OR m.userReceiver.idUser=:idUser2 AND m.userSender.idUser=:idUser1 order by m.messageTime ASC"),
		@NamedQuery(name = "Message.findMessagesToReadBetweenTwoUsersAndUserIsReceiver", query = "SELECT m FROM Message m WHERE m.userReceiver.idUser=:idUser1 AND m.userSender.idUser=:idUser2 AND m.readMessage=false"),
		@NamedQuery(name = "Message.countMessagesToRead", query = "SELECT COUNT(m) FROM Message m WHERE m.readMessage=false AND m.userReceiver.email=:email"),

})
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idMessage;

	@CreationTimestamp
	@Column(name = "MessageTime", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date messageTime;

	@Column(name = "MessageContent", nullable = false)
	private String message;

	@Column(name = "readMessage", nullable = false)
	private boolean readMessage;

	@Column(name = "deletedMessage", nullable = false)
	private boolean deletedMessage;

	@ManyToOne
	private User userReceiver;

	@ManyToOne
	private User userSender;

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

	public User getUserReceiver() {
		return userReceiver;
	}

	public void setUserReceiver(User userReceiver) {
		this.userReceiver = userReceiver;
	}

	public User getUserSender() {
		return userSender;
	}

	public void setUserSender(User userSender) {
		this.userSender = userSender;
	}

	@Override
	public String toString() {
		return "Message [idMessage=" + idMessage + ", messageTime=" + messageTime + ", message=" + message
				+ ", readMessage=" + readMessage + ", deletedMessage=" + deletedMessage + ", userReceiver="
				+ userReceiver + ", userSender=" + userSender + "]";
	}

}
