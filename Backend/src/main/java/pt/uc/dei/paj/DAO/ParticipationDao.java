package pt.uc.dei.paj.DAO;

import java.util.List;

import javax.ejb.Stateless;

import pt.uc.dei.paj.DTO.ParticipationDTO;
import pt.uc.dei.paj.entity.Participation;
import pt.uc.dei.paj.general.SaveFile;

@Stateless
public class ParticipationDao extends AbstractDao<Participation> {
	private static final long serialVersionUID = 1L;

	public ParticipationDao() {
		super(Participation.class);
	}

	public Long countParticipationsFromProject(int idProject) {
		try {
			Long count = (Long) em.createNamedQuery("Participation.countProjecParticipation")
					.setParameter("idProject", idProject).getSingleResult();

			return count;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Participation> participationFromUserAndFromProject(int idProject, String email) {
		try {
			List<Participation> participation = em.createNamedQuery("Participation.userAlredyMember")
					.setParameter("idProject", idProject).setParameter("email", email).getResultList();
			return participation;
		} catch (Exception e) {
			return null;
		}
	}

	
	public List<Participation> checkIfUserUsedToParticipateInThisProject(int idProject, String email) {
		try {
			List<Participation> participation = em.createNamedQuery("Participation.checkIfUserUsedToParticipateInThisProject")
					.setParameter("idProject", idProject).setParameter("email", email).getResultList();
			return participation;
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Participation> participationsUserProjectActive(String email) {
		List<Participation> resultList = em.createNamedQuery("Participation.participationsUserProjectActive")
				.setParameter("email", email).getResultList();
		return resultList;
	}

	public List<Participation> pendingParticipationsFromAProject(int idProject) {
		List<Participation> resultList = em.createNamedQuery("Participation.pendingParticipationsFromAProject")
				.setParameter("idProject", idProject).getResultList();
		return resultList;
	}

	public List<Participation> pendingInvitesFromAProject(int idProject) {
		List<Participation> resultList = em.createNamedQuery("Participation.pendingInvitesFromAProject")
				.setParameter("idProject", idProject).getResultList();
		return resultList;
	}

	
	public List<Participation> pendingParticipationsFromAUserInvites(String email) {
		List<Participation> resultList = em.createNamedQuery("Participation.pendingParticipationsFromAUser")
				.setParameter("email", email).getResultList();
		return resultList;
	}

	
	public List<Participation> pendingParticipationsFromAUserRequests(String email) {
		List<Participation> resultList = em.createNamedQuery("Participation.pendingParticipationsFromAUserRequests")
				.setParameter("email", email).getResultList();
		return resultList;
	}

	
	public List<Participation> verifyIfTheUserIsMemberOfAProject(String email, int idProject) {
		List<Participation> resultList = em.createNamedQuery("Participation.verifyIfTheUserIsMemberOfAProject")
				.setParameter("email", email).setParameter("idProject", idProject).getResultList();
		return resultList;
	}
	
	
	
	public List<Participation> participationsUserCertainProject(String email, int idProject) {
		List<Participation> resultList = em.createNamedQuery("Participation.participationsUserCertainProject")
				.setParameter("email", email).setParameter("idProject", idProject).getResultList();
		return resultList;
	}
	
	public List<Participation> listOfMembersAndAdminsProject( int idProject) {
		List<Participation> resultList = em.createNamedQuery("Participation.projectParticipation")
				.setParameter("idProject", idProject).getResultList();
		return resultList;
	}
	

	public ParticipationDTO convertEntityToDTO(Participation participationEntity) {

		ParticipationDTO participationDTO = new ParticipationDTO();
		participationDTO.setUsernameMember(participationEntity.getUserParticipation().getFirstName() +" "
				+ participationEntity.getUserParticipation().getLastName());
		participationDTO.setEmailMember(participationEntity.getUserParticipation().getEmail());
		if(participationEntity.getUserParticipation().getPictureUrl()!=null) {
		participationDTO.setPictureUser(
				SaveFile.convertFileToFrontEnd(participationEntity.getUserParticipation().getPictureUrl()));}
		participationDTO.setIdMember(participationEntity.getUserParticipation().getIdUser());
		
		participationDTO.setRole(participationEntity.getRole().toString());
		participationDTO.setIdProject(participationEntity.getProjectParticipation().getIdProject());
		return participationDTO;
	}

	@Override
	public String getIdColumn(String string) {
		switch (string) {
		case "id":
			return "idMember";
		case "deleted":
			return "deletedParticipation";
		default:
			throw new IllegalArgumentException("Invalid data");

		}
	}

}
