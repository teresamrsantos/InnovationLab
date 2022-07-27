package pt.uc.dei.paj.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@NamedQueries({
	@NamedQuery(name = "Comment.findAllCommentsAndAnswesByIdIdeaNecessity", query = "SELECT c FROM Comment c WHERE c.postJoin.id=:id ORDER BY c.creationTime ASC"),
})
public class Comment extends Post implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy="answersForComment")
	private List <Answer> answerList = new ArrayList<Answer>();

	public List<Answer> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<Answer> answerList) {
		this.answerList = answerList;
	}

	@Override
	public String toString() {
		return "Comment [answerList=" + answerList + "]";
	}
	
}
