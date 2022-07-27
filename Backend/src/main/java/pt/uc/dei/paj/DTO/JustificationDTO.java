package pt.uc.dei.paj.DTO;

import java.io.Serializable;
import java.util.Date;

import pt.uc.dei.paj.general.IdeaOrNecessity;

public class JustificationDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String description;
	private Date creationTime;
	private String author;
	private int idAuthor;
	private int ideaId1;
	private String authorIdeaId1;
	private String titleId1;
	private IdeaOrNecessity ideaOrNecessityId1;
	private boolean deletedIdeaNecessity1;
	private int ideaId2;
	private String authorIdeaId2;
	private String titleId2;
	private IdeaOrNecessity ideaOrNecessityId2;
	private boolean deletedIdeaNecessity2;

	public JustificationDTO() {

	}

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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getIdeaId1() {
		return ideaId1;
	}

	public void setIdeaId1(int ideaId1) {
		this.ideaId1 = ideaId1;
	}

	public int getIdeaId2() {
		return ideaId2;
	}

	public void setIdeaId2(int ideaId2) {
		this.ideaId2 = ideaId2;
	}
	
	public int getIdAuthor() {
		return idAuthor;
	}

	public void setIdAuthor(int idAuthor) {
		this.idAuthor = idAuthor;
	}

	public String getAuthorIdeaId1() {
		return authorIdeaId1;
	}

	public void setAuthorIdeaId1(String authorIdeaId1) {
		this.authorIdeaId1 = authorIdeaId1;
	}

	public IdeaOrNecessity getIdeaOrNecessityId1() {
		return ideaOrNecessityId1;
	}

	public void setIdeaOrNecessityId1(IdeaOrNecessity ideaOrNecessityId1) {
		this.ideaOrNecessityId1 = ideaOrNecessityId1;
	}

	public String getAuthorIdeaId2() {
		return authorIdeaId2;
	}

	public void setAuthorIdeaId2(String authorIdeaId2) {
		this.authorIdeaId2 = authorIdeaId2;
	}

	public IdeaOrNecessity getIdeaOrNecessityId2() {
		return ideaOrNecessityId2;
	}

	public void setIdeaOrNecessityId2(IdeaOrNecessity ideaOrNecessityId2) {
		this.ideaOrNecessityId2 = ideaOrNecessityId2;
	}
	
	public String getTitleId1() {
		return titleId1;
	}

	public void setTitleId1(String titleId1) {
		this.titleId1 = titleId1;
	}

	public String getTitleId2() {
		return titleId2;
	}

	public void setTitleId2(String titleId2) {
		this.titleId2 = titleId2;
	}
	
	public boolean isDeletedIdeaNecessity1() {
		return deletedIdeaNecessity1;
	}

	public void setDeletedIdeaNecessity1(boolean deletedIdeaNecessity1) {
		this.deletedIdeaNecessity1 = deletedIdeaNecessity1;
	}

	public boolean isDeletedIdeaNecessity2() {
		return deletedIdeaNecessity2;
	}

	public void setDeletedIdeaNecessity2(boolean deletedIdeaNecessity2) {
		this.deletedIdeaNecessity2 = deletedIdeaNecessity2;
	}

	@Override
	public String toString() {
		return "JustificationDTO [description=" + description + ", creationTime=" + creationTime + ", author=" + author
				+ ", idAuthor=" + idAuthor + ", ideaId1=" + ideaId1 + ", authorIdeaId1=" + authorIdeaId1 + ", titleId1="
				+ titleId1 + ", ideaOrNecessityId1=" + ideaOrNecessityId1 + ", deletedIdeaNecessity1="
				+ deletedIdeaNecessity1 + ", ideaId2=" + ideaId2 + ", authorIdeaId2=" + authorIdeaId2 + ", titleId2="
				+ titleId2 + ", ideaOrNecessityId2=" + ideaOrNecessityId2 + ", deletedIdeaNecessity2="
				+ deletedIdeaNecessity2 + "]";
	}

}
