package pt.uc.dei.paj.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pt.uc.dei.paj.DTO.GroupVisibilityDTO;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.service.GroupVisibilityBean;
import pt.uc.dei.paj.service.UserBean;

@Path("/groupvisibility")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GroupVisibilityController {
	@Inject
	private UserBean servUser;
	@Inject
	private GroupVisibilityBean servGroup;

	// set group for a user
	@POST
	@Path("/setGroup")
	public Response setaGroup(@HeaderParam("token") String token, GroupVisibilityDTO groupDTO) {
		if (token != null && !token.isEmpty() && groupDTO != null) {

			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else {
				List<GroupVisibilityDTO> group = servGroup.setGroup(user, groupDTO);
				if (group!=null) {

					return Response.ok(group).build();
				}
			}
		}
		return Response.status(401).build();
	}
	
	
	// set group for a user
	@POST
	@Path("/deleteGroup")
	public Response deleteaGroup(@HeaderParam("token") String token, String idGroup) {
		if (token != null && !token.isEmpty() && idGroup != null) {
			User user = servUser.checkIfTokenIsValid(token);
			if (user == null) {
				return Response.status(403).build();
			} else {
				List<GroupVisibilityDTO> group = servGroup.deleteGroup(Integer.parseInt(idGroup), user);
				if (group!=null) {

					return Response.ok(group).build();
				}
	
							}
		}
		return Response.status(401).build();
	}
}
