package pt.uc.dei.paj.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import pt.uc.dei.paj.DAO.NotificationDao;
import pt.uc.dei.paj.DAO.UserDao;
import pt.uc.dei.paj.DTO.NotificationDTO;
import pt.uc.dei.paj.entity.Notification;
import pt.uc.dei.paj.entity.Participation;
import pt.uc.dei.paj.entity.Project;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.general.NotificationType;
import pt.uc.dei.paj.general.Role;

@RequestScoped
public class NotificationBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@Inject
	NotificationDao notifDao;
	@Inject
	UserDao userDao;

	public Notification findNotification(int idNotification) {
		try {
			return notifDao.find(idNotification);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Method that find all notifications from a user and returns a dto list.
	 * 
	 * @param user
	 * @return list of NotificationDTO
	 */
	public ArrayList<NotificationDTO> findAllNotificationsFromUser(User user) {
		try {
			ArrayList<NotificationDTO> listToReturn = new ArrayList<NotificationDTO>();
			List<Notification> notificationList = notifDao.findAllNotificationFromUser(user.getEmail());
			if (notificationList != null && !notificationList.isEmpty()) {
				listToReturn = notifDao.convertEntityListToDTOList(notificationList);
			}
			return listToReturn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Method that creates notification
	 * 
	 * @param user
	 * @param project
	 * @return boolean (operation success)
	 */
	public boolean createNotificationN3(User userInvolved, Project projectInvolved) {
		try {
			for (Participation participation : projectInvolved.getMembersList()) {
				if (participation.getRole() == Role.ADMIN) {
					Notification notif = new Notification();
					notif.setNotificationType(NotificationType.N3_REQUESTTOBEPROJECTMEMBER);

					notif.setUsernameUserInvolved(userInvolved.getUsername());

					notif.setUserNotified(participation.getUserParticipation());
					notif.setProjectInvolvedID(projectInvolved.getIdProject());

					try {
						participation.getUserParticipation().getNotificationList().add(notif);
						userDao.merge(participation.getUserParticipation());
						notifDao.persist(notif);
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method that creates notification
	 * 
	 * @param notifType
	 * @param user
	 * @param project
	 * @return boolean (operation success)
	 */
	public boolean createNotificationN1N2(NotificationType notifType, User userToApprove, Project projectInvolved,
			String usernameUserWhoApproved) {
		try {
			Notification notif = new Notification();
			notif.setNotificationType(notifType);
			notif.setUsernameUserInvolved(usernameUserWhoApproved);
			notif.setUserNotified(userToApprove);
			notif.setProjectInvolvedID(projectInvolved.getIdProject());

			try {
				userToApprove.getNotificationList().add(notif);
				userDao.merge(userToApprove);
				notifDao.persist(notif);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * Method that creates notification
	 * 
	 * @param user
	 * @param project
	 * @return boolean (operation success)
	 */
	public boolean createNotificationN4(User userInvited, Project projectInvolved, User userWhoInvited) {

		Notification notif = new Notification();
		notif.setNotificationType(NotificationType.N4_INVITEUSERTOBEINPROJECT);
		notif.setUsernameUserInvolved(userWhoInvited.getUsername());
		notif.setUserNotified(userInvited);
		notif.setProjectInvolvedID(projectInvolved.getIdProject());

		try {
			userInvited.getNotificationList().add(notif);
			userDao.merge(userInvited);
			notifDao.persist(notif);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method that creates notification
	 * 
	 * @param notifType
	 * @param user
	 * @param project
	 * @return boolean (operation success)
	 */
	public boolean createNotificationN5N6(NotificationType notifType, String emailUserInvolved, Project projectInvolved) {

		try {
			for (Participation participation : projectInvolved.getMembersList()) {

				if (participation.getRole() == Role.ADMIN) {
					Notification notif = new Notification();
					notif.setNotificationType(notifType);
					notif.setUsernameUserInvolved(emailUserInvolved);
					notif.setUserNotified(participation.getUserParticipation());
					notif.setProjectInvolvedID(projectInvolved.getIdProject());

					try {
						participation.getUserParticipation().getNotificationList().add(notif);
						userDao.merge(projectInvolved.getUserJoinProject());
						notifDao.persist(notif);
						return true;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * Method to delete a notification or restore it
	 * @param notifAux
	 * @return boolean (operation success)
	 */
	public boolean deleteNotification(Notification notifAux) {
		try {
			if (notifAux.isDeletedNotification()) {
				notifAux.setDeletedNotification(false);
			} else {
				notifAux.setDeletedNotification(true);
			}
			notifDao.merge(notifAux);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Method to read a notification or unreads it
	 * @param notifAux
	 * @return boolean (operation success)
	 */
	public boolean readNotification(Notification notifAux) {
		try {
			if (notifAux.isReadNotification()) {
				notifAux.setReadNotification(false);
			} else {
				notifAux.setReadNotification(true);
			}
			notifDao.merge(notifAux);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Method that count notifications to read
	 * @param email
	 * @return number notifications to read
	 */
	public Long countNotificationsToRead(String email) {
		List<Long> listNotif = notifDao.countNotificationsToRead(email);
		if (listNotif != null && listNotif.get(0) != null) {
			return listNotif.get(0);
		}
		return (long) 0;
	}
}
