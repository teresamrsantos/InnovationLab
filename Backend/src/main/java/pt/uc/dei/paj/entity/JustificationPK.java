package pt.uc.dei.paj.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class JustificationPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private User userJoinJustification;

	@ManyToOne
	private IdeaNecessity ideaId1;

	@ManyToOne
	private IdeaNecessity ideaId2;

	public User getUserJoinJustification() {
		return userJoinJustification;
	}

	public void setUserJoinJustification(User userJoinJustification) {
		this.userJoinJustification = userJoinJustification;
	}

	public IdeaNecessity getIdeaId1() {
		return ideaId1;
	}

	public void setIdeaId1(IdeaNecessity ideaId1) {
		this.ideaId1 = ideaId1;
	}

	public IdeaNecessity getIdeaId2() {
		return ideaId2;
	}

	public void setIdeaId2(IdeaNecessity ideaId2) {
		this.ideaId2 = ideaId2;
	}

	@Override
	public String toString() {
		return "JustificationPK [userJoinJustification=" + userJoinJustification + ", ideaId1=" + ideaId1 + ", ideaId2="
				+ ideaId2 + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(ideaId1, ideaId2, userJoinJustification);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JustificationPK other = (JustificationPK) obj;
		return Objects.equals(ideaId1, other.ideaId1) && Objects.equals(ideaId2, other.ideaId2)
				&& Objects.equals(userJoinJustification, other.userJoinJustification);
	}
	
}
