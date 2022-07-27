package pt.uc.dei.paj.service;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import pt.uc.dei.paj.DAO.SystemManegementDao;
import pt.uc.dei.paj.DTO.SystemManegementDTO;
import pt.uc.dei.paj.entity.SystemManegement;

@RequestScoped
public class SystemManegementBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@Inject
	SystemManegementDao systemManegementDao;

	/**
	 * Method que add or edit session timeout
	 * @param systemDTO
	 * @return boolean (operation success)
	 */
	public boolean editSessionTimeOut(SystemManegementDTO systemDTO) {
		try {
			List<SystemManegement> systemManegementList = systemManegementDao.findAll();
			if (systemManegementList != null && !systemManegementList.isEmpty() && systemManegementList.size() == 1) {
				SystemManegement system = systemManegementList.get(0);
				system.setSessionTimeOutTime(systemDTO.getSessionTimeOutTime());
				systemManegementDao.merge(system);
				return true;
			} else {
				SystemManegement system = new SystemManegement();
				system.setSessionTimeOutTime(systemDTO.getSessionTimeOutTime());
				systemManegementDao.persist(system);
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Method that get session timeout
	 * @return session timeout
	 */
	public SystemManegementDTO getSessionTimeOut() {
		try {
		SystemManegement systemManegement = systemManegementDao.find(1);
		if (systemManegement != null) {
			SystemManegementDTO systemManegementDTO = new SystemManegementDTO();
			systemManegementDTO.setSessionTimeOutTime(systemManegement.getSessionTimeOutTime());
			return systemManegementDTO;
		}
		return new SystemManegementDTO();
		}catch (Exception e) {
			return null;
		}
	}
}
