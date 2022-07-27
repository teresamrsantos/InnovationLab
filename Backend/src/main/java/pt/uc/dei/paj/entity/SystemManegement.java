package pt.uc.dei.paj.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SystemManegement")
public class SystemManegement implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idSystem;
	private int sessionTimeOutTime;

	public int getIdSystem() {
		return idSystem;
	}

	public void setIdSystem(int idSystem) {
		this.idSystem = idSystem;
	}

	public int getSessionTimeOutTime() {
		return sessionTimeOutTime;
	}

	public void setSessionTimeOutTime(int sessionTimeOutTime) {
		this.sessionTimeOutTime = sessionTimeOutTime;
	}

	@Override
	public String toString() {
		return "SystemManegement [idSystem=" + idSystem + ", sessionTimeOutTime=" + sessionTimeOutTime + "]";
	}

}