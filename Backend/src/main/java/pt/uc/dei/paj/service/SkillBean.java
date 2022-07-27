package pt.uc.dei.paj.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.json.JSONObject;
import org.json.JSONArray;

import pt.uc.dei.paj.DAO.IdeaNecessityDao;
import pt.uc.dei.paj.DAO.ProjectDao;
import pt.uc.dei.paj.DAO.SkillDao;
import pt.uc.dei.paj.DAO.UserDao;
import pt.uc.dei.paj.DTO.SkillDTO;
import pt.uc.dei.paj.entity.Skill;
import pt.uc.dei.paj.general.SkillType;

@RequestScoped
public class SkillBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	UserDao userDao;

	@Inject
	SkillDao skillDao;

	@Inject
	IdeaNecessityDao ideaNecessityDao;

	@Inject
	ProjectDao projectDao;

	/**
	 * Method to add a Skill
	 * 
	 * @param skillDto object add
	 * @return sucess of the operation
	 */
	public Skill addSkill(SkillDTO skillDto) {
		try {
			Skill skill = skillDao.convertDtoToEntity(skillDto);
			skillDao.persist(skill);
			return skill;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that validates if there is skill with the description you want to add
	 * 
	 * @param description
	 * @return true if there is an equal description and false if not
	 */
	public boolean validateDescriptionSkill(String description, SkillType type) {
		try {
			List<Skill> skillsList = skillDao.findSkillByDescription(description.toLowerCase());

			if (!skillsList.isEmpty() && skillsList != null) {
				for (Skill skill : skillsList) {
					if (skill.getSkillType().equals(type)) {
						return true;
					}
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Method to find a skill by Id
	 * 
	 * @param idInterest
	 * @return SkillDTO or null
	 */
	public SkillDTO findSkillById(int idSkill) {
		try {
			Skill skill = skillDao.find(idSkill);
			if (skill != null) {
				return skillDao.convertEntityToDto(skill);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to find a skill by word or expression to search all skills that start
	 * with that word
	 * 
	 * @param WordSearch
	 * @return list of skills
	 */
	public List<SkillDTO> findAllSkillByWordSearch(String WordSearch) {
		try {
			List<Skill> skillList = skillDao.findSkillsbyWordSearch(WordSearch.toLowerCase());
			if (!skillList.isEmpty() && skillList.get(0) != null) {
				return skillDao.convertEntityToDto(skillList);
			}
			return new ArrayList<SkillDTO>();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to find a skills by id
	 * 
	 * @return list of skills
	 */
	public List<Skill> findSkillsbyId(List<Integer> idSkillList) {
		try {
			List<Skill> listToReturn = new ArrayList<Skill>();
			for (Integer skillId : idSkillList) {
				Skill skill = skillDao.find(skillId);
				if (skill != null) {
					listToReturn.add(skill);
				}
			}
			return listToReturn;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that returns a array object of skills with all the information necessary to select box
	 * 
	 * @return JSONArray
	 */
	public JSONArray allSkillSelect() {
		try {
			List<Skill> skillList = skillDao.findAll();
			JSONArray jsonArray = new JSONArray();
			for (Skill skill : skillList) {
				JSONObject json = new JSONObject();
				json.put("label", skill.getDescription()+" - "+skill.getSkillType());
				json.put("value", skill.getDescription()+" - "+skill.getSkillType());
				json.put("id", skill.getIdSkill());
				jsonArray.put(json);
			}
			return jsonArray;
		} catch (Exception e) {
			return null;
		}
	}

}
