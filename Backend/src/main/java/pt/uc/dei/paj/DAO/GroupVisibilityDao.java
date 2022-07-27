package pt.uc.dei.paj.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NamedQuery;

import pt.uc.dei.paj.DTO.GroupVisibilityDTO;
import pt.uc.dei.paj.entity.GroupVisibility;
import pt.uc.dei.paj.entity.Interest;
import pt.uc.dei.paj.entity.Skill;
import pt.uc.dei.paj.entity.User;
import pt.uc.dei.paj.general.Workplace;

@Stateless
public class GroupVisibilityDao extends AbstractDao<GroupVisibility> {
	private static final long serialVersionUID = 1L;

	@Inject
	SkillDao skillDao;
	@Inject
	InterestDao interestDao;

	public GroupVisibilityDao() {
		super(GroupVisibility.class);
	}

	public GroupVisibility convertDTOToEntity(GroupVisibilityDTO groupDTO) {

		if (groupDTO.getIdSkillList() != null && !groupDTO.getIdSkillList().isEmpty()) {

		}
		return null;

	}

	public List<GroupVisibilityDTO> convertEntitytoDTO(List<GroupVisibility> group) {

		List<GroupVisibilityDTO> listToReturn = new ArrayList<GroupVisibilityDTO>();
		for (GroupVisibility groupVisibility : group) {
			if (!groupVisibility.isDeleted()) {
				listToReturn.add(convertEntitytoDTO(groupVisibility));
			}
		}

		return listToReturn;
	}

	public GroupVisibilityDTO convertEntitytoDTO(GroupVisibility group) {
		GroupVisibilityDTO groupDTO = new GroupVisibilityDTO();

		if (group.getSkillList() != null && !group.getSkillList().isEmpty()) {
			for (Skill skill : group.getSkillList()) {
				groupDTO.getSkillDTOList().add(skillDao.convertEntityToDto(skill));
			}
		}

		if (group.getInterestList() != null && !group.getInterestList().isEmpty()) {
			for (Interest interest : group.getInterestList()) {
				groupDTO.getInterestDTOList().add(interestDao.convertEntityToDto(interest));
			}
		}

		groupDTO.setIdGroup(group.getIdGroup());
		groupDTO.setUsernameUser(group.getUserGroup().getUsername());
		if (group.getWorkplace() != null) {
			groupDTO.setWorkplace(group.getWorkplace().toString());
		}
		return groupDTO;

	}

	public List<User> findUsersgroupVisibilityInterestSkillWorkPlace(List<Integer> idSkills, List<Integer> idInterests,
			Workplace workplace) {
		List<User> resultList = em.createNamedQuery("User.groupVisibilityInterestSkillWorkPlace")
				.setParameter("idSkillList", idSkills).setParameter("idInterestList", idInterests)
				.setParameter("workplace", workplace).getResultList();
		return resultList;
	}

	public List<User> findUsersgroupVisibilityWorkPlace(Workplace workplace) {
		List<User> resultList = em.createNamedQuery("User.groupVisibilityWorkPlace")
				.setParameter("workplace", workplace).getResultList();
		return resultList;
	}

	public List<User> findUsersgroupVisibilityInterest(List<Integer> idSkills) {
		List<User> resultList = em.createNamedQuery("User.groupVisibilityInterest")
				.setParameter("idInterestList", idSkills).getResultList();
		return resultList;
	}

	public List<User> findUsersgroupVisibilitySkill(List<Integer> idInterests) {
		List<User> resultList = em.createNamedQuery("User.groupVisibilitySkill")
				.setParameter("idSkillList", idInterests).getResultList();
		return resultList;
	}

	public List<User> findUsersgroupVisibilityInterestSkill(List<Integer> idSkills, List<Integer> idInterests) {
		List<User> resultList = em.createNamedQuery("User.groupVisibilityInterestSkill")
				.setParameter("idSkillList", idSkills).setParameter("idInterestList", idInterests)

				.getResultList();
		return resultList;
	}

	public List<User> findUsersgroupVisibilityInterestWorkPlace(List<Integer> idInterests, Workplace workplace) {
		List<User> resultList = em.createNamedQuery("User.groupVisibilityInterestWorkPlace")
				.setParameter("idInterestList", idInterests).setParameter("workplace", workplace).getResultList();
		return resultList;
	}

	public List<User> findUsersgroupVisibilitySkillWorkPlace(List<Integer> idSkills, Workplace workplace) {
		List<User> resultList = em.createNamedQuery("User.groupVisibilitySkillWorkPlace")
				.setParameter("idSkillList", idSkills).setParameter("workplace", workplace).getResultList();
		return resultList;
	}

	public List<GroupVisibility> findAllGroupsFromUser(String email) {
		List<GroupVisibility> resultList = em.createNamedQuery("GroupVisibility.findAllGroupsFromUser")
				.setParameter("email", email).getResultList();
		return resultList;
	}

}
