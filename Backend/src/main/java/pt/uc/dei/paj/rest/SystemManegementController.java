package pt.uc.dei.paj.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pt.uc.dei.paj.DTO.SystemManegementDTO;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.general.UserType;
import pt.uc.dei.paj.service.SystemManegementBean;
import pt.uc.dei.paj.service.UserBean;

@Path("/system")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SystemManegementController {
	private static final Logger logger = LogManager.getLogger(PostController.class);
	@Context
	private javax.servlet.http.HttpServletRequest hsr;
	@Inject
	private UserBean servUser;
	@Inject
	private SystemManegementBean systemManegementBean;

	/******************** EDIT SESSION TIMEOUT ****************************/
	@POST
	@Path("/timeout")
	public Response editSessionTimeOut(@HeaderParam("token") String token, SystemManegementDTO systemManegementDTO) {
		if (token != null && !token.isEmpty() && systemManegementDTO != null) {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null || user.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			} else if (systemManegementBean.editSessionTimeOut(systemManegementDTO)) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok().build();
			}
		}
		return Response.status(401).build();
	}

	/******************** GET SESSION TIMEOUT ****************************/
	@GET
	@Path("/timeout")
	public Response getSessionTimeOut(@HeaderParam("token") String token) {
		if (token != null && !token.isEmpty()) {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null ) {
				return Response.status(403).build();
			} else {
				SystemManegementDTO systemManegementDTO = systemManegementBean.getSessionTimeOut();
				if (systemManegementDTO != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(systemManegementDTO).build();
				}
			}
		}
		return Response.status(401).build();
	}
}
