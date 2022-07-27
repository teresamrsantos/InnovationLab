package pt.uc.dei.paj.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

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
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.uc.dei.paj.DTO.IdeaNecessityDTO;
import pt.uc.dei.paj.DTO.InterestDTO;
import pt.uc.dei.paj.DTO.SkillDTO;
import pt.uc.dei.paj.DTO.UserDTO;
import pt.uc.dei.paj.entity.IdeaNecessity;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.general.SaveFile;
import pt.uc.dei.paj.general.UserType;
import pt.uc.dei.paj.service.IdeaNecessityBean;
import pt.uc.dei.paj.service.UserBean;

@Path("/ideaNecessity")
public class IdeaNecessityController {
	private static final Logger logger = LogManager.getLogger(IdeaNecessityController.class);
	@Context
	private javax.servlet.http.HttpServletRequest hsr;
	@Inject
	private UserBean servUser;
	@Inject
	private IdeaNecessityBean ideaNecessityBean;

	/******************** ADD IDEANECESSITY ****************************/
	@POST
	@Path("/addIdeaNecessity/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addIdeaNecessity(@HeaderParam("token") String token, MultipartFormDataInput input,
			@PathParam("id") String id) {
		List<InputPart> imgpart = input.getFormDataMap().get("image");
		List<InputPart> ideaJson = input.getFormDataMap().get("ideaNecessityJson");
		String stringnewIdea = null;

		InputStream is2 = null;

		if (token == null || token.isEmpty() || ideaJson == null) {
			return Response.status(401).build();
		}

		try {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null || user.getIdUser() != (Integer.parseInt(id)) && user.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			}

			IdeaNecessityDTO ideaNecessityDTO = null;
			stringnewIdea = input.getFormDataPart("ideaNecessityJson", String.class, null);

			ObjectMapper m = new ObjectMapper();
			ideaNecessityDTO = m.readValue(stringnewIdea, IdeaNecessityDTO.class);

			if (ideaNecessityBean.verificationNoRepeat(ideaNecessityDTO.getSkillAssociatedList(),
					ideaNecessityDTO.getInterestAssociatedList(), ideaNecessityDTO.getIdeaNecessityAssociatedList())) {
				return Response.status(400).build();
			} else {
				String picturePath = null;

				String UPLOAD_FOLDER = "\\ideaNecessities\\";
				if (imgpart != null && !imgpart.isEmpty()) {

					try {
						is2 = input.getFormDataPart("image", InputStream.class, null);
						if (is2 != null) {

							picturePath = SaveFile.saveToFile(
									ideaNecessityDTO.getTitle().replaceAll("\\s+", "") + System.currentTimeMillis(),
									is2, UPLOAD_FOLDER);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (picturePath != null) {
					ideaNecessityDTO.setImageIdeaNecessity(picturePath);

				}
				User userID = servUser.findUserByID(Integer.parseInt(id));
				if (userID != null) {

					if (ideaNecessityBean.addIdeaNecessity(user, ideaNecessityDTO)) {
						logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
						return Response.ok().build();
					}
				}
			}
			return Response.status(401).build();
		} catch (Exception e) {
			return Response.status(401).build();
		}
	}

	/******************** EDIT IDEANECESSITY ****************************/
	@POST
	@Path("/editIdeaNecessity/{idIdeaNecessity}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response editIdeaNecessity(@HeaderParam("token") String token, MultipartFormDataInput input,
			@PathParam("idIdeaNecessity") String idIdeaNecessity) {

		IdeaNecessity ideaNecessityToEdit = ideaNecessityBean.findIdeaNecessityEntityById(Integer.parseInt(idIdeaNecessity));
		if (token != null && !token.isEmpty() && ideaNecessityToEdit != null) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				List<InputPart> imgpart = input.getFormDataMap().get("image");
				List<InputPart> ideaJson = input.getFormDataMap().get("ideaNecessityJson");
				if (ideaJson != null) {
					String stringnewIdeaNecessityDTO = null;

					InputStream is2 = null;
					IdeaNecessityDTO newIdeaNecessityDTO = null;
					try {

						stringnewIdeaNecessityDTO = input.getFormDataPart("ideaNecessityJson", String.class, null);

						ObjectMapper m = new ObjectMapper();
						newIdeaNecessityDTO = m.readValue(stringnewIdeaNecessityDTO, IdeaNecessityDTO.class);

					} catch (IOException e) {
						e.printStackTrace();
					}
					String UPLOAD_FOLDER = "\\ideaNecessities\\";
					if (newIdeaNecessityDTO != null) {
						if (imgpart != null) {

							try {
								is2 = input.getFormDataPart("image", InputStream.class, null);

							} catch (IOException e1) {
								e1.printStackTrace();
							}
							String picturePath = null;
							try {
								picturePath = SaveFile.saveToFile(newIdeaNecessityDTO.getTitle().replaceAll("\\s+", "")
										+ System.currentTimeMillis(), is2, UPLOAD_FOLDER);
							} catch (IOException e) {
								e.printStackTrace();
							}

							if (picturePath == null) {
								return Response.status(401).build();
							} else {
								newIdeaNecessityDTO.setImageIdeaNecessity(picturePath);
							}
						} else {
							if (ideaNecessityToEdit.getImageIdeaNecessity() != null) {
								newIdeaNecessityDTO.setImageIdeaNecessity(ideaNecessityToEdit.getImageIdeaNecessity());
							} else {
								newIdeaNecessityDTO.setImageIdeaNecessity(null);
							}
						}
						if (ideaNecessityBean.editIdeaNecessity(newIdeaNecessityDTO, ideaNecessityToEdit)) {
							logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
							return Response.ok().build();
						}
					}
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** DELETE IDEANECESSITY ****************************/
	@POST
	@Path("/softDelete/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response softDelete(@HeaderParam("token") String token, @PathParam("id") String id) {
		if (token == null || token.isEmpty() || id == null || id.isEmpty()) {
			return Response.status(401).build();
		}

		try {
			User user = servUser.checkIfTokenIsValid(token);
			IdeaNecessityDTO ideaNecessityDto = ideaNecessityBean.findIdeaNecessityById(Integer.parseInt(id));
			if (user == null || user.getIdUser() != ideaNecessityDto.getIdAuthor()) {
				return Response.status(403).build();
			} else if (ideaNecessityBean.softDeleteIdeaNecessity(Integer.parseInt(id))) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok().build();
			}
			return Response.status(401).build();
		} catch (Exception e) {
			return Response.status(401).build();
		}
	}

	/******************** ASSOCIATE SKILL WITH AN IDEANECESSITY ****************************/
	@Path("/associateSkill/{idIdeaNecessity}/{idSkill}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response associateSkill(@HeaderParam("token") String token,
			@PathParam("idIdeaNecessity") String idIdeaNecessity, @PathParam("idSkill") String idSkill) {
		if (token == null || token.isEmpty() || idIdeaNecessity == null || idIdeaNecessity.isEmpty() || idSkill == null
				|| idSkill.isEmpty()) {
			return Response.status(401).build();
		}

		try {
			User user = servUser.checkIfTokenIsValid(token);
			IdeaNecessityDTO ideaNecessityDto = ideaNecessityBean
					.findIdeaNecessityById(Integer.parseInt(idIdeaNecessity));
			if (user == null
					|| user.getIdUser() != ideaNecessityDto.getIdAuthor() && user.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			} else {
				List<SkillDTO> skillDtoList = ideaNecessityBean.associateSkills(Integer.parseInt(idIdeaNecessity),
						Integer.parseInt(idSkill));
				if (!skillDtoList.isEmpty()) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(skillDtoList).build();
				}
			}
			return Response.status(400).build();
		} catch (Exception e) {
			return Response.status(401).build();
		}
	}

	/******************** ASSOCIATE INTEREST WITH AN IDEANECESSITY ****************************/
	@Path("/associateInterest/{idIdeaNecessity}/{idInterest}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response associateInterest(@HeaderParam("token") String token,
			@PathParam("idIdeaNecessity") String idIdeaNecessity, @PathParam("idInterest") String idInterest) {
		if (token == null || token.isEmpty() || idIdeaNecessity == null || idIdeaNecessity.isEmpty()
				|| idInterest == null || idInterest.isEmpty()) {
			return Response.status(401).build();
		}

		try {
			User user = servUser.checkIfTokenIsValid(token);
			IdeaNecessityDTO ideaNecessityDto = ideaNecessityBean
					.findIdeaNecessityById(Integer.parseInt(idIdeaNecessity));
			if (user == null
					|| user.getIdUser() != ideaNecessityDto.getIdAuthor() && user.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			} else {
				List<InterestDTO> interestDtoList = ideaNecessityBean
						.associateInterests(Integer.parseInt(idIdeaNecessity), Integer.parseInt(idInterest));
				if (!interestDtoList.isEmpty()) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(interestDtoList).build();
				}
			}
			return Response.status(400).build();
		} catch (Exception e) {
			return Response.status(401).build();
		}
	}

	/******************** DISASSOCIATE SKILL WITH AN IDEANECESSITY ****************************/
	@Path("/disassociateSkill/{idIdeaNecessity}/{idSkill}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response disassociateSkill(@HeaderParam("token") String token,
			@PathParam("idIdeaNecessity") String idIdeaNecessity, @PathParam("idSkill") String idSkill) {
		if (token == null || token.isEmpty() || idIdeaNecessity == null || idIdeaNecessity.isEmpty() || idSkill == null
				|| idSkill.isEmpty()) {
			return Response.status(401).build();
		}

		try {
			User user = servUser.checkIfTokenIsValid(token);
			IdeaNecessityDTO ideaNecessityDto = ideaNecessityBean
					.findIdeaNecessityById(Integer.parseInt(idIdeaNecessity));
			if (user == null
					|| user.getIdUser() != ideaNecessityDto.getIdAuthor() && user.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			} else {
				List<SkillDTO> skillDtoList = ideaNecessityBean.disassociateSkills(Integer.parseInt(idIdeaNecessity),
						Integer.parseInt(idSkill));
				if (skillDtoList != null && !skillDtoList.isEmpty()) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(skillDtoList).build();
				} else if (skillDtoList.isEmpty()) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(skillDtoList).build();
				}
			}
			return Response.status(401).build();
		} catch (Exception e) {
			return Response.status(401).build();
		}
	}

	/******************** DISASSOCIATE INTEREST WITH AN IDEANECESSITY ****************************/
	@Path("/disassociateInterest/{idIdeaNecessity}/{idInterest}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response disassociateInterest(@HeaderParam("token") String token,
			@PathParam("idIdeaNecessity") String idIdeaNecessity, @PathParam("idInterest") String idInterest) {
		if (token == null || token.isEmpty() || idIdeaNecessity == null || idIdeaNecessity.isEmpty()
				|| idInterest == null || idInterest.isEmpty()) {
			return Response.status(401).build();
		}

		try {
			User user = servUser.checkIfTokenIsValid(token);
			IdeaNecessityDTO ideaNecessityDto = ideaNecessityBean
					.findIdeaNecessityById(Integer.parseInt(idIdeaNecessity));
			if (user == null
					|| user.getIdUser() != ideaNecessityDto.getIdAuthor() && user.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			} else {
				List<InterestDTO> interestDtoList = ideaNecessityBean
						.disassociateInterests(Integer.parseInt(idIdeaNecessity), Integer.parseInt(idInterest));
				if (interestDtoList != null && !interestDtoList.isEmpty()) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(interestDtoList).build();
				} else if (interestDtoList.isEmpty()) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(interestDtoList).build();
				}
			}
			return Response.status(401).build();
		} catch (Exception e) {
			return Response.status(401).build();
		}
	}

	/******************** ASSOCIATE AN IDEANECESSITY WITH AN IDEANECESSITY ****************************/
	@Path("/associateIdeaNecessity/{idIdeaNecessity}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response associateIdeaNecessity(@HeaderParam("token") String token,
			@PathParam("idIdeaNecessity") String idIdeaNecessity, String justification) {
		if (token == null || token.isEmpty() || idIdeaNecessity == null || idIdeaNecessity.isEmpty()
				|| justification == null || justification.isEmpty()) {
			return Response.status(401).build();
		}

		JSONObject joJustification = new JSONObject(justification);
		int idAdd = Integer.parseInt(joJustification.getString("idAdd"));
		String description = joJustification.getString("description");

		try {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null || user.getUserType() == UserType.VISITOR) {
				return Response.status(403).build();
			} else if (ideaNecessityBean.associateIdeaNecessity(Integer.parseInt(idIdeaNecessity), idAdd, description,
					user)) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok().build();
			}
			return Response.status(400).build();
		} catch (Exception e) {
			return Response.status(401).build();
		}
	}

	/******************** DISASSOCIATE AN IDEANECESSITY WITH AN IDEANECESSITY ****************************/
	@Path("/disassociateIdeaNecessity/{idIdeaNecessity}/{idRemove}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response disassociateIdeaNecessity(@HeaderParam("token") String token,
			@PathParam("idIdeaNecessity") String idIdeaNecessity, @PathParam("idRemove") String idRemove) {
		if (token == null || token.isEmpty() || idIdeaNecessity == null || idIdeaNecessity.isEmpty() || idRemove == null
				|| idRemove.isEmpty()) {
			return Response.status(401).build();
		}

		try {
			User user = servUser.checkIfTokenIsValid(token);
			IdeaNecessityDTO ideaNecessity1Dto = ideaNecessityBean
					.findIdeaNecessityById(Integer.parseInt(idIdeaNecessity));
			IdeaNecessityDTO ideaNecessity2Dto = ideaNecessityBean.findIdeaNecessityById(Integer.parseInt(idRemove));

			if (user == null || user.getIdUser() != ideaNecessity1Dto.getIdAuthor()
					&& user.getIdUser() != ideaNecessity2Dto.getIdAuthor()) {
				return Response.status(403).build();
			} else if (ideaNecessityBean.disassociateIdeaNecessity(Integer.parseInt(idIdeaNecessity),
					Integer.parseInt(idRemove), user)) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok().build();
			}
			return Response.status(400).build();
		} catch (Exception e) {
			return Response.status(401).build();
		}
	}

	/******************** SELECT AN IDEANECESSITY AS A FAVORITE ****************************/
	@Path("/favorite/{idIdeaNecessity}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectFavoriteIdeaNecessity(@HeaderParam("token") String token,
			@PathParam("idIdeaNecessity") String idIdeaNecessity) {
		if (token == null || token.isEmpty() || idIdeaNecessity == null || idIdeaNecessity.isEmpty()) {
			return Response.status(401).build();
		}

		try {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else if (ideaNecessityBean.favoriteIdeaNecessity(Integer.parseInt(idIdeaNecessity), user)) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok().build();
			}
			return Response.status(400).build();
		} catch (Exception e) {
			return Response.status(401).build();
		}
	}

	/******************** REMOVE AN IDEANECESSITY AS A FAVORITE ****************************/
	@Path("/removeFavorite/{idIdeaNecessity}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeFavoriteIdeaNecessity(@HeaderParam("token") String token,
			@PathParam("idIdeaNecessity") String idIdeaNecessity) {
		if (token == null || token.isEmpty() || idIdeaNecessity == null || idIdeaNecessity.isEmpty()) {
			return Response.status(401).build();
		}

		try {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else if (ideaNecessityBean.removeFavoriteIdeaNecessity(Integer.parseInt(idIdeaNecessity), user)) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok().build();
			}
			return Response.status(400).build();
		} catch (Exception e) {
			return Response.status(401).build();
		}
	}

	/******************** SELECT AN IDEANECESSITY WITH AVAILABILITY ****************************/
	@Path("/availability/{idIdeaNecessity}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectAvailabilityIdeaNecessity(@HeaderParam("token") String token,
			@PathParam("idIdeaNecessity") String idIdeaNecessity) {
		if (token == null || token.isEmpty() || idIdeaNecessity == null || idIdeaNecessity.isEmpty()) {
			return Response.status(401).build();
		}

		try {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else if (ideaNecessityBean.availabilityIdeaNecessity(Integer.parseInt(idIdeaNecessity), user)) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok().build();
			}
			return Response.status(400).build();
		} catch (Exception e) {
			return Response.status(401).build();
		}
	}

	/******************** REMOVE AN IDEANECESSITY WITH AVAILABILITY ****************************/
	@Path("/removeAvailability/{idIdeaNecessity}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeAvailabilityIdeaNecessity(@HeaderParam("token") String token,
			@PathParam("idIdeaNecessity") String idIdeaNecessity) {
		if (token == null || token.isEmpty() || idIdeaNecessity == null || idIdeaNecessity.isEmpty()) {
			return Response.status(401).build();
		}

		try {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else if (ideaNecessityBean.removeAvailabilityIdeaNecessity(Integer.parseInt(idIdeaNecessity), user)) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok().build();
			}
			return Response.status(400).build();
		} catch (Exception e) {
			return Response.status(401).build();
		}
	}

	/******************** VOTE FOR AN IDEANECESSITY ****************************/
	@Path("/vote/{idIdeaNecessity}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response voteIdeaNecessity(@HeaderParam("token") String token,
			@PathParam("idIdeaNecessity") String idIdeaNecessity) {
		if (token == null || token.isEmpty() || idIdeaNecessity == null || idIdeaNecessity.isEmpty()) {
			return Response.status(401).build();
		}

		try {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else if (ideaNecessityBean.voteIdeaNecessity(Integer.parseInt(idIdeaNecessity), user)) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok().build();
			}
			return Response.status(400).build();
		} catch (Exception e) {
			return Response.status(401).build();
		}
	}

	/******************** REMOVE THE VOTE FOR AN IDEANECESSITY ****************************/
	@Path("/removeVote/{idIdeaNecessity}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeVoteIdeaNecessity(@HeaderParam("token") String token,
			@PathParam("idIdeaNecessity") String idIdeaNecessity) {
		if (token == null || token.isEmpty() || idIdeaNecessity == null || idIdeaNecessity.isEmpty()) {
			return Response.status(401).build();
		}

		try {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else if (ideaNecessityBean.removeVoteIdeaNecessity(Integer.parseInt(idIdeaNecessity), user)) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok().build();
			}
			return Response.status(400).build();
		} catch (Exception e) {
			return Response.status(401).build();
		}
	}

	/******************** GET ALL IDEANECESSITY ****************************/
	@GET
	@Path("/allIdeaNecessity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response allIdeaNecessity(@HeaderParam("token") String token) {
		if (token == null || token.isEmpty()) {
			return Response.status(401).build();
		}

		User user = servUser.checkIfTokenIsValid(token);
		if (user == null) {
			return Response.status(403).build();
		}

		if (user != null) {
			List<IdeaNecessityDTO> ideaNecessityList = new ArrayList<IdeaNecessityDTO>();
			if (user.getUserType() == UserType.ADMIN) {
				ideaNecessityList = ideaNecessityBean.findAllIdeaNecessity();
			} else {
				ideaNecessityList = ideaNecessityBean.findAllIdeaNecessityNoDelete();
			}

			if (ideaNecessityList != null) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok(ideaNecessityList).build();
			}
		}
		return Response.status(401).build();
	}
	
	/******************** GET ALL IDEANECESSITY NO DELETE ****************************/
	@GET
	@Path("/allIdeaNecessityNotDeleted")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response allIdeaNecessityNotDeleted(@HeaderParam("token") String token) {
		if (token == null || token.isEmpty()) {
			return Response.status(401).build();
		}

		User user = servUser.checkIfTokenIsValid(token);
		if (user == null) {
			return Response.status(403).build();
		}

		if (user != null) {
			List<IdeaNecessityDTO> ideaNecessityList  = ideaNecessityBean.findAllIdeaNecessityNoDelete();
			
			if (ideaNecessityList != null) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok(ideaNecessityList).build();
			}
		}
		return Response.status(401).build();
	}

	/******************** GET ALL IDEANECESSITY FOR SELECT BOX ****************************/
	@GET
	@Path("/allIdeaNecessityselect")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response allIdeaNecessitySelect(@HeaderParam("token") String token) {
		if (token == null || token.isEmpty()) {
			return Response.status(401).build();
		}

		User user = servUser.checkIfTokenIsValid(token);
		if (user == null) {
			return Response.status(403).build();
		}

		if (user != null) {
			JSONArray ideaNecessityList = ideaNecessityBean.allIdeaNecessitySelect();
			if (ideaNecessityList != null) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok(ideaNecessityList.toString()).build();
			}
		}
		return Response.status(401).build();
	}

	/******************** GET ALL IDEANECESSITY NO DELETE ****************************/
	@GET
	@Path("/allIdeaNecessityNoDelete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response allIdeaNecessitiesNoDelete(@HeaderParam("token") String token) {
		if (token == null || token.isEmpty()) {
			return Response.status(401).build();
		}

		User user = servUser.checkIfTokenIsValid(token);
		if (user == null) {
			return Response.status(403).build();
		} else {
			List<IdeaNecessityDTO> ideaNecessityList = ideaNecessityBean.findAllIdeaNecessityNoDelete();
			if (ideaNecessityList != null) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok(ideaNecessityList).build();
			}
		}
		return Response.status(401).build();

	}

	/******************** GET AN IDEANECESSITY BY ID ****************************/
	@GET
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findIdeaNecessityById(@HeaderParam("token") String token, @PathParam("id") String id) {
		if (token == null || token.isEmpty() || id == null || id.isEmpty()) {
			return Response.status(401).build();
		}

		User user = servUser.checkIfTokenIsValid(token);
		if (user == null) {
			return Response.status(403).build();
		} else {
			IdeaNecessityDTO ideaNecessityDTO = ideaNecessityBean.findIdeaNecessityById(Integer.parseInt(id));
			if (ideaNecessityDTO != null) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok(ideaNecessityDTO).build();
			}
		}
		return Response.status(401).build();

	}

	/******************** GET ALL SKILLS OF AN IDEANECESSITY ****************************/
	@GET
	@Path("/skill/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response allSkillsAssociateIdIdeaNecessity(@HeaderParam("token") String token, @PathParam("id") String id) {
		if (token == null || token.isEmpty() || id == null || id.isEmpty()) {
			return Response.status(401).build();
		}

		User user = servUser.checkIfTokenIsValid(token);
		if (user == null) {
			return Response.status(403).build();
		} else {
			List<SkillDTO> skillList = ideaNecessityBean.findAllSkillsAssociateIdIdeaNecessity(Integer.parseInt(id));
			if (skillList != null) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok(skillList).build();
			}
		}
		return Response.status(401).build();
	}

	/******************** GET ALL INTERESTS OF AN IDEANECESSITY ****************************/
	@GET
	@Path("/interest/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response allInterestsAssociateIdIdeaNecessity(@HeaderParam("token") String token,
			@PathParam("id") String id) {
		if (token == null || token.isEmpty() || id == null || id.isEmpty()) {
			return Response.status(401).build();
		}

		User user = servUser.checkIfTokenIsValid(token);
		if (user == null) {
			return Response.status(403).build();
		} else {
			List<InterestDTO> interestList = ideaNecessityBean
					.findAllInterestsAssociateIdIdeaNecessity(Integer.parseInt(id));
			if (interestList != null) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok(interestList).build();
			}
		}
		return Response.status(401).build();
	}

	/******************** GET ALL IDEANECESSITIES ASSOCIATE THE AN IDEANECESSITY ****************************/
	@GET
	@Path("/idIdeaNecessityAssociate/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response allIdsIdeaNecessityAssociate(@HeaderParam("token") String token, @PathParam("id") String id) {
		if (token == null || token.isEmpty() || id == null || id.isEmpty()) {
			return Response.status(401).build();
		}

		User user = servUser.checkIfTokenIsValid(token);
		if (user == null) {
			return Response.status(403).build();
		} else {
			List<Integer> idList = ideaNecessityBean
					.allIdIdeaNecessityAssociateOtherIdIdeaNecessity(Integer.parseInt(id));
			if (idList != null) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok(idList).build();
			}
		}
		return Response.status(401).build();
	}

	/******************** GET ALL USERS WITH AVAILABLE TO WORK IN AN IDEANECESSITY ****************************/
	@GET
	@Path("/available/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response allUsersWithAvailableToWorkIdIdeaNecessity(@HeaderParam("token") String token,
			@PathParam("id") String id) {
		if (token == null || token.isEmpty() || id == null || id.isEmpty()) {
			return Response.status(401).build();
		}

		User user = servUser.checkIfTokenIsValid(token);
		if (user == null) {
			return Response.status(403).build();
		} else {
			List<UserDTO> userList = ideaNecessityBean
					.findAllUserWithAvailableToWorkIdIdeaNecessity(Integer.parseInt(id));
			if (userList != null) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok(userList).build();
			}
		}
		return Response.status(401).build();
	}
}
