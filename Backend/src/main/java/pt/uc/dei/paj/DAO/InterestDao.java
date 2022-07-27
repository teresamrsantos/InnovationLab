package pt.uc.dei.paj.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import pt.uc.dei.paj.DTO.InterestDTO;
import pt.uc.dei.paj.entity.Interest;

@Stateless
public class InterestDao extends AbstractDao<Interest> {
	private static final long serialVersionUID = 1L;

	public InterestDao() {
		super(Interest.class);
	}

	/**
	 * Method that converts interest of the type DTO to interest of the type Entity
	 * 
	 * @param interestDto
	 * @return Interest
	 */
	public Interest convertDtoToEntity(InterestDTO interestDto) {
		Interest interest = new Interest();
		if (interestDto.getDescription() != null && !interestDto.getDescription().isEmpty()) {
			interest.setDescription(interestDto.getDescription().toLowerCase());
		}
		return interest;
	}

	/**
	 * Method that converts a interest list of the type Entity to interest list of the type DTO
	 * 
	 * @param interestList
	 * @return InterestDTO List
	 */
	public List<InterestDTO> convertEntityToDto(List<Interest> interestList) {
		List<InterestDTO> interestDtoList = new ArrayList<InterestDTO>();
		for (Interest interest : interestList) {
			interestDtoList.add(convertEntityToDto(interest));
		}
		return interestDtoList;
	}

	/**
	 * Method that converts interest of the type Entity to interest of the type DTO
	 * 
	 * @param interestDTO
	 * @return InterestDTO
	 */
	public InterestDTO convertEntityToDto(Interest interest) {
		InterestDTO interestDTO = new InterestDTO();
		interestDTO.setIdInterest(interest.getIdInterest());
		interestDTO.setDescription(interest.getDescription());
		interestDTO.setCreationTime(interest.getCreationTime());
		interestDTO.setDeletedInterest(interest.isDeletedInterest());
		return interestDTO;
	}

	/**
	 * Method that returns interests from the DB that contains exactly the description word
	 * 
	 * @param description
	 * @return Interest List
	 */
	public List<Interest> findInterestByDescription(String description) {
		List<Interest> resultList = em.createNamedQuery("Interest.findInterestByDescription")
				.setParameter("description", description).getResultList();
		return resultList;
	}

	/**
	 * Method that returns interests from the DB that contains the description of the word search
	 * 
	 * @param description
	 * @return Interest List
	 */
	public List<Interest> findInterestsbyWordSearch(String description) {
		List<Interest> resultList = em.createNamedQuery("Interest.findInterestsbyWordSearch")
				.setParameter("description", description).getResultList();
		return resultList;
	}
}
