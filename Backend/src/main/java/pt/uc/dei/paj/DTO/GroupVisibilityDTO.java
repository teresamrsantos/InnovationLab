package pt.uc.dei.paj.DTO;

import java.util.ArrayList;
import java.util.List;

public class GroupVisibilityDTO {
	
	private List<Integer> idSkillList = new ArrayList<Integer>();
	private List<Integer> idInterestList= new ArrayList<Integer>();
	private List<InterestDTO> interestDTOList = new ArrayList<InterestDTO>();
	private List<SkillDTO> skillDTOList= new ArrayList<SkillDTO>();
	
	private String usernameUser;
	private int idGroup;
	private String workplace;
	public List<Integer> Skill() {
		return idSkillList;
	}
	public void setIdSkillList(List<Integer> idSkillList) {
		this.idSkillList = idSkillList;
	}
	public List<Integer> getIdInterestList() {
		return idInterestList;
	}
	
	public List<Integer> getIdSkillList() {
		return idSkillList;
	}
	public List<InterestDTO> getInterestDTOList() {
		return interestDTOList;
	}
	public void setInterestDTOList(List<InterestDTO> interestDTOList) {
		this.interestDTOList = interestDTOList;
	}
	public List<SkillDTO> getSkillDTOList() {
		return skillDTOList;
	}
	public void setSkillDTOList(List<SkillDTO> skillDTOList) {
		this.skillDTOList = skillDTOList;
	}
	public void setIdInterestList(List<Integer> idInterestList) {
		this.idInterestList = idInterestList;
	}
	public String getUsernameUser() {
		return usernameUser;
	}
	public void setUsernameUser(String usernameUser) {
		this.usernameUser = usernameUser;
	}
	public int getIdGroup() {
		return idGroup;
	}
	public void setIdGroup(int idGroup) {
		this.idGroup = idGroup;
	}
	public String getWorkplace() {
		return workplace;
	}
	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}
	@Override
	public String toString() {
		return "GroupVisibilityDTO [idSkillList=" + idSkillList + ", idInterestList=" + idInterestList
				+ ", usernameUser=" + usernameUser + ", idGroup=" + idGroup + ", workplace=" + workplace + "]";
	}
	

}
