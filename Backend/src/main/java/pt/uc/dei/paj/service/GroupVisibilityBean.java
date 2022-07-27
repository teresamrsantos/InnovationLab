package pt.uc.dei.paj.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import pt.uc.dei.paj.DAO.GroupVisibilityDao;
import pt.uc.dei.paj.DAO.InterestDao;
import pt.uc.dei.paj.DAO.SkillDao;
import pt.uc.dei.paj.DAO.UserDao;
import pt.uc.dei.paj.DTO.GroupVisibilityDTO;
import pt.uc.dei.paj.entity.GroupVisibility;
import pt.uc.dei.paj.entity.Interest;
import pt.uc.dei.paj.entity.Skill;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.general.Workplace;

@RequestScoped
public class GroupVisibilityBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@Inject
	UserDao userDao;

	@Inject
	GroupVisibilityDao groupDao;
	@Inject
	InterestDao interestDao;
	@Inject
	SkillDao skillDao;
	@Inject
	InterestBean interestBean;

	@Inject
	SkillBean skillBean;

	public List<GroupVisibilityDTO> setGroup(User user, GroupVisibilityDTO groupDTO) {

		GroupVisibility groupVisibility = new GroupVisibility();
		groupVisibility.setUserGroup(user);
		
		if (groupDTO.getWorkplace() != null &&groupDTO.getWorkplace() != "" ) {
			groupVisibility.setWorkplace(Workplace.valueOf(groupDTO.getWorkplace().toUpperCase()));
		}

		try {
			groupDao.persist(groupVisibility);

			if (groupDTO.getIdInterestList() != null && !groupDTO.getIdInterestList().isEmpty()) {
				addInterestToGroup(groupDTO.getIdInterestList(), groupVisibility);
			}

			if (groupDTO.getIdSkillList() != null && !groupDTO.getIdSkillList().isEmpty()) {
				addSkillsToGroup(groupDTO.getIdSkillList(), groupVisibility);
			}

			return 	groupDao.convertEntitytoDTO(groupDao.findAllGroupsFromUser(user.getEmail()));
		} catch (Exception e) {
			return null;
		}
	}

	public boolean addInterestToGroup(List<Integer> listIdiNterest, GroupVisibility groupVisibility) {

		List<Interest> interestList = interestBean.findInterestsbyId(listIdiNterest);
		try {
			for (Interest interest : interestList) {
				groupVisibility.getInterestList().add(interest);
				interest.getGroupListInterest().add(groupVisibility);
				interestDao.merge(interest);
				groupDao.merge(groupVisibility);
			}
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public boolean addSkillsToGroup(List<Integer> listIdSkills, GroupVisibility groupVisibility) {

		List<Skill> skillList = skillBean.findSkillsbyId(listIdSkills);
		try {
			for (Skill skill : skillList) {
				groupVisibility.getSkillList().add(skill);
				skill.getGroupListSkill().add(groupVisibility);
				skillDao.merge(skill);
				groupDao.merge(groupVisibility);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public  List<GroupVisibilityDTO> deleteGroup(int id, User user) {

		GroupVisibility group = groupDao.find(id);
		try {
			if (group != null) {
				group.setDeleted(true);
				groupDao.merge(group);
				return 	groupDao.convertEntitytoDTO(groupDao.findAllGroupsFromUser(user.getEmail()));
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
}
