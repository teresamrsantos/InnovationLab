package pt.uc.dei.paj.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.json.JSONObject;

import pt.uc.dei.paj.DAO.MessageDao;
import pt.uc.dei.paj.DAO.UserDao;
import pt.uc.dei.paj.DTO.InformationDTO;
import pt.uc.dei.paj.DTO.MessageDTO;
import pt.uc.dei.paj.DTO.UserDTO;
import pt.uc.dei.paj.entity.Message;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.general.SaveFile;

@RequestScoped
public class MessageBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	UserDao userDao;

	@Inject
	MessageDao messageDao;

	/**
	 * Method that creates a message from one user to another
	 * 
	 * @param messageDTO
	 * @param userSender
	 * @return operation success
	 */
	public boolean sendMessageToUser(MessageDTO messageDTO, User userSender) {
		try {
			User userReceiver = userDao.find(messageDTO.getUserReceiver());
			Message message = messageDao.convertDtoToEntity(messageDTO, userSender, userReceiver);
			messageDao.persist(message);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Method that mark all Messages between two Users as read to userReceiver
	 * 
	 * @param email1 userReceiver
	 * @param email2 userSender
	 * @return operation success
	 */
	public boolean markAllMessagesBetweenTwoUsersAsReadTouserReceiver(int idUser1, int idUser2) {

		try {
			List<Message> messageList = messageDao.findMessagesToReadBetweenTwoUsersAndUserIsReceiver(idUser1, idUser2);
			if (messageList != null && !messageList.isEmpty()) {
				for (Message message : messageList) {
					message.setReadMessage(true);
					messageDao.merge(message);
				}
				return true;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Method that returns Information about all conversation of the userReceiver
	 * 
	 * @param email
	 * @return InformationDTO List
	 */
	public List<InformationDTO> findInformationFromOfTheConversationsOfAUser(String email) {
		try {
			List<Integer> allUsers = new ArrayList<Integer>();
			List<Integer> receiverList = messageDao.findConversationReceiver(email);
			List<Integer> senderList = messageDao.findConversationSender(email);
			allUsers.addAll(receiverList);
			allUsers.addAll(senderList);
			allUsers = removeDuplicates(allUsers);

			List<InformationDTO> informationDTOList = new ArrayList<InformationDTO>();
			for (int idConversation : allUsers) {
				InformationDTO informationDTO = new InformationDTO();
				informationDTO.setIdUser(idConversation);
				informationDTO.setDateLastMessage(messageDao.findLastMessageUser(email, idConversation).get(0));
				informationDTO.setMessageToRead(messageDao.findMessageToRead(email, idConversation).get(0));
				User userReceiver = userDao.find(idConversation);
				informationDTO.setName(userReceiver.getFirstName() + " " + userReceiver.getLastName());
				
				if(userReceiver.getPictureUrl()!=null) {
				informationDTO.setPhoto(SaveFile.convertFileToFrontEnd(userReceiver.getPictureUrl()));}
				informationDTO.setUsername(userReceiver.getUsername());
				informationDTOList.add(informationDTO);
			}
			Collections.sort(informationDTOList, new RegisterDateComparator());
			return informationDTOList;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that returns all messages between two users
	 * 
	 * @param email1
	 * @param email2
	 * @return MessageDTO List
	 */
	public List<MessageDTO> findConversationBetweenTwoUsers(int idUser1, int idUser2) {
		try {
			List<Message> messageList = messageDao.findConversationBetweenTwoUsers(idUser1, idUser2);
			if (messageList != null && !messageList.isEmpty()) {
				return messageDao.convertEntityToDto(messageList);
			}
			return new ArrayList<MessageDTO>();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that remove duplicate a list
	 * 
	 * @param <T>
	 * @param list
	 * @return List without duplicates
	 */
	public static <T> List<T> removeDuplicates(List<T> list) {
		List<T> newList = new ArrayList<T>();
		for (T element : list) {
			if (!newList.contains(element)) {
				newList.add(element);
			}
		}
		return newList;
	}

	/**
	 * Method to count Unread Messages
	 * 
	 * @param email
	 * 
	 * @return MessageDTO List
	 */
	public Long countUnreadMessages(String email) {
		try {
			List<Long> countUnreadMessages = messageDao.countMessagesToRead(email);
			if (countUnreadMessages != null && countUnreadMessages.get(0) != null) {
				return countUnreadMessages.get(0);
			}

		} catch (Exception e) {

			return (long) 0;
		}
		return (long) 0;
	}
	
	public List<UserDTO> listUsersToTalk(String email){
		try {
		List<User> userList = userDao.findAllUserToTalk(email);
		if(!userList.isEmpty()&&userList.size()>0) {
			return userDao.convertEntityListtoDTOList(userList);
		}
		return new ArrayList<UserDTO>(); 
		}catch (Exception e) {
			return null;
		}
	}
	
	public JSONObject infoUserConversation(int idUser){
		try {
			User user = userDao.find(idUser);
		if(user!=null) {			
			JSONObject json = new JSONObject();
			json.put("name", user.getFirstName()+" "+user.getLastName());
			if(user.getPictureUrl()!=null) {
			json.put("photo", SaveFile.convertFileToFrontEnd(user.getPictureUrl()));}
			return json;
		}
		return new JSONObject();
		}catch (Exception e) {
			return null;
		}
	}

	class RegisterDateComparator implements Comparator<InformationDTO> {
		/**
		 * Method that compare and sort a list of the InformationDTO type
		 */
		public int compare(InformationDTO s1, InformationDTO s2) {
			if (s1.getDateLastMessage() == s2.getDateLastMessage())
				return 0;
			else if (s1.getDateLastMessage().after(s2.getDateLastMessage()))
				return -1;
			else
				return 1;
		}
	}
}