package pt.uc.dei.paj.DTO;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SystemManegementDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private int sessionTimeOutTime;

	public int getSessionTimeOutTime() {
		return sessionTimeOutTime;
	}

	public void setSessionTimeOutTime(int sessionTimeOutTime) {
		this.sessionTimeOutTime = sessionTimeOutTime;
	}

	@Override
	public String toString() {
		return "SystemManegementDTO [sessionTimeOutTime=" + sessionTimeOutTime + "]";
	}


}
