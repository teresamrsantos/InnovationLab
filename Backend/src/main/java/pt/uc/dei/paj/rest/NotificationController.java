package pt.uc.dei.paj.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pt.uc.dei.paj.DTO.NotificationDTO;
import pt.uc.dei.paj.entity.Notification;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.general.UserType;
import pt.uc.dei.paj.service.NotificationBean;
import pt.uc.dei.paj.service.UserBean;

@Path("/notifController")
public class NotificationController {
	private static final Logger logger = LogManager.getLogger(NotificationController.class);
	@Context
	private javax.servlet.http.HttpServletRequest hsr;
	@Inject
	private UserBean servUser;
	@Inject
	private NotificationBean notifBean;

	/******************** Get all Users Notifications **********************/
	@GET
	@Path("allUserNotifications/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUserNotifications(@PathParam("id") String id, @HeaderParam("token") String token) {

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {

				if (servUser.findUserByID(Integer.parseInt(id)) != null) {
					List<NotificationDTO> listAllNotifications = notifBean
							.findAllNotificationsFromUser(servUser.findUserByID(Integer.parseInt(id)));
					if (listAllNotifications != null) {
						// the number list will be reversed using this method
		
						logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
						return Response.ok(listAllNotifications).build();
					}
				}
			}
		}
		return Response.status(401).build();
	}

	/***************************** Soft Delete Notification *****************************/
	@POST
	@Path("/deleteNotification/{idNotification}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteNotification(@HeaderParam("token") String token,
			@PathParam("idNotification") String idNotification) {

		Notification notifToDelete = notifBean.findNotification(Integer.parseInt(idNotification));
		if (token != null && !token.isEmpty() && notifToDelete != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null || !notifToDelete.getUserNotified().getEmail().equals(userAux.getEmail())
					&& userAux.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			} else {
				if (notifBean.deleteNotification(notifToDelete)) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.status(200).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/****************************** GET NUMBER OF NOTIFICATIONS NOT READ ******************************/

	@GET
	@Path("/countNotificationsToRead")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response countNotificationsToRead(@HeaderParam("token") String token) {

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				Long num = notifBean.countNotificationsToRead(userAux.getEmail());
				logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
				return Response.ok(num).build();
			}
		}

		return Response.status(401).build();
	}

	/****************************** Mark Notification as Read ******************************/
	@POST
	@Path("/notificationRead/{idNotification}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response readNotification(@HeaderParam("token") String token,
			@PathParam("idNotification") String idNotification) {

		Notification notifToRead = notifBean.findNotification(Integer.parseInt(idNotification));
		if (token != null && !token.isEmpty() && notifToRead != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null || !notifToRead.getUserNotified().getEmail().equals(userAux.getEmail())
					&& userAux.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			} else {
				if (notifBean.readNotification(notifToRead)) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.status(200).build();
				}
			}
		}
		return Response.status(401).build();
	}
}
