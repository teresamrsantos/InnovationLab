package pt.uc.dei.paj.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import pt.uc.dei.paj.DTO.IdeaNecessityDTO;
import pt.uc.dei.paj.DTO.InterestDTO;
import pt.uc.dei.paj.DTO.ProjectDTO;
import pt.uc.dei.paj.DTO.SkillDTO;
import pt.uc.dei.paj.DTO.UserDTO;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.general.SaveFile;
import pt.uc.dei.paj.general.UserType;
import pt.uc.dei.paj.general.Visibility;
import pt.uc.dei.paj.service.ProjectBean;
import pt.uc.dei.paj.service.UserBean;

@Path("/users")

public class UserController {
	private static final Logger logger = LogManager.getLogger(UserController.class);
	@Context
	private javax.servlet.http.HttpServletRequest hsr;
	@Inject
	private UserBean servUser;
	@Inject
	private ProjectBean projBean;

	@PostConstruct
	public void init() {
		// step-1 : set hostName into System's property, which will use by log4j

		// System.setProperty("IP", hsr.getRemoteAddr());

		/*
		 * List<User> usersList = servUser.userList(); if (usersList.size() == 0) { //
		 * se a lista estiver vazia é criado um admin, como administrador principal do
		 * // sistema UserDTO user_Num1 = new UserDTO();
		 * user_Num1.setFirstName("Teresa"); user_Num1.setLastName("Santos");
		 * user_Num1.setEmail("t.santos@mail.com");
		 * user_Num1.setWorkplace(Workplace.PORTO);
		 * user_Num1.setUserType(UserType.ADMIN); user_Num1.setUsername("tsantos");
		 * user_Num1.setPictureUrl("https://randomuser.me/api/portraits/women/22.jpg");
		 * user_Num1.setPassword("123"); //user_Num1.
		 * setBiography("Mentirosa es tu que dizes que tens so tres homens e eu sei ja de cinco."
		 * ); user_Num1.
		 * setBiography("Lorem Ipsum is simply dummy text of the printing and typesetting industry."
		 * ); servUser.registerNewUser(user_Num1);
		 */
	}

	/******************** Rest API - LOGIN METHOD ****************************/
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(String credentials) {
		JSONObject joCredentials = new JSONObject(credentials);
		String email = joCredentials.getString("email");
		String password = joCredentials.getString("password");

		if (email.isEmpty() || password.isEmpty()) {
			return Response.status(401).build();
		}

		JSONObject user = servUser.login(email, password);
		if (user != null) {
			logger.info("IP " + hsr.getRemoteAddr() + " User " + email);
			return Response.ok(user.toString()).build();
		} else {
			return Response.status(403).build();
		}

	}

	/******************** Register a new User ****************************/
	@POST
	@Path("/register")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response registerUser(MultipartFormDataInput input) {

		List<InputPart> imgpart = input.getFormDataMap().get("image");
		List<InputPart> userJSON = input.getFormDataMap().get("userJSON");

		if (userJSON != null) {
			String stringnewUserDTO = null;
			InputStream is2 = null;
			UserDTO newUserDTO = null;
			try {

				stringnewUserDTO = input.getFormDataPart("userJSON", String.class, null);

				ObjectMapper m = new ObjectMapper();
				newUserDTO = m.readValue(stringnewUserDTO, UserDTO.class);

			} catch (IOException e) {
				e.printStackTrace();
			}
			String UPLOAD_FOLDER = "\\users\\";

			User userEmail = servUser.findUserByEmail(newUserDTO.getEmail());
			if (userEmail != null) {
				return Response.status(406).build();
			} else {
				if (newUserDTO != null) {
					if (imgpart != null) {
						try {
							is2 = input.getFormDataPart("image", InputStream.class, null);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						String picturePath = null;
						try {
							picturePath = SaveFile.saveToFile(newUserDTO.getEmail()+System.currentTimeMillis(), is2, UPLOAD_FOLDER);
						} catch (IOException e) {
							e.printStackTrace();
						}

						if (picturePath == null) {
							return Response.status(401).build();
						} else {

							newUserDTO.setPictureUrl(picturePath);
						}
					} else {
						newUserDTO.setPictureUrl(null);
					}

					UserDTO userDTO = servUser.registerNewUser(newUserDTO);
					if (userDTO != null) {
						logger.info("IP " + hsr.getRemoteAddr() + " User " + userDTO.getEmail());
						return Response.ok().build();
					}
				}
			}

		}
		return Response.status(401).build();

	}

	/******************** Activation Count User ****************************/
	@POST
	@Path("/activation")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response activationCountUser(@HeaderParam("activation") String activation) {

		User user = servUser.findUserByActivation(activation);
		if (activation != null && !activation.isEmpty()) {
			if (servUser.activationCount(activation)) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok().build();
			}
		}
		return Response.status(401).build();
	}

