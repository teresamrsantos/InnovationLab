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

import pt.uc.dei.paj.DTO.SkillDTO;
import pt.uc.dei.paj.entity.Skill;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.service.SkillBean;
import pt.uc.dei.paj.service.UserBean;

@Path("/skill")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SkillController {
	private static final Logger logger = LogManager.getLogger(SkillController.class);
	@Context
	private javax.servlet.http.HttpServletRequest hsr;
	@Inject
	private UserBean servUser;
	@Inject
	private SkillBean skillBean;

	/******************** ADD SKILL ****************************/
	@POST
	@Path("/addSkill")
	public Response addSkill(@HeaderParam("token") String token, SkillDTO skillDTO) {
		if (token == null || token.isEmpty() || skillDTO == null || skillDTO.getDescription().isEmpty()
				|| skillDTO.getDescription() == null || skillDTO.getSkillType() == null
				|| skillDTO.getDescription().isEmpty()) {
			return Response.status(401).build();
		}

		User user = servUser.checkIfTokenIsValid(token);

		if (user == null) {
			return Response.status(403).build();
		} else {
			if (skillBean.validateDescriptionSkill(skillDTO.getDescription(), skillDTO.getSkillType())) {
				return Response.status(400).build();
			} else {
				Skill skill = (skillBean.addSkill(skillDTO));
				if(skill!=null) {
				logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
				return Response.ok(skill.getIdSkill()).build();
			}
			}
			return Response.status(401).build();
		}
	}

	/******************** GET ALL SKILLS FOR SELECT BOX ****************************/
	@GET
	@Path("/allSkillSelect")
	public Response allSkillSelect(@HeaderParam("token") String token) {

		if (token != null && !token.isEmpty()) {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else {
				JSONArray skillList = skillBean.allSkillSelect();
				if (skillList != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(skillList.toString()).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** GET SKILL BY ID ****************************/
	@GET
	@Path("/{id}")
	public Response skillById(@HeaderParam("token") String token, @PathParam("id") String id) {

		if (token != null && !token.isEmpty() && id != null && !id.isEmpty()) {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else {
				SkillDTO skillDto = skillBean.findSkillById(Integer.parseInt(id));
				if (skillDto == null || skillDto.isDeletedSkill()) {
					return Response.status(400).build();
				} else if (!skillDto.isDeletedSkill()) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(skillDto).build();
				}
			}
		}
		return Response.status(401).build();
	}

	/******************** GET ALL SKILLS BY WORD SEARCH ****************************/
	@GET
	@Path("/activeSearch/{wordSearch}")
	public Response allSkillsByWordSearch(@HeaderParam("token") String token,
			@PathParam("wordSearch") String wordSearch) {

		if (token != null && !token.isEmpty() && wordSearch != null && !wordSearch.isEmpty()) {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else {
				List<SkillDTO> skillList = skillBean.findAllSkillByWordSearch(wordSearch);
				if (skillList != null) {
					logger.info("IP " + hsr.getRemoteAddr() + " User " + user.getEmail());
					return Response.ok(skillList).build();
				}
			}
		}
		return Response.status(401).build();
	}
}
