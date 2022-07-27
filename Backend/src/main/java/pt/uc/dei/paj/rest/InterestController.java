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
import org.json.JSONArray;

import pt.uc.dei.paj.DTO.InterestDTO;
import pt.uc.dei.paj.entity.Interest;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.service.InterestBean;
import pt.uc.dei.paj.service.UserBean;

@Path("/interest")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InterestController {
	private static final Logger logger = LogManager.getLogger(InterestController.class);
	@Context
	private javax.servlet.http.HttpServletRequest hsr;
	@Inject
	private UserBean servUser;
	@Inject
	private InterestBean interestBean;

	/******************** ADD INTEREST ****************************/
	@POST
	@Path("/addInterest")
	public Response addInterest(@HeaderParam("token") String token, InterestDTO interestDTO) {
		if (token == null || token.isEmpty() || interestDTO == null || interestDTO.getDescription().isEmpty()
				|| interestDTO.getDescription() == null) {
			return Response.status(401).build();
		}

		User user = servUser.checkIfTokenIsValid(token);
		if (user == null) {
			return Response.status(403).build();
		} else if (interestBean.validateDescriptionInterest(interestDTO.getDescription())) {
			return Response.status(400).build();
		
		} else {
			Interest interest = interestBean.addInterest(interestDTO);
			if (interest!= null) {
		
			logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
			return Response.ok(interest.getIdInterest()).build();
		}}
		return Response.status(401).build();

	}

	/******************** GET ALL INTERESTS FOR SELECT BOX ****************************/
	@GET
	@Path("/allInterestSelect")
	public Response allInterestSelect(@HeaderParam("token") String token) {

		if (token != null && !token.isEmpty()) {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else {
				JSONArray interestList = interestBean.allInterestSelect();
				if (interestList != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(interestList.toString()).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** GET INTEREST BY ID ****************************/
	@GET
	@Path("/{id}")
	public Response interestById(@HeaderParam("token") String token, @PathParam("id") String id) {
		if (token != null && !token.isEmpty() && id != null && !id.isEmpty()) {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else {
				InterestDTO interestDto = interestBean.findInterestById(Integer.parseInt(id));
				if (interestDto == null || interestDto.isDeletedInterest()) {
					return Response.status(400).build();
				} else if (!interestDto.isDeletedInterest()) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(interestDto).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** GET ALL INTERESTS BY WORD SEARCH ****************************/
	@GET
	@Path("/activeSearch/{wordSearch}")
	public Response allInterestsByWordSearch(@HeaderParam("token") String token,
			@PathParam("wordSearch") String wordSearch) {
		if (token != null && !token.isEmpty() && wordSearch != null && !wordSearch.isEmpty()) {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else {
				List<InterestDTO> interestList = interestBean.findInterestsbyWordSearch(wordSearch);
				if (interestList != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(interestList).build();
				}
			}
		}
		return Response.status(401).build();
	}
}