	/************************* USER FORGOT PASSWORD ***************************/
	@POST
	@Path("/forgotMyPassword/{email}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response forgotMyPassword(@PathParam("email") String email) {

		try {
			User userPassword = servUser.findUserByEmail(email);

			if (userPassword != null && servUser.resetUserPasswordSendEmail(userPassword)) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + userPassword.getEmail());
				return Response.status(200).build();
			}
			return Response.status(401).build();
		} catch (Exception e) {
			return Response.status(401).build();
		}
	}

	/******************** Reset password ****************************/
	@POST
	@Path("/resetPassword")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response resetPassword(@HeaderParam("resetToken") String resetToken, String newPassword) {

		User user = servUser.findUserByActivation(resetToken);

		if (user != null && resetToken != null && !resetToken.isEmpty()) {
			if (servUser.resetUserPassword(resetToken, newPassword)) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.status(200).build();
			}
		}
		return Response.status(401).build();
	}

	/******************** Edit User profile ****************************/
	@POST
	@Path("/editProfile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response changeUserProfile(@HeaderParam("token") String token, MultipartFormDataInput input) {

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				List<InputPart> imgpart = input.getFormDataMap().get("image");
				List<InputPart> userJSON = input.getFormDataMap().get("userJSON");

				if (userJSON != null) { // )&& userJSON != null) {
					String stringnewUserDTO = null;
					InputStream is2 = null;
					UserDTO newUserDTO = null;
					try {

						stringnewUserDTO = input.getFormDataPart("userJSON", String.class, null);

						ObjectMapper m = new ObjectMapper();
						newUserDTO = m.readValue(stringnewUserDTO, UserDTO.class);

					} catch (IOException e) {
						 e.printStackTrace();
					}
					String UPLOAD_FOLDER = "\\users\\";

					if (newUserDTO != null) {
						if (imgpart != null) {
							try {
								is2 = input.getFormDataPart("image", InputStream.class, null);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							String picturePath = null;
							try {
								picturePath = SaveFile.saveToFile(newUserDTO.getEmail(), is2, UPLOAD_FOLDER);
							} catch (IOException e) {
								e.printStackTrace();
							}

							if (picturePath == null) {
								return Response.status(401).build();
							} else {

								newUserDTO.setPictureUrl(picturePath);
							}
						} else {
							newUserDTO.setPictureUrl(null);
						}
						User newUser = servUser.findUserByID(newUserDTO.getIdUser());
						if (servUser.changeUserProfile(newUser, newUserDTO)) {
							logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
							return Response.status(200).build();
						}

					}
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** Edit User password **********************/
	@POST
	@Path("/editPassword")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeUserPassword(@HeaderParam("token") String token, String passwords) {
		JSONObject joCredentials = new JSONObject(passwords);
		String oldPassword = joCredentials.getString("oldPassword");
		String newPassword = joCredentials.getString("newPassword");

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			User userPasswordIsCorrect = servUser.verifyPasswordAndEmail(oldPassword, userAux.getEmail());

			if (userAux == null || userPasswordIsCorrect == null || userPasswordIsCorrect.equals(userAux)) {
				return Response.status(403).build();
			} else {
				if (servUser.changeUserPassword(userAux, newPassword)) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.status(200).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************* EDIT USER PROFILE VISIBILITY ********************/
	@POST
	@Path("/editProfileVisibility/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editProfileVisibility(@HeaderParam("token") String token, @PathParam("id") String id,
			Visibility visibility) {

		int idUser = Integer.parseInt(id);
		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null || userAux.getIdUser() != (idUser) && userAux.getIdUser() != (idUser)
					&& userAux.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			} else {
				User userIDaux = servUser.findUserByID(idUser);
				if (userIDaux != null) {
					if (servUser.changeUserProfileVisibility(userIDaux, visibility)) {
						logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
						return Response.status(200).build();
					}
				}
			}
		}
		return Response.status(401).build();
	}

	/******************* CHANGE USER TYPE AS AN ADMIN ****************************/
	@POST
	@Path("/usertype/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editUserType(@HeaderParam("token") String token, @PathParam("id") String id, UserType userType) {
		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null || userAux.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			} else {

				User userToEdit = servUser.findUserByID(Integer.parseInt(id));
				if (userToEdit != null) {
					List<UserDTO> listToReturn = servUser.editUserRole(userToEdit, userType, userAux);
					if (listToReturn != null)

						logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listToReturn).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** Rest - LOGOUT ****************************/
	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAllAdmins(@HeaderParam("token") String token) {

		if (token.isEmpty() || token == null) {
			return Response.status(401).build();
		} else {
			User user = servUser.checkIfTokenIsValid(token);
			if (user != null) {
				if (servUser.logout(token)) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.status(200).build();
				}
			}
			return Response.status(401).build();
		}
	}

	/******************** Add member to list of users able to see profile ********************/
	@POST
	@Path("/SpecificVisibility/AddMember/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response specificVisibilityAddMember(@HeaderParam("token") String token, @PathParam("id") String id,
			@QueryParam("idsToAdd") String idsToAdd) {
		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null || userAux.getIdUser() != Integer.parseInt(id)
					&& userAux.getIdUser() != (Integer.parseInt(id)) && userAux.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			} else {
				User userToAlteraux = servUser.findUserByID(Integer.parseInt(id));
				if (userToAlteraux != null) {
					UserDTO usersAdded = servUser.addingUsersSpecificVisibility(idsToAdd, userToAlteraux);
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(usersAdded).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** Remove member from list of users able to see profile ********************/
	@POST
	@Path("/SpecificVisibility/RemoveMember/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response specificVisibilityRemoveMember(@HeaderParam("token") String token, @PathParam("id") String id,
			@QueryParam("idsToRemove") String idsToRemove) {

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null || userAux.getIdUser() != Integer.parseInt(id)
					&& userAux.getIdUser() != (Integer.parseInt(id)) && userAux.getUserType() != UserType.ADMIN) {
				return Response.status(403).build();
			} else {
				User userToAlteraux = servUser.findUserByID(Integer.parseInt(id));
				if (userToAlteraux != null) {
					UserDTO usersRemoved = servUser.removingUsersSpecificVisibility(idsToRemove, userToAlteraux);
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok().build();

				}
			}
		}
		return Response.status(401).build();
	}

	/******************** Associate Skills **********************/
	@POST
	@Path("/associateSkill")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response associateSkill(@HeaderParam("token") String token, @QueryParam("idSkill") String idSkill) {

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {

				List<SkillDTO> listToReturn = servUser.associateSkillToUser(Integer.parseInt(idSkill), userAux);

				if (listToReturn != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listToReturn).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** Disassociate Skills ******************/
	@POST
	@Path("/disassociateSkill")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response disassociateSkill(@HeaderParam("token") String token, @QueryParam("idSkill") String idSkill) {

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {

				List<SkillDTO> listToReturn = servUser.disassociateSkillToUser(Integer.parseInt(idSkill), userAux);

				if (listToReturn != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listToReturn).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** Associate Interests **********************/
	@POST
	@Path("/associateInterest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response associateInterest(@HeaderParam("token") String token, @QueryParam("idInterest") String idInterest) {

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {

				List<InterestDTO> listToReturn = servUser.associateInterestToUser(Integer.parseInt(idInterest),
						userAux);

				if (listToReturn != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listToReturn).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** Disassociate Interests ******************/
	@POST
	@Path("/disassociateInterest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response disassociateInterest(@HeaderParam("token") String token,
			@QueryParam("idInterest") String idInterest) {

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {

				List<InterestDTO> listToReturn = servUser.disassociateInterestToUser(Integer.parseInt(idInterest),
						userAux);

				if (listToReturn != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listToReturn).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** GET User Profile by ID with Permission ******************/
	@GET
	@Path("/find/{idUser}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserProfileByID(@HeaderParam("token") String token, @PathParam("idUser") String idUser) {

		if (token != null && !token.isEmpty() && idUser != null && !idUser.isEmpty()) {

			User userToken = servUser.checkIfTokenIsValid(token);
			User userEmail = servUser.findUserByID(Integer.parseInt(idUser));
			UserDTO userDTOaux = null;
			if (userToken.getUserType() != UserType.ADMIN && userToken.getIdUser()!=userEmail.getIdUser()) {
				
				if (userToken == null || userEmail == null || !servUser.isUserAllowedToSeeProfile(userToken, userEmail)) {
					return Response.status(403).build();
				}
				 userDTOaux = servUser.returnUserDTO(userEmail.getEmail());
			} else {
				 userDTOaux = servUser.returnUserDTO(userEmail.getEmail());
			}
			
			if (userDTOaux != null) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + userToken.getEmail());
				return Response.ok(userDTOaux).build();
			}
		}
		return Response.status(401).build();
	}

	/******************** GET All Project User is Author ******************/
	@GET
	@Path("/project/author/{idUser}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllProjectUserIsAuthor(@HeaderParam("token") String token, @PathParam("idUser") String idUser) {

		if (token != null && !token.isEmpty() && idUser != null && !idUser.isEmpty()) {
			User userToken = servUser.checkIfTokenIsValid(token);
			User userEmail = servUser.findUserByID(Integer.parseInt(idUser));
			List<ProjectDTO> listProjectUserIsAuthor = null;
			if (userToken.getUserType() != UserType.ADMIN && userToken.getIdUser()!=userEmail.getIdUser()) {
				if (userToken == null || userEmail == null || !servUser.isUserAllowedToSeeProfile(userToken, userEmail)) {
					return Response.status(403).build();
				}
				listProjectUserIsAuthor = projBean.findAllProjectsWhereUserIsMember(userEmail.getEmail());
			} else {
				listProjectUserIsAuthor = projBean.findAllProjectsWhereUserIsMember(userEmail.getEmail());
			}
			if (listProjectUserIsAuthor != null) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + userToken.getEmail());
				return Response.ok(listProjectUserIsAuthor).build();
			}
		}
		return Response.status(401).build();
	}

	/******************* GET USER PROFILE (NO EMAIL ) ****************************/
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserProfileNoEmail(@HeaderParam("token") String token) {

		if (token != null && !token.isEmpty()) {
			User userToken = servUser.checkIfTokenIsValid(token);

			if (userToken == null) {
				return Response.status(403).build();
			} else {
				UserDTO userDTOaux = servUser.returnUserDTO(userToken.getEmail());
				if (userDTOaux != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userToken.getEmail());
					return Response.ok(userDTOaux).build();
				}
			}
		}
		return Response.status(401).build();
	}
	
	/******************* GET USER PROFILE BY ID****************************/
	@GET
	@Path("/profile/{idUser}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserProfilebyId(@HeaderParam("token") String token, @PathParam("idUser") String idUser) {

		if (token != null && !token.isEmpty() && idUser != null && !idUser.isEmpty()) {
			User userToken = servUser.checkIfTokenIsValid(token);

			if (userToken == null) {
				return Response.status(403).build();
			} else {
				UserDTO userDTOaux = servUser.findUserById(Integer.parseInt(idUser));
				if (userDTOaux != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userToken.getEmail());
					return Response.ok(userDTOaux).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** Get all Interests From User **********************/
	@GET
	@Path("/interestsUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllInterests(@HeaderParam("token") String token) {

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				List<InterestDTO> listInterests = servUser.getUsersInterest(userAux);
				if (listInterests != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listInterests).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** Get all Skills From User **********************/
	@GET
	@Path("/skillsUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSkills(@HeaderParam("token") String token) {

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				List<SkillDTO> listSkills = servUser.getUsersSkills(userAux);
				if (listSkills != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
					return Response.ok(listSkills).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** All IdeaNecessity User Is Author ******************/
	@GET
	@Path("/ideaNecessity/author")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Response allIdeaNecessityUserIsAuthor(
			@HeaderParam("token") String token) {

		if (token == null || token.isEmpty()) {
			return Response.status(401).build();
		}

		User user = servUser.checkIfTokenIsValid(token);
		if (user == null) {
			return Response.status(403).build();
		} else {
			List<IdeaNecessityDTO> ideaNecessityList = servUser.findIdeaNecessitiesUserIsAuthor(user.getIdUser());

			if (ideaNecessityList != null) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok(ideaNecessityList).build();
			}
		}
		return Response.status(401).build();
	}

	/********************* All IdeaNecessities Favorite Of The User ******************/
	@GET
	@Path("/ideaNecessity/favorite")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response allIdeaNecessitiesFavoriteOfTheUser(@HeaderParam("token") String token) {

		if (token == null || token.isEmpty()) {
			return Response.status(401).build();
		}

		User user = servUser.checkIfTokenIsValid(token);
		if (user == null) {
			return Response.status(403).build();
		} else {
			List<IdeaNecessityDTO> ideaNecessityList = servUser.findIdeaNecessitiesFavoriteOfTheUserMember(user.getIdUser());

			if (ideaNecessityList != null) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok(ideaNecessityList).build();
			}
		}
		return Response.status(401).build();
	}

	/********************* All Users not participating in a project ******************/
	@GET
	@Path("/usersNotParticipating/{idProject}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findUsersNotPaticipatingInProject(@HeaderParam("token") String token,
			@PathParam("idProject") String idProject) {

		if (token != null && !token.isEmpty()) {
			User userAux = servUser.checkIfTokenIsValid(token);
			if (userAux == null) {
				return Response.status(403).build();
			} else {
				if (idProject != null) {
					List<UserDTO> userList = servUser.findUsersNotPaticipatingInProject(idProject);
					if (userList != null) {
						logger.info("IP " + hsr.getRemoteAddr() + " User " + userAux.getEmail());
						return Response.ok(userList).build();
					}
				}
			}
		}
		return Response.status(401).build();
	}
	
	
	/************************** All Users but himself *************************/
	@GET
	@Path("/exceptHimself/get")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllUserExceptHimself(@HeaderParam("token") String token) {

		if (token != null && !token.isEmpty()) {

			User user = servUser.checkIfTokenIsValid(token);
				if (user == null || user.getUserType()!=UserType.ADMIN) {
		
				return Response.status(403).build();
			} else {
		
				List<UserDTO> userList = servUser.findAllUserExceptHimself(user.getEmail());
				if (userList != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(userList).build();
				}
			}
		}
		return Response.status(401).build();
	}
}