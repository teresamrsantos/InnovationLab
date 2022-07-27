package pt.uc.dei.paj.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public abstract class Post implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idPost;

	@CreationTimestamp
	@Column(name = "Created_at", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;

	@Column(name = "Description")
	private String description;

	@Column(name = "deletedPost")
	private boolean deletedPost;

	@ManyToOne
	private User userJoinPost;

	@ManyToOne
	private IdeaNecessity postJoin;

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

	public User getUserJoinPost() {
		return userJoinPost;
	}

	public void setUserJoinPost(User userJoinPost) {
		this.userJoinPost = userJoinPost;
	}

	public IdeaNecessity getPostJoin() {
		return postJoin;
	}

	public void setPostJoin(IdeaNecessity postJoin) {
		this.postJoin = postJoin;
	}

	@Override
	public String toString() {
		return "Post [idPost=" + idPost + ", creationTime=" + creationTime + ", description=" + description
				+ ", deletedPost=" + deletedPost + ", userJoinPost=" + userJoinPost + ", postJoin=" + postJoin + "]";
	}

}
