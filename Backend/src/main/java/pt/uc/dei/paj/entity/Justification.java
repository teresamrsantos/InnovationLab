package pt.uc.dei.paj.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity

@NamedQueries({
	@NamedQuery(name = "Justification.checkIfIdIdeaNecessityIsAssociateOtherIdIdeaNecessity", query = "SELECT p FROM Justification p WHERE p.primaryKey.ideaId1.id=:ideaId1 AND p.primaryKey.ideaId2.id=:ideaId2 OR  p.primaryKey.ideaId1.id=:ideaId2 AND p.primaryKey.ideaId2.id=:ideaId1"),
	@NamedQuery(name = "Justification.AllIdIdeaNecessityAssociateOtherIdIdeaNecessity1", query = "SELECT p.primaryKey.ideaId2.id FROM Justification p WHERE p.primaryKey.ideaId1.id=:ideaId1"),
	@NamedQuery(name = "Justification.AllIdIdeaNecessityAssociateOtherIdIdeaNecessity2", query = "SELECT p.primaryKey.ideaId1.id FROM Justification p WHERE p.primaryKey.ideaId2.id=:ideaId2"),
})

public class Justification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "Description")
	private String description;

	@CreationTimestamp
	@Column(name = "Created_at", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	
	// https://stackoverflow.com/questions/12106124/eclipse-error-on-mapping-with-embeddedid
	@EmbeddedId
	private JustificationPK primaryKey;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public JustificationPK getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(JustificationPK primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Override
	public String toString() {
		return "Justification [description=" + description + ", creationTime=" + creationTime + ", primaryKey="
				+ primaryKey + "]";
	}

}
