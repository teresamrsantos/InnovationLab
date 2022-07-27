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
import org.json.JSONObject;

import pt.uc.dei.paj.DTO.InformationDTO;
import pt.uc.dei.paj.DTO.MessageDTO;
import pt.uc.dei.paj.DTO.UserDTO;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.service.MessageBean;
import pt.uc.dei.paj.service.UserBean;

@Path("/message")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessageController {
	private static final Logger logger = LogManager.getLogger(MessageController.class);
	@Context
	private javax.servlet.http.HttpServletRequest hsr;
	@Inject
	private UserBean servUser;
	@Inject
	private MessageBean messageBean;

	/******************** SEND MESSAGE ****************************/
	@POST
	@Path("/send")
	public Response sendMessage(@HeaderParam("token") String token, MessageDTO messageDTO) {
		if (token == null || token.isEmpty() || messageDTO == null || messageDTO.getMessage().isEmpty()
				|| messageDTO.getMessage() == null) {
			return Response.status(401).build();
		}

		User user = servUser.checkIfTokenIsValid(token);
		if (user == null) {
			return Response.status(403).build();
		} else if (messageBean.sendMessageToUser(messageDTO, user)) {
			logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
			return Response.ok().build();
		}

		return Response.status(401).build();
	}

	/******************** MARK ALL MESSAGES BETWEEN TWO USERS AS READ TO USER RECEIVER ****************************/
	@POST
	@Path("/read/{idUser}")
	public Response markAllMessagesBetweenTwoUsersAsReadTouserReceiver(@HeaderParam("token") String token,
			@PathParam("idUser") String idUser) {
		if (token == null || token.isEmpty() || idUser == null || idUser.isEmpty()) {
			return Response.status(401).build();
		}

		User user = servUser.checkIfTokenIsValid(token);
		if (user == null) {
			return Response.status(403).build();
		} else if (messageBean.markAllMessagesBetweenTwoUsersAsReadTouserReceiver(user.getIdUser(), Integer.parseInt(idUser))) {
			logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
			return Response.ok().build();
		}
		return Response.status(401).build();
	}

	/******************** FIND INFORMATION FROM OF THE CONVERSATION OF USER ****************************/
	@GET
	@Path("/conversation")
	public Response findInformationFromOfTheConversationsOfAUser(@HeaderParam("token") String token) {
		if (token != null && !token.isEmpty()) {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else {
				List<InformationDTO> informationList = messageBean
						.findInformationFromOfTheConversationsOfAUser(user.getEmail());
				if (informationList != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(informationList).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** FIND CONVERSATION BETWEEN TWO USERS ****************************/
	@GET
	@Path("/conversation/{idUser}")
	public Response findConversationBetweenTwoUsers(@HeaderParam("token") String token,
			@PathParam("idUser") String idUser) {
		if (token != null && !token.isEmpty() && idUser != null && !idUser.isEmpty()) {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else {
				List<MessageDTO> messageList = messageBean.findConversationBetweenTwoUsers(user.getIdUser(), Integer.parseInt(idUser));
				if (messageList != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(messageList).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** COUNT UNREAD MESSAGES ****************************/
	@GET
	@Path("/countUnreadMessages")
	public Response countUnreadMessages(@HeaderParam("token") String token) {
		if (token != null && !token.isEmpty()) {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else {
				Long messageList = messageBean.countUnreadMessages(user.getEmail());
				if (messageList != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(messageList).build();
				}
			}
		}
		return Response.status(401).build();
	}
	
	/******************** ALL USERS TO TALK ****************************/
	@GET
	@Path("/users")
	public Response allUsersToTalk(@HeaderParam("token") String token) {
		if (token != null && !token.isEmpty()) {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else {
				List<UserDTO> userList = messageBean.listUsersToTalk(user.getEmail());
				if (userList != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(userList).build();
				}
			}
		}
		return Response.status(401).build();
	}
	
	/******************** INFORMATION OF THE USER CONVERSATION ****************************/
	@GET
	@Path("/userConversation/{idUser}")
	public Response infoUserConversation(@HeaderParam("token") String token,@PathParam("idUser") String idUser) {
		if (token != null && !token.isEmpty() && idUser!=null &&!idUser.isEmpty()) {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else {
				JSONObject infoUser = messageBean.infoUserConversation(Integer.parseInt(idUser));
				if (infoUser != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(infoUser.toString()).build();
				}
			}
		}
		return Response.status(401).build();
	}
}
