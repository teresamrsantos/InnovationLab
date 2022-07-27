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

import pt.uc.dei.paj.DTO.ParticipationDTO;
import pt.uc.dei.paj.DTO.ProjectDTO;
import pt.uc.dei.paj.DTO.UserDTO;
import pt.uc.dei.paj.entity.Project;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.service.ProjectBean;
import pt.uc.dei.paj.service.UserBean;

@Path("/participationController")
public class ParticipationController {
	private static final Logger logger = LogManager.getLogger(ParticipationController.class);
	@Context
	private javax.servlet.http.HttpServletRequest hsr;
	@Inject
	private UserBean servUser;
	@Inject
	private ProjectBean projBean;

	/**************************************** Get all Invited Pending answer from User ****************************************/
	@GET
	@Path("/invitesPending/{email}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllProjectsInvitesPending(@HeaderParam("token") String token,
			@PathParam("email") String email) {

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				List<ProjectDTO> listProjects = projBean.findAllProjectsWithPendingInvites(email);
				if (listProjects != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listProjects).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/**************************************** Get all Requests Pending answer from A Project ****************************************/
	@GET
	@Path("/requestsPending/{idProject}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllProjectsRequestsPending(@HeaderParam("token") String token,
			@PathParam("idProject") String idProject) {
		Project projectBeingAnalized = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				List<UserDTO> listUsers = projBean
						.findAllUsersWithPendingRequestsToAProjectPending(Integer.parseInt(idProject));
				if (listUsers != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listUsers).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/***************************************
	 * Get all Requests Pending answer from A Project *
	 ***************************************/
	@GET
	@Path("/invitesPendingProject/{idProject}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response invitesPendingProject(@HeaderParam("token") String token,
			@PathParam("idProject") String idProject) {
		Project projectBeingAnalized = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				List<UserDTO> listUsers = projBean
						.findAllUsersWithPendingInvitesToAProjectPending(Integer.parseInt(idProject));
				if (listUsers != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listUsers).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/**************************************** Get all Projects that User is member ****************************************/

	@GET
	@Path("/projectUserMember/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllProjectsWhereUserIsMember(@HeaderParam("token") String token, @PathParam("id") String id) {

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				User user = servUser.findUserByID(Integer.parseInt(id));
				if (user != null) {
					List<ProjectDTO> listProjects = projBean.findAllProjectsWhereUserIsMember(user.getEmail());
					if (listProjects != null) {
						logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
						return Response.ok(listProjects).build();
					}
				}
			}
		}
		return Response.status(401).build();
	}

	/**************************************** delete participation ****************************************/

	@POST
	@Path("/deleteParticipation/{idProject}/{idUser}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteParticipation(@HeaderParam("token") String token, @PathParam("idProject") String idProject,
			@PathParam("idUser") String idUser) {

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				User user = servUser.findUserByID(Integer.parseInt(idUser));
				if (user != null) {
					if (projBean.deleteParticipationFromUser(Integer.parseInt(idProject), user.getEmail())) {
						logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
						return Response.ok().build();
					}
				}
			}
		}
		return Response.status(401).build();
	}
	
	/**************************************** delete participation ****************************************/

	@POST
	@Path("/changeParticipation/{idProject}/{idUser}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeParticipation(@HeaderParam("token") String token, @PathParam("idProject") String idProject,
			@PathParam("idUser") String idUser) {

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				User user = servUser.findUserByID(Integer.parseInt(idUser));
				if (user != null) {
					if (projBean.changeParticipationFromUser(Integer.parseInt(idProject), user.getEmail())) {
						logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
						return Response.ok().build();
					}
				}
			}
		}
		return Response.status(401).build();
	}
	
	
	

	/**************************************** Get all Users that are member of a project ****************************************/

	@GET
	@Path("/usersMember/{idProject}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllMembersOfProject(@HeaderParam("token") String token,
			@PathParam("idProject") String idProject) {

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				List<ParticipationDTO> listUsers = projBean.findAllMembersOfProject(Integer.parseInt(idProject));
				if (listUsers != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listUsers).build();
				}
			}
		}
		return Response.status(401).build();
	}

}
