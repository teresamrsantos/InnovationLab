package pt.uc.dei.paj.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import pt.uc.dei.paj.DTO.NotificationDTO;
import pt.uc.dei.paj.entity.Notification;

@Stateless
public class NotificationDao extends AbstractDao<Notification> {

	@Inject
	ProjectDao projectDao;
	@Inject
	UserDao userDao;
	private static final long serialVersionUID = 1L;

	public NotificationDao() {
		super(Notification.class);
	}

	public ArrayList<NotificationDTO> convertEntityListToDTOList(List<Notification> notificationList) {
		ArrayList<NotificationDTO> listNotifDTO = new ArrayList<NotificationDTO>();
		for (Notification notification : notificationList) {
			listNotifDTO.add(convertEntityToDTO(notification));
		}
		return listNotifDTO;
	}

	public NotificationDTO convertEntityToDTO(Notification notifEntity) {
		NotificationDTO notifDTO = new NotificationDTO();

		switch (notifEntity.getNotificationType()) {

		case N1_APPROVEPROJECTMEMBER:
			notifDTO.setDescription("The user: " + notifEntity.getUsernameUserInvolved()
					+ ", has accepted your request to be part of the project "
					+ projectDao.find(notifEntity.getProjectInvolvedID()).getTitle());
			break;
		case N2_REJECTPROJECTMEMBER:
			notifDTO.setDescription("The user: " + notifEntity.getUsernameUserInvolved()
					+ ", has rejected your request to be part of the project "
					+ projectDao.find(notifEntity.getProjectInvolvedID()).getTitle());
			break;

		case N3_REQUESTTOBEPROJECTMEMBER:
			notifDTO.setDescription(
					"The user: " + notifEntity.getUsernameUserInvolved() + ", has requested to be part of your project "
							+ projectDao.find(notifEntity.getProjectInvolvedID()).getTitle());
			break;

		case N4_INVITEUSERTOBEINPROJECT:
			notifDTO.setDescription("The user: " + notifEntity.getUsernameUserInvolved()
					+ ", has requested you to be part of their project "
					+ projectDao.find(notifEntity.getProjectInvolvedID()).getTitle());
			break;

		case N5_ACCEPTUSERINVITATION:
			notifDTO.setDescription("The user: " + notifEntity.getUsernameUserInvolved()
					+ ", has accepted the invite to be part of the project "
					+ projectDao.find(notifEntity.getProjectInvolvedID()).getTitle());
			break;
		case N6_DECLINEUSERINVITATION:
			notifDTO.setDescription("The user: " + notifEntity.getUsernameUserInvolved()
					+ ", has declined the invite to be part of the project "
					+ projectDao.find(notifEntity.getProjectInvolvedID()).getTitle());
			break;

		default:
			return null;

		}
		
		notifDTO.setIdNotif(notifEntity.getIdNotification());
		notifDTO.setIdProject(notifEntity.getProjectInvolvedID());
		notifDTO.setIdUserThatNotificationIsAbout(
				(userDao.findUserByUsername(notifEntity.getUsernameUserInvolved())).get(0).getIdUser());
		notifDTO.setNotificationDate(notifEntity.getNotificationTime());
		notifDTO.setDeleted(notifEntity.isDeletedNotification());
		notifDTO.setRead(notifEntity.isReadNotification());
		return notifDTO;
	}

	public List<Notification> findAllNotificationFromUser(String email) {
		List<Notification> resultList = em.createNamedQuery("Notification.findAllNotificationsFromUser")
				.setParameter("email", email).getResultList();
		return resultList;
	}

	public List<Long> countNotificationsToRead(String email) {
		List<Long> resultList = em.createNamedQuery("Notification.countNotificationsToRead")
				.setParameter("email", email).getResultList();
		return resultList;
	}

}
