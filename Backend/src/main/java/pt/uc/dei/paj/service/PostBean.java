package pt.uc.dei.paj.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import pt.uc.dei.paj.DAO.IdeaNecessityDao;
import pt.uc.dei.paj.DAO.PostDao;
import pt.uc.dei.paj.DAO.UserDao;
import pt.uc.dei.paj.DTO.AnswerDTO;
import pt.uc.dei.paj.DTO.CommentDTO;
import pt.uc.dei.paj.entity.Answer;
import pt.uc.dei.paj.entity.Comment;
import pt.uc.dei.paj.entity.IdeaNecessity;
import pt.uc.dei.paj.entity.Post;
import pt.uc.dei.paj.entity.User;

@RequestScoped
public class PostBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@Inject
	UserDao userDao;
	@Inject
	PostDao postDao;
	@Inject
	IdeaNecessityDao ideaNecessityDao;

	/**
	 * Method that adds a Comment to an IdeaNecessity
	 * 
	 * @param commentDTO
	 * @param user
	 * @return operation success
	 */
	public CommentDTO addComment(CommentDTO commentDTO, User user) {
		try {
			IdeaNecessity ideaNecessity = ideaNecessityDao.find(commentDTO.getIdIdeaNecessity());
			if (ideaNecessity != null) {
				Comment comment = postDao.convertDtoToEntity(commentDTO, ideaNecessity, user);
				postDao.persist(comment);
				return postDao.convertEntityToDto(comment);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that adds a Answer to an IdeaNecessity
	 * 
	 * @param answerDTO
	 * @param user
	 * @return operation success
	 */
	public AnswerDTO addAnswer(AnswerDTO answerDTO, User user) {
		try {
			Post post = postDao.find(answerDTO.getIdComment());
			if (post != null && post instanceof Comment) {
				Comment comment = (Comment) post;
				Answer answer = postDao.convertDtoToEntity(answerDTO, comment, user);
				postDao.persist(answer);
				return postDao.convertEntityToDto(answer);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method that finds all Comments and Answes of a IdeaNecessity by id
	 * 
	 * @param id
	 * @return CommentDTO List
	 */
	public List<CommentDTO> findAllCommentsAndAnswesByIdIdeaNecessity(int id) {
		try {
			List<Comment> commentList = postDao.findAllCommentsAndAnswesByIdIdeaNecessity(id);
			if (!commentList.isEmpty() && commentList.get(0) != null) {
				return postDao.convertEntityToDto(commentList);
			}
			return new ArrayList<CommentDTO>();
		} catch (Exception e) {
			return null;
		}
	}
}
