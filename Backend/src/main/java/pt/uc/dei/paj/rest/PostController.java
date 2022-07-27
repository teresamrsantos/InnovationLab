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

import pt.uc.dei.paj.DTO.AnswerDTO;
import pt.uc.dei.paj.DTO.CommentDTO;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.general.UserType;
import pt.uc.dei.paj.service.PostBean;
import pt.uc.dei.paj.service.UserBean;

@Path("/post")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostController {
	private static final Logger logger = LogManager.getLogger(PostController.class);
	@Context
	private javax.servlet.http.HttpServletRequest hsr;
	@Inject
	private UserBean servUser;
	@Inject
	private PostBean postBean;

	/******************** ADD COMMENT ****************************/
	@POST
	@Path("/addComment")
	public Response addComment(@HeaderParam("token") String token, CommentDTO commentDTO) {
		if (token != null && !token.isEmpty() && commentDTO != null) {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null || user.getUserType() == UserType.VISITOR) {
				return Response.status(403).build();
			} else {
				CommentDTO newCommentDTO = postBean.addComment(commentDTO, user);
				if (newCommentDTO != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(newCommentDTO).build();
				}
			}
		}
		return Response.status(401).build();
	}
	
	/******************** ADD ANSWER ****************************/
	@POST
	@Path("/addAnswer")
	public Response addAnswer(@HeaderParam("token") String token, AnswerDTO answerDTO) {
		if (token != null && !token.isEmpty() && answerDTO != null) {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null || user.getUserType() == UserType.VISITOR) {
				return Response.status(403).build();
			} else {
				AnswerDTO newAnswerDTO = postBean.addAnswer(answerDTO, user);
				if (newAnswerDTO != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(newAnswerDTO).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** ALL COMMENTS ****************************/
	@GET
	@Path("/allComments/{id}")
	public Response allCommentsAndAnswesByIdIdeaNecessity(@HeaderParam("token") String token,
			@PathParam("id") String id) {

		if (token == null || token.isEmpty() || id == null || id.isEmpty()) {
			return Response.status(401).build();
		}

		User user = servUser.checkIfTokenIsValid(token);
		if (user == null) {
			return Response.status(403).build();
		} else {
			List<CommentDTO> commentList = postBean.findAllCommentsAndAnswesByIdIdeaNecessity(Integer.parseInt(id));
			if (commentList != null) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok(commentList).build();
			}
		}
		return Response.status(401).build();
	}
}
