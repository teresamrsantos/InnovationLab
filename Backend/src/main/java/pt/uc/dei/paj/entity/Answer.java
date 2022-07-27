package pt.uc.dei.paj.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Answer extends Post implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	private Comment answersForComment;

	public Comment getAnswersForComment() {
		return answersForComment;
	}

	public void setAnswersForComment(Comment answersForComment) {
		this.answersForComment = answersForComment;
	}

	@Override
	public String toString() {
		return "Answer [answersForComment=" + answersForComment + "]";
	}
	
}
