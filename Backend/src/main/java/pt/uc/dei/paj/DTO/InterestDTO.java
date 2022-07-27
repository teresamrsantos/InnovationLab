package pt.uc.dei.paj.DTO;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class InterestDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private int idInterest;
	private Date creationTime;
	private String description;
	private boolean deletedInterest;
	
	public InterestDTO() {
	}

	public int getIdInterest() {
		return idInterest;
	}

	public void setIdInterest(int idInterest) {
		this.idInterest = idInterest;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDeletedInterest() {
		return deletedInterest;
	}

	public void setDeletedInterest(boolean deletedInterest) {
		this.deletedInterest = deletedInterest;
	}

	@Override
	public String toString() {
		return "InterestDTO [idInterest=" + idInterest + ", creationTime=" + creationTime + ", description="
				+ description + ", deletedInterest=" + deletedInterest + "]";
	}
		
}
