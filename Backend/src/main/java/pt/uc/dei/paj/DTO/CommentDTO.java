package pt.uc.dei.paj.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CommentDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private int idPost;
	private Date creationTime;
	private String description;
	private boolean deletedPost;
	private UserDTO userJoinPost;
	private int idIdeaNecessity;
	private List<AnswerDTO> answerList = new ArrayList<AnswerDTO>();

	public CommentDTO() {
	}

	public int getIdPost() {
		return idPost;
	}

	public void setIdPost(int idPost) {
		this.idPost = idPost;
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

	public boolean isDeletedPost() {
		return deletedPost;
	}

	public void setDeletedPost(boolean deletedPost) {
		this.deletedPost = deletedPost;
	}

	public UserDTO getUserJoinPost() {
		return userJoinPost;
	}

	public void setUserJoinPost(UserDTO userJoinPost) {
		this.userJoinPost = userJoinPost;
	}

	public int getIdIdeaNecessity() {
		return idIdeaNecessity;
	}

	public void setIdIdeaNecessity(int idIdeaNecessity) {
		this.idIdeaNecessity = idIdeaNecessity;
	}

	public List<AnswerDTO> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<AnswerDTO> answerList) {
		this.answerList = answerList;
	}

	@Override
	public String toString() {
		return "CommentDTO [idPost=" + idPost + ", creationTime=" + creationTime + ", description=" + description
				+ ", deletedPost=" + deletedPost + ", userJoinPost=" + userJoinPost + ", idIdeaNecessity="
				+ idIdeaNecessity + ", answerList=" + answerList + "]";
	}

}
