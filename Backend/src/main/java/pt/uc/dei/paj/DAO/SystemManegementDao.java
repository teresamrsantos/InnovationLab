package pt.uc.dei.paj.DAO;

import javax.ejb.Stateless;
import pt.uc.dei.paj.entity.SystemManegement;

@Stateless
public class SystemManegementDao extends AbstractDao<SystemManegement> {
	private static final long serialVersionUID = 1L;

	public SystemManegementDao() {
		super(SystemManegement.class);
	}

}
