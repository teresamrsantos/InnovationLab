package pt.uc.dei.paj.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;

import pt.uc.dei.paj.DAO.IdeaNecessityDao;
import pt.uc.dei.paj.DAO.InterestDao;
import pt.uc.dei.paj.DAO.ProjectDao;
import pt.uc.dei.paj.DAO.UserDao;
import pt.uc.dei.paj.DTO.InterestDTO;
import pt.uc.dei.paj.entity.Interest;

@RequestScoped
public class InterestBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	UserDao userDao;

	@Inject
	InterestDao interestDao;

	@Inject
	IdeaNecessityDao ideaNecessityDao;

	@Inject
	ProjectDao projectDao;

	/**
	 * Method to add a interest
	 * 
	 * @param interestDTO object add
	 * @return sucess of the operation
	 */
	public Interest addInterest(InterestDTO interestDTO) {
		try {
			Interest interest = interestDao.convertDtoToEntity(interestDTO);
			interestDao.persist(interest);
			return interest;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that validates if there is interest with the description you want to
	 * add
	 * 
	 * @param description
	 * @return true if there is an equal description and false if not
	 */
	public boolean validateDescriptionInterest(String description) {
		try {
			List<Interest> interestsList = interestDao.findInterestByDescription(description.toLowerCase());
			return (interestsList.isEmpty() || interestsList == null) ? false : true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Method to find all interest
	 * 
	 * @return list of interests
	 */
	public List<InterestDTO> findAllInterest() {
		try {
			List<Interest> interestList = interestDao.findAll();
			if (!interestList.isEmpty() && interestList.get(0) != null) {
				return interestDao.convertEntityToDto(interestList);
			}
			return new ArrayList<InterestDTO>();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to find a interest by Id
	 * 
	 * @param idInterest that identifies the interest
	 * @return InterestDTO or null
	 */
	public InterestDTO findInterestById(int idInterest) {
		try {
			Interest interest = interestDao.find(idInterest);
			if (interest != null) {
				return interestDao.convertEntityToDto(interest);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to find a interest by word or expression to search all interests that
	 * start with that word
	 * 
	 * @param WordSearch is word or expression
	 * @return list of interests
	 */
	public List<InterestDTO> findInterestsbyWordSearch(String WordSearch) {
		try {
			List<Interest> skillList = interestDao.findInterestsbyWordSearch(WordSearch.toLowerCase());
			if (!skillList.isEmpty() && skillList.get(0) != null) {
				return interestDao.convertEntityToDto(skillList);
			}
			return new ArrayList<InterestDTO>();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to find a interest by id
	 * 
	 * @param WordSearch is word or expression
	 * @return list of interests
	 */
	public List<Interest> findInterestsbyId(List<Integer> idInterestList) {
		try {
			List<Interest> listToReturn = new ArrayList<Interest>();
			for (Integer interestId : idInterestList) {
				Interest interest = interestDao.find(interestId);
				if (interest != null) {
					listToReturn.add(interest);
				}
			}
			return listToReturn;

		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Method that returns a array object of interest with all the information necessary to select box
	 * 
	 * @return JSONArray
	 */
	public JSONArray allInterestSelect() {
		try {
			List<Interest> interestList = interestDao.findAll();
			JSONArray jsonArray = new JSONArray();
			for (Interest interest : interestList) {
				JSONObject json = new JSONObject();
				json.put("label", interest.getDescription());
				json.put("value", interest.getDescription());
				json.put("id", interest.getIdInterest());
				jsonArray.put(json);
			}
			return jsonArray;
		} catch (Exception e) {
			return null;
		}
	}
}
