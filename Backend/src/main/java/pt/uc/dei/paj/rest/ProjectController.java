package pt.uc.dei.paj.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.uc.dei.paj.DTO.IdeaNecessityDTO;
import pt.uc.dei.paj.DTO.InterestDTO;
import pt.uc.dei.paj.DTO.ParticipationDTO;
import pt.uc.dei.paj.DTO.ProjectDTO;
import pt.uc.dei.paj.DTO.SkillDTO;
import pt.uc.dei.paj.DTO.UserDTO;
import pt.uc.dei.paj.entity.Project;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.general.NotificationType;
import pt.uc.dei.paj.general.SaveFile;
import pt.uc.dei.paj.general.UserType;
import pt.uc.dei.paj.service.NotificationBean;
import pt.uc.dei.paj.service.ProjectBean;
import pt.uc.dei.paj.service.UserBean;

@Path("/projectController")
public class ProjectController {
	private static final Logger logger = LogManager.getLogger(ProjectController.class);
	@Context
	private javax.servlet.http.HttpServletRequest hsr;
	@Inject
	private UserBean servUser;
	@Inject
	private ProjectBean projBean;
	@Inject
	private NotificationBean notifBean;

	/**************************** add Project ****************************/
	@POST
	@Path("/addProject/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addProject(@HeaderParam("token") String token, MultipartFormDataInput input,
			@PathParam("id") String id, @QueryParam("idProjectIdeas") String idProjectIdeas) {

		List<InputPart> imgpart = input.getFormDataMap().get("image");
		List<InputPart> projectJson = input.getFormDataMap().get("projectJson");
		String stringnewProject = null;

		User userID = servUser.findUserByID(Integer.parseInt(id));
		if (token != null && !token.isEmpty() && projectJson != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userID == null || userAux == null
					|| userAux.getIdUser() != (Integer.parseInt(id)) && userAux.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();

			} else {
				InputStream is2 = null;
				ProjectDTO projectDTO = null;
				try {
					stringnewProject = input.getFormDataPart("projectJson", String.class, null);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				ObjectMapper m = new ObjectMapper();
				try {
					projectDTO = m.readValue(stringnewProject, ProjectDTO.class);
				} catch (JsonMappingException e1) {
					e1.printStackTrace();
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}

				String picturePath = null;

				String UPLOAD_FOLDER = "\\Projects\\";
				if (imgpart != null && !imgpart.isEmpty()) {

					try {
						is2 = input.getFormDataPart("image", InputStream.class, null);
						if (is2 != null) {

							picturePath = SaveFile.saveToFile(
									projectDTO.getTitle().replaceAll("\\s+", "") + System.currentTimeMillis(), is2,
									UPLOAD_FOLDER);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (picturePath != null) {
					projectDTO.setImageProject(picturePath);
				}

				if (projectDTO != null && idProjectIdeas != null) {
					if (projBean.addProject(projectDTO, userAux, idProjectIdeas)) {
						logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
						return Response.ok().build();
					}
				}
			}
		}
		return Response.status(401).build();
	}

	/**************************** edit Project ****************************/

	@POST
	@Path("/editProject/{idProject}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response editProject(@HeaderParam("token") String token, MultipartFormDataInput input,
			@PathParam("idProject") String idProject) {

		Project projectToEdit = projBean.findProjectById(Integer.parseInt(idProject));
		if (token != null && !token.isEmpty() && projectToEdit != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {

				List<InputPart> imgpart = input.getFormDataMap().get("image");
				List<InputPart> projectJSON = input.getFormDataMap().get("projectJSON");

				if (projectJSON != null) {
					String stringnewProjectDTO = null;

					InputStream is2 = null;
					ProjectDTO newProjectDTO = null;
					try {

						stringnewProjectDTO = input.getFormDataPart("projectJSON", String.class, null);

						ObjectMapper m = new ObjectMapper();
						newProjectDTO = m.readValue(stringnewProjectDTO, ProjectDTO.class);

					} catch (IOException e) {
						// e.printStackTrace();
					}
					String UPLOAD_FOLDER = "\\projects\\";
					if (newProjectDTO != null) {
						if (imgpart != null) {
							try {
								is2 = input.getFormDataPart("image", InputStream.class, null);

							} catch (IOException e1) {
								e1.printStackTrace();
							}
							String picturePath = null;
							try {
								picturePath = SaveFile.saveToFile(
										newProjectDTO.getTitle().replaceAll("\\s+", "") + System.currentTimeMillis(),
										is2, UPLOAD_FOLDER);
							} catch (IOException e) {
								e.printStackTrace();
							}

							if (picturePath == null) {
								return Response.status(401).build();
							} else {

								newProjectDTO.setImageProject(picturePath);
							}
						} else {
							if (projectToEdit.getImageProject() != null) {
								newProjectDTO.setImageProject(projectToEdit.getImageProject());
							} else {
								newProjectDTO.setImageProject(null);
							}
						}

						if (projBean.editProject(newProjectDTO, projectToEdit)) {
							logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
							return Response.ok().build();
						}
					}
				}
			}
		}
		return Response.status(401).build();
	}

	/************************************* Invite Members to join a Project *************************************/
	@POST
	@Path("/{idProject}/inviteToJoinProject/{idUser}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response inviteMembersToJoinProject(@HeaderParam("token") String token,
			@PathParam("idProject") String idProject, @PathParam("idUser") String idUser) {
		Project projectToAssociateMembers = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));
		if (token != null && !token.isEmpty() && projectToAssociateMembers != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {

				User userToAddToProject = servUser.findUserByID(Integer.parseInt(idUser));
				if (userToAddToProject != null) {

					/*
					 * if (projBean.verifyIfTheUserIsParticipatingOnThProjectEvenIfAsInviteRequest(
					 * Integer.parseInt(idProject), userToAddToProject.getEmail())) { return
					 * Response.status(406).build(); }
					 */
					List<UserDTO> listToReturn = projBean.requestToJoinProject(projectToAssociateMembers,
							userToAddToProject, "INVITE");
					if (listToReturn != null
							&& notifBean.createNotificationN4(userToAddToProject, projectToAssociateMembers, userAux)) {
						logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
						return Response.ok(listToReturn).build();
					}
				}
			}

		}
		return Response.status(401).build();
	}

	/************************************************* REQUEST TO BE A PART OF A PROJECT *************************************************/
	@POST
	@Path("/{idProject}/requestToJoinProject")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response requestToJoinProject(@HeaderParam("token") String token, @PathParam("idProject") String idProject) {
		Project projectToAssociateMembers = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));
		if (token != null && !token.isEmpty() && projectToAssociateMembers != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				if (projBean.verifyIfTheUserIsParticipatingOnThProjectEvenIfAsInviteRequest(Integer.parseInt(idProject),
						userAux.getEmail())) {
					return Response.status(406).build();
				}
				List<UserDTO> listToReturn = projBean.requestToJoinProject(projectToAssociateMembers, userAux,
						"REQUEST");
				if (listToReturn != null && notifBean.createNotificationN3(userAux, projectToAssociateMembers)) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok().build();
				}
			}

		}
		return Response.status(401).build();
	}

	/************************************************* Accept Decline Invite to be a member Project *************************************************/
	@POST
	@Path("/acceptInviteToBePartOfProject/{idProject}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response acceptInviteToBePartOfProject(@HeaderParam("token") String token,
			@PathParam("idProject") String idProject, String acceptedOrNotS) {

		Project projectToAssociateMembers = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));
		if (acceptedOrNotS != null) {
			boolean acceptedOrNot = Boolean.parseBoolean(acceptedOrNotS);
			if (token != null && !token.isEmpty() && projectToAssociateMembers != null) {
				User userAux = servUser.checkIfTokenIsValid(token);
				if (userAux == null) {
					return Response.status(403).build();
				} else {
					NotificationType notif;
					if (acceptedOrNot) {
						notif = NotificationType.N5_ACCEPTUSERINVITATION;
					} else {
						notif = NotificationType.N6_DECLINEUSERINVITATION;
					}

					if (notifBean.createNotificationN5N6(notif, userAux.getUsername(), projectToAssociateMembers)
							&& projBean.acceptInviteOrNot(projectToAssociateMembers, userAux, acceptedOrNot)) {
						logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
						return Response.ok().build();
					}
				}
			}
		}
		return Response.status(401).build();
	}

	/************************************************* Aprove or Not Members to Project *************************************************/
	@POST
	@Path("/{idProject}/approveMemberToProject")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response approveMemberToProject(@HeaderParam("token") String token, @PathParam("idProject") String idProject,
			String approvedOrNotJson) {
		Project projectToAssociateMembers = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));
		JSONObject JOapprovedOrNot = new JSONObject(approvedOrNotJson);
		if (JOapprovedOrNot != null) {
			String idUser = JOapprovedOrNot.getString("idUser");
			boolean approvedOrNot = JOapprovedOrNot.getBoolean("approved");

			if (token != null && !token.isEmpty() && projectToAssociateMembers != null) {
				User userAux = servUser.checkIfTokenIsValid(token);
				if (userAux == null) {
					return Response.status(403).build();
				} else {
					NotificationType notif;
					if (approvedOrNot) {
						notif = NotificationType.N1_APPROVEPROJECTMEMBER;
					} else {
						notif = NotificationType.N2_REJECTPROJECTMEMBER;
					}
					User userToApprove = servUser.findUserByID(Integer.parseInt(idUser));

					if (userToApprove != null
							&& notifBean.createNotificationN1N2(notif, userToApprove, projectToAssociateMembers,
									userAux.getUsername())
							&& projBean.approveMemberOrNot(projectToAssociateMembers, userToApprove.getEmail(),
									approvedOrNot)) {
						logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
						return Response.ok().build();
					}
				}

			}
		}
		return Response.status(401).build();
	}

	/***************** Change Member Type Project *******************/
	@POST
	@Path("/{idProject}/changeMemberType")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeMemberType(@HeaderParam("token") String token, @PathParam("idProject") String idProject,
			ParticipationDTO participationDTO) {
		Project projectToAssociateMembers = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));
		if (token != null && !token.isEmpty() && projectToAssociateMembers != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null || !projectToAssociateMembers.getUserJoinProject().getEmail().equals(userAux.getEmail())
					&& userAux.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			} else {

				List<ParticipationDTO> listToReturn = projBean.changeMemberRole(projectToAssociateMembers,
						participationDTO);
				if (listToReturn != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listToReturn).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/***************** Disassociate Members to Project *******************/
	@POST
	@Path("/{idProject}/removeMemberProject/{idUser}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeMemberProject(@HeaderParam("token") String token, @PathParam("idProject") String idProject,
			@PathParam("idUser") String idUser) {
		Project projectToAssociateMembers = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));

		if (token != null && !token.isEmpty() && projectToAssociateMembers != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				User userRemove = servUser.findUserByID(Integer.parseInt(idUser));
				if (userRemove != null && projBean.removeMemberproject(userRemove, projectToAssociateMembers)) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok().build();
				}
			}

		}
		return Response.status(401).build();
	}

	/***************** Disassociate Members to Project *******************/
	@POST
	@Path("/{idProject}/removeMemberToProject/{idUser}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeMemberToProjectIdUser(@HeaderParam("token") String token,
			@PathParam("idProject") String idProject, @PathParam("idUser") String idUser) {
		Project projectToAssociateMembers = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));

		if (token != null && !token.isEmpty() && projectToAssociateMembers != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				List<ParticipationDTO> listToReturn = projBean.disassociateMemberFromproject(Integer.parseInt(idUser),
						projectToAssociateMembers);
				if (listToReturn != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listToReturn).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/***************** Add Skill to Project *******************/
	@POST
	@Path("/{idProject}/associateSkill")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response associateSkill(@HeaderParam("token") String token, @PathParam("idProject") String idProject,
			@QueryParam("idSkill") String idSkill) {
		Project projectToAssociateSkills = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));
		if (token != null && !token.isEmpty() && projectToAssociateSkills != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null || !userAux.getEmail().equals(projectToAssociateSkills.getUserJoinProject().getEmail())
					&& !userAux.getEmail().equals(projectToAssociateSkills.getUserJoinProject().getEmail())
					&& userAux.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			} else {
				List<SkillDTO> listSkills = projBean.associateSkillToProject(Integer.parseInt(idSkill),
						projectToAssociateSkills);
				if (listSkills != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listSkills).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/***************** Remove Skill to Project *******************/
	@POST
	@Path("/{idProject}/disassociateSkill")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response disassociateSkill(@HeaderParam("token") String token, @PathParam("idProject") String idProject,
			@QueryParam("idSkill") String idSkill) {
		Project projectToDisassociateSkill = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));
		if (token != null && !token.isEmpty() && projectToDisassociateSkill != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null
					|| !userAux.getEmail().equals(projectToDisassociateSkill.getUserJoinProject().getEmail())
							&& !userAux.getEmail().equals(projectToDisassociateSkill.getUserJoinProject().getEmail())
							&& userAux.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			} else {
				List<SkillDTO> listSkills = projBean.disassociateSkillToProject(Integer.parseInt(idSkill),
						projectToDisassociateSkill);
				if (listSkills != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listSkills).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** Associate Interests to Project **********************/
	@POST
	@Path("/{idProject}/associateInterest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response associateInterest(@HeaderParam("token") String token, @PathParam("idProject") String idProject,
			@QueryParam("idInterest") String idInterest) {

		Project projectToAssociateInterests = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));
		if (token != null && !token.isEmpty() && projectToAssociateInterests != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null
					|| !userAux.getEmail().equals(projectToAssociateInterests.getUserJoinProject().getEmail())
							&& !userAux.getEmail().equals(projectToAssociateInterests.getUserJoinProject().getEmail())
							&& userAux.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			} else {
				List<InterestDTO> listInterests = projBean.associateInterestToProject(Integer.parseInt(idInterest),
						projectToAssociateInterests);
				if (listInterests != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listInterests).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** Disassociate Interests To Project ******************/
	@POST
	@Path("/{idProject}/disassociateInterest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response disassociateInterest(@HeaderParam("token") String token, @PathParam("idProject") String idProject,
			@QueryParam("idInterest") String idInterest) {

		Project projectToDisassociateInterests = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));
		if (token != null && !token.isEmpty() && projectToDisassociateInterests != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null
					|| !userAux.getEmail().equals(projectToDisassociateInterests.getUserJoinProject().getEmail())
							&& !userAux.getEmail()
									.equals(projectToDisassociateInterests.getUserJoinProject().getEmail())
							&& userAux.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			} else {
				List<InterestDTO> listInterests = projBean.disassociateInterestToProject(Integer.parseInt(idInterest),
						projectToDisassociateInterests);
				if (listInterests != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listInterests).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/********** Associate Necessity/Idea to a Project **************************/
	@POST
	@Path("/{idProject}/associateNecessityIdea")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response associateNecessityIdea(@HeaderParam("token") String token, @PathParam("idProject") String idProject,
			@QueryParam("idNecessityIdea") String idNecessityIdea) {

		Project projectToAssociateIdeaNecessity = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));
		if (token != null && !token.isEmpty() && projectToAssociateIdeaNecessity != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				List<IdeaNecessityDTO> listToReturn = projBean.associateIdeaNecessityToProject(
						Integer.parseInt(idNecessityIdea), projectToAssociateIdeaNecessity);
				if (listToReturn != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listToReturn).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/********** Disassociate Necessity/Idea to a Project **************************/
	@POST
	@Path("/{idProject}/disassociateNecessityIdea")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response disassociateNecessityIdea(@HeaderParam("token") String token,
			@PathParam("idProject") String idProject, @QueryParam("idNecessityIdea") String idNecessityIdea) {

		Project projectToDisassociateIdeaNecessity = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));
		if (token != null && !token.isEmpty() && projectToDisassociateIdeaNecessity != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				List<IdeaNecessityDTO> listToReturn = projBean.disassociateIdeaNecessityToProject(
						Integer.parseInt(idNecessityIdea), projectToDisassociateIdeaNecessity);
				if (listToReturn != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listToReturn).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/********************************* Mark Project as Favourite ********************************/
	@POST
	@Path("/addFavorite/{idProject}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectProjectAsFavorite(@HeaderParam("token") String token,
			@PathParam("idProject") String idProject) {

		Project projectToDisassociateIdeaNecessity = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));
		if (token != null && !token.isEmpty() && projectToDisassociateIdeaNecessity != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				if (projBean.favoriteProject(Integer.parseInt(idProject), userAux)) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.status(200).build();
				}

			}
		}
		return Response.status(401).build();
	}

	/********************************* remove Project as Favourite ********************************/
	@POST
	@Path("/removeFavorite/{idProject}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeFavoriteProject(@HeaderParam("token") String token,
			@PathParam("idProject") String idProject) {

		Project projectToDisassociateIdeaNecessity = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));
		if (token != null && !token.isEmpty() && projectToDisassociateIdeaNecessity != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				if (projBean.removeFavoriteProject(Integer.parseInt(idProject), userAux)) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.status(200).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/********************************* CHANGE PROJECT TO CONCLUDED ********************************/
	@POST
	@Path("concludeProject/{idProject}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response concludeProject(@HeaderParam("token") String token, @PathParam("idProject") String idProject) {
		Project concludeProject = projBean.findProjectIfNotDeleted(Integer.parseInt(idProject));
		if (token != null && !token.isEmpty() && concludeProject != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				if (projBean.concludeProject(concludeProject)) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok().build();
				}
			}
		}
		return Response.status(401).build();
	}

	/**************************************** Get a specificProject ****************************************/
	@GET
	@Path("/{idProject}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectById(@HeaderParam("token") String token, @PathParam("idProject") String idProject) {

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				ProjectDTO projectDTO = projBean.findProjectDTOIfNotDeleted(Integer.parseInt(idProject));
				if (projectDTO != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(projectDTO).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** Get all Projects **********************/
	@GET
	@Path("/allProjects")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllProjects(@HeaderParam("token") String token) {
		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {

				List<ProjectDTO> listProjects = new ArrayList<ProjectDTO>();

				if (userAux.getUserType() == UserType.ADMIN) {
					listProjects = projBean.findAllProjects();
				} else {
					listProjects = projBean.findAllProjectsInProgress();
				}
				
				if (listProjects != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listProjects).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/**************************************** Get all Projects that User marked as favorite ****************************************/
	@GET
	@Path("/favorite/{idUser}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllProjectsUserMarkedAsFavorite(@HeaderParam("token") String token,
			@PathParam("idUser") String idUser) {
		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null || userAux.getIdUser() != Integer.parseInt(idUser)
					&& userAux.getIdUser() != Integer.parseInt(idUser) && userAux.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			} else {

				User userToConsult = servUser.findUserByID(Integer.parseInt(idUser));
				if (userToConsult != null) {
					List<ProjectDTO> listProjects = projBean
							.findAllProjectsMarkedAsFavoriteByUser(userToConsult.getEmail());
					if (listProjects != null) {
						logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
						return Response.ok(listProjects).build();
					}
				}
			}
		}
		return Response.status(401).build();
	}

	/**************************************** Get allideasNecessityfromProject ****************************************/
	@GET
	@Path("/ideasNecessity/{idProject}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllIdeasNecessityfromProject(@HeaderParam("token") String token,
			@PathParam("idProject") String idProject) {
		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				List<IdeaNecessityDTO> listIdeasNecessity = projBean
						.findAllIdeasNecessityAssociatedProject(Integer.parseInt(idProject));
				if (listIdeasNecessity != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listIdeasNecessity).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/**************************************** Get interests from Project ****************************************/
	@GET
	@Path("/interestList/{idProject}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllInterestAssociateIdProject(@HeaderParam("token") String token,
			@PathParam("idProject") String idProject) {
		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				List<InterestDTO> interestList = projBean
						.findAllInterestsAssociatedProject(Integer.parseInt(idProject));
				if (interestList != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(interestList).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/**************************************** Get skills from Project ****************************************/
	@GET
	@Path("/skillList/{idProject}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSkillAssociateIdProject(@HeaderParam("token") String token,
			@PathParam("idProject") String idProject) {
		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				List<SkillDTO> skillList = projBean.findAllSkillAssociatedProject(Integer.parseInt(idProject));
				if (skillList != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(skillList).build();
				}
			}
		}
		return Response.status(401).build();
	}
}