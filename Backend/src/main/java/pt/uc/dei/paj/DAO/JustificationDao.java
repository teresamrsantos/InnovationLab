package pt.uc.dei.paj.DAO;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

import pt.uc.dei.paj.DTO.JustificationDTO;
import pt.uc.dei.paj.entity.Justification;

@Stateless
public class JustificationDao extends AbstractDao<Justification> {
	private static final long serialVersionUID = 1L;

	public JustificationDao() {
		super(Justification.class);
	}

	/**
	 * Method that converts list justification of the type DTO to justification list of the type Entity
	 * 
	 * @param interestDto
	 * @return Interest
	 */
	public List<JustificationDTO> convertEntityToDto(List<Justification> justificationlList) {
		List<JustificationDTO> justificationDtoList = new ArrayList<JustificationDTO>();
		for (Justification justification : justificationlList) {
			JustificationDTO justificationDto = new JustificationDTO();
			justificationDto.setDescription(justification.getDescription());
			justificationDto.setCreationTime(justification.getCreationTime());
			justificationDto.setAuthor(justification.getPrimaryKey().getUserJoinJustification().getFirstName()+" "+justification.getPrimaryKey().getUserJoinJustification().getLastName());
			justificationDto.setIdeaId1(justification.getPrimaryKey().getIdeaId1().getId());
			justificationDto.setAuthorIdeaId1(justification.getPrimaryKey().getIdeaId1().getUserJoinIdeaNecessity().getFirstName()+" "+
			justification.getPrimaryKey().getIdeaId1().getUserJoinIdeaNecessity().getLastName());
			justificationDto.setIdeaOrNecessityId1(justification.getPrimaryKey().getIdeaId1().getIdeaOrNecessity());
			justificationDto.setTitleId1(justification.getPrimaryKey().getIdeaId1().getTitle());
			justificationDto.setDeletedIdeaNecessity1(justification.getPrimaryKey().getIdeaId1().isDeletedIdeaNecessity());
			justificationDto.setIdeaId2(justification.getPrimaryKey().getIdeaId2().getId());
			justificationDto.setAuthorIdeaId2(justification.getPrimaryKey().getIdeaId2().getUserJoinIdeaNecessity().getFirstName()+" "+
			justification.getPrimaryKey().getIdeaId2().getUserJoinIdeaNecessity().getLastName());
			justificationDto.setIdeaOrNecessityId2(justification.getPrimaryKey().getIdeaId2().getIdeaOrNecessity());
			justificationDto.setTitleId2(justification.getPrimaryKey().getIdeaId2().getTitle());
			justificationDto.setDeletedIdeaNecessity2(justification.getPrimaryKey().getIdeaId2().isDeletedIdeaNecessity());
			justificationDtoList.add(justificationDto);
		}
		return justificationDtoList;
	}

	/**
	 * Method that check if idIdeaNecessity is Associate other id IdeaNecessity
	 * @param ideaId1
	 * @param ideaId2
	 * @param email
	 * @return justification list
	 */
	public List<Justification> checkIfIdIdeaNecessityIsAssociateOtherIdIdeaNecessity(int ideaId1, int ideaId2, String email) {
		List<Justification> resultList = em.createNamedQuery("Justification.checkIfIdIdeaNecessityIsAssociateOtherIdIdeaNecessity")
				.setParameter("ideaId1", ideaId1)
				.setParameter("ideaId2", ideaId2).getResultList();
		return resultList;
	}
	
	/**
	 * Method that return all ids IdeaNecessities associate with a Id IdeaNecessity1
	 * @param ideaId1
	 * @return list of id ideaNecessity
	 */
	public List<Integer> AllIdIdeaNecessityAssociateOtherIdIdeaNecessity1(int ideaId1) {
		List<Integer> resultList = em.createNamedQuery("Justification.AllIdIdeaNecessityAssociateOtherIdIdeaNecessity1")
				.setParameter("ideaId1", ideaId1).getResultList();
		return resultList;
	}
	
	/**
	 * Method that return all ids IdeaNecessities associate with a Id IdeaNecessity2
	 * @param ideaId1
	 * @return list of id ideaNecessity
	 */
	public List<Integer> AllIdIdeaNecessityAssociateOtherIdIdeaNecessity2(int ideaId2) {
		List<Integer> resultList = em.createNamedQuery("Justification.AllIdIdeaNecessityAssociateOtherIdIdeaNecessity2")
				.setParameter("ideaId2", ideaId2).getResultList();
		return resultList;
	}
}
