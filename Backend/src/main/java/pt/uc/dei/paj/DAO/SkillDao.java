package pt.uc.dei.paj.DAO;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import pt.uc.dei.paj.DTO.SkillDTO;
import pt.uc.dei.paj.entity.Skill;

@Stateless
public class SkillDao extends AbstractDao<Skill> {
	private static final long serialVersionUID = 1L;

	public SkillDao() {
		super(Skill.class);
	}

	/**
	 * Method that converts skill of the type DTO to skill of the type Entity
	 * 
	 * @param skillDto
	 * @return Skill
	 */
	public Skill convertDtoToEntity(SkillDTO skillDto) {
		Skill skill = new Skill();

		if (skillDto.getDescription() != null && !skillDto.getDescription().isEmpty()) {
			skill.setDescription(skillDto.getDescription().toLowerCase());
		}
		if (skillDto.getSkillType() != skill.getSkillType() && skillDto.getSkillType() != null
				&& !skillDto.getSkillType().equals("")) {
			skill.setSkillType(skillDto.getSkillType());
		}
		return skill;
	}

	/**
	 * Method that converts a skill list of the type Entity to skill list of the type DTO
	 * 
	 * @param skillList
	 * @return SkillDTO List
	 */
	public List<SkillDTO> convertEntityToDto(List<Skill> skillList) {
		List<SkillDTO> skillDtoList = new ArrayList<SkillDTO>();
		for (Skill skill : skillList) {
			skillDtoList.add(convertEntityToDto(skill));
		}
		return skillDtoList;
	}

	/**
	 * Method that converts skill of the type Entity to skill of the type DTO
	 * 
	 * @param skill
	 * @return SkillDTO
	 */
	public SkillDTO convertEntityToDto(Skill skill) {
		SkillDTO skillDTO = new SkillDTO();
		skillDTO.setIdSkill(skill.getIdSkill());
		skillDTO.setDescription(skill.getDescription());
		skillDTO.setSkillType(skill.getSkillType());
		skillDTO.setCreationTime(skill.getCreationTime());
		skillDTO.setDeletedSkill(skill.isDeletedSkill());
		return skillDTO;
	}

	/**
	 * Method that returns skills from the DB that contains exactly the description
	 * word
	 * 
	 * @param description
	 * @return Skill List
	 */
	public List<Skill> findSkillByDescription(String description) {
		List<Skill> resultList = em.createNamedQuery("Skill.findSkillByDescription")
				.setParameter("description", description).getResultList();
		return resultList;
	}

	/**
	 * Method that returns skills from the DB that contains the description of the word search
	 * 
	 * @param descriptio
	 * @return Skill List
	 */
	public List<Skill> findSkillsbyWordSearch(String description) {
		List<Skill> resultList = em.createNamedQuery("Skill.findSkillsbyWordSearch")
				.setParameter("description", description).getResultList();
		return resultList;
	}
}
