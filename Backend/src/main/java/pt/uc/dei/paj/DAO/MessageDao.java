package pt.uc.dei.paj.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import pt.uc.dei.paj.DTO.MessageDTO;
import pt.uc.dei.paj.entity.Message;
import pt.uc.dei.paj.entity.User;

@Stateless
public class MessageDao extends AbstractDao<Message> {
	private static final long serialVersionUID = 1L;

	public MessageDao() {
		super(Message.class);
	}

	/**
	 * Method that converts Message of the type DTO to Message of the type Entity
	 * @param messageDto
	 * @param userSender
	 * @param userReceiver
	 * @return Message
	 */
	public Message convertDtoToEntity(MessageDTO messageDto, User userSender, User userReceiver) {
		Message message = new Message();
		if (messageDto.getMessage() != null && !messageDto.getMessage().isEmpty()) {
			message.setMessage(messageDto.getMessage());
		}
		if (userSender != null) {
			message.setUserSender(userSender);
		}
		if (userReceiver != null) {
			message.setUserReceiver(userReceiver);
		}
		return message;
	}

	/**
	 * Method that converts Message of the type DTO to Message of the type Entity
	 * @param message
	 * @return MessageDTO
	 */
	public MessageDTO convertEntityToDto(Message message) {
		MessageDTO messageDto = new MessageDTO();
		messageDto.setIdMessage(message.getIdMessage());
		messageDto.setMessage(message.getMessage());
		messageDto.setMessageTime(message.getMessageTime());
		messageDto.setUserReceiver(message.getUserReceiver().getIdUser());
		messageDto.setUserSender(message.getUserSender().getIdUser());
		messageDto.setDeletedMessage(message.isDeletedMessage());
		messageDto.setReadMessage(message.isReadMessage());
		messageDto.setNameSender(message.getUserSender().getFirstName()+" "+message.getUserSender().getLastName());
		messageDto.setNameReceiver(message.getUserReceiver().getFirstName()+" "+message.getUserReceiver().getLastName());
		return messageDto;
	}

	/**
	 * Method that converts a Message list of the type Entity to Message list of the type DTO
	 * @param messageList
	 * @return MessageDTO List
	 */
	public List<MessageDTO> convertEntityToDto(List<Message> messageList) {
		List<MessageDTO> messageDtoList = new ArrayList<MessageDTO>();
		for (Message message : messageList) {
			messageDtoList.add(convertEntityToDto(message));
		}
		return messageDtoList;
	}
	
	/**
	 * Method that finds all consersations that user is userSender
	 * @param email
	 * @return email List
	 */
	public List<Integer> findConversationSender(String email) {
		List<Integer> resultList = em.createNamedQuery("Message.findConversationSender").setParameter("email", email)
				.getResultList();
		return resultList;
	}
	
	/**
	 * Method that finds all consersations that user is userReceiver
	 * @param email
	 * @return email List
	 */
	public List<Integer> findConversationReceiver(String email) {
		List<Integer> resultList = em.createNamedQuery("Message.findConversationReceiver").setParameter("email", email)
				.getResultList();
		return resultList;
	}
	
	/**
	 * Method that finds the date of the last message between two users
	 * @param email1
	 * @param email2
	 * @return date List
	 */
	public List<Date> findLastMessageUser(String email1, int idUser2) {
		List<Date> resultList = em.createNamedQuery("Message.findLastMessageUser")
				.setParameter("email1", email1)
				.setParameter("idUser2", idUser2)
				.getResultList();
		return resultList;
	}
	
	/**
	 * Method that finds the number messages to read of userReceiver between two users
	 * @param email1
	 * @param email2
	 * @return number messages to read List
	 */
	public List<Long> findMessageToRead(String email1, int idUser2) {
		List<Long> resultList = em.createNamedQuery("Message.findMessageToRead")
				.setParameter("email1", email1)
				.setParameter("idUser2", idUser2)
				.getResultList();
		return resultList;
	}
	
	/**
	 * Method that finds all messages of a conversation between two users
	 * @param email1
	 * @param email2
	 * @return Message List
	 */
	public List<Message> findConversationBetweenTwoUsers(int idUser1, int idUser2) {
		List<Message> resultList = em.createNamedQuery("Message.findConversationBetweenTwoUsers")
				.setParameter("idUser1", idUser1)
				.setParameter("idUser2", idUser2)
				.getResultList();
		return resultList;
	}
	
	/**
	 * Method that finds all messages to read between two users and user is receiver
	 * @param email1
	 * @param email2
	 * @return Message List
	 */
	public List<Message> findMessagesToReadBetweenTwoUsersAndUserIsReceiver(int idUser1, int idUser2) {
		List<Message> resultList = em.createNamedQuery("Message.findMessagesToReadBetweenTwoUsersAndUserIsReceiver")
				.setParameter("idUser1", idUser1)
				.setParameter("idUser2", idUser2)
				.getResultList();
		return resultList;
	}
	
	/**
	 * Method that counts all messages to read of a user
	 * @param email
	 * @return Message List
	 */
	public List<Long> countMessagesToRead(String email) {
		List<Long> resultList = em.createNamedQuery("Message.countMessagesToRead")
				.setParameter("email", email)		
				.getResultList();
		return resultList;
	}
}
