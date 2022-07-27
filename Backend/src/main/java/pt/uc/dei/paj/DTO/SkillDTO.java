package pt.uc.dei.paj.DTO;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import pt.uc.dei.paj.general.SkillType;

@XmlRootElement
public class SkillDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private int idSkill;
	private Date creationTime;
	private String description;
	private SkillType skillType;
	private boolean deletedSkill;
	
	public SkillDTO() {
	}

	public int getIdSkill() {
		return idSkill;
	}

	public void setIdSkill(int idSkill) {
		this.idSkill = idSkill;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SkillType getSkillType() {
		return skillType;
	}

	public void setSkillType(SkillType skillType) {
		this.skillType = skillType;
	}

	public boolean isDeletedSkill() {
		return deletedSkill;
	}

	public void setDeletedSkill(boolean deletedSkill) {
		this.deletedSkill = deletedSkill;
	}

	@Override
	public String toString() {
		return "SkillDTO [idSkill=" + idSkill + ", creationTime=" + creationTime + ", description=" + description
				+ ", skillType=" + skillType + ", deletedSkill=" + deletedSkill + "]";
	}
		
}
