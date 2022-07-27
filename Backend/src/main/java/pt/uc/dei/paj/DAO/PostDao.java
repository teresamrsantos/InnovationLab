package pt.uc.dei.paj.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import pt.uc.dei.paj.DTO.AnswerDTO;
import pt.uc.dei.paj.DTO.CommentDTO;
import pt.uc.dei.paj.DTO.UserDTO;
import pt.uc.dei.paj.entity.Answer;
import pt.uc.dei.paj.entity.Comment;
import pt.uc.dei.paj.entity.IdeaNecessity;
import pt.uc.dei.paj.entity.Post;
import pt.uc.dei.paj.entity.User;

@Stateless
public class PostDao extends AbstractDao<Post> {
	private static final long serialVersionUID = 1L;
	@Inject
	UserDao userDao;
	@Inject
	IdeaNecessityDao ideaNecessityDao;

	public PostDao() {
		super(Post.class);
	}

	/**
	 * Method that converts Comment of the type DTO to Comment of the type Entity
	 * 
	 * @param commentDto
	 * @param ideaNecessity
	 * @param user
	 * @return Comment
	 */
	public Comment convertDtoToEntity(CommentDTO commentDto, IdeaNecessity ideaNecessity, User user) {
		Comment comment = new Comment();

		if (commentDto.getDescription() != null && !commentDto.getDescription().isEmpty()) {
			comment.setDescription(commentDto.getDescription());
		}

		if (ideaNecessity != null) {
			comment.setPostJoin(ideaNecessity);
		}
		if (user != null) {
			comment.setUserJoinPost(user);
		}
		return comment;
	}

	/**
	 * Method that converts Comment of the type Entity to Comment of the type DTO
	 * 
	 * @param comment
	 * @return CommentDTO
	 */
	public CommentDTO convertEntityToDto(Comment comment) {
		CommentDTO commentDto = new CommentDTO();
		commentDto.setIdPost(comment.getIdPost());
		commentDto.setCreationTime(comment.getCreationTime());
		commentDto.setDescription(comment.getDescription());
		commentDto.setDeletedPost(comment.isDeletedPost());
		UserDTO userJoin = userDao.convertEntitytoDTO(comment.getUserJoinPost());
		commentDto.setUserJoinPost(userJoin);
		commentDto.setIdIdeaNecessity(comment.getPostJoin().getId());
		List<Answer> answerList = comment.getAnswerList();
		List<AnswerDTO> answerDtoList = convertEntityToDtoAnswer(answerList);
		commentDto.setAnswerList(answerDtoList);
		return commentDto;
	}

	/**
	 * Method that converts a Comment list of the type Entity to Comment list of the
	 * type DTO
	 * 
	 * @param commentList
	 * @return CommentDTO List
	 */
	public List<CommentDTO> convertEntityToDto(List<Comment> commentList) {
		List<CommentDTO> commentDtoList = new ArrayList<CommentDTO>();
		for (Comment comment : commentList) {
			commentDtoList.add(convertEntityToDto(comment));
		}
		return commentDtoList;
	}

	/**
	 * Method that converts Answer of the type DTO to Answer of the type Entity
	 * 
	 * @param answerDto
	 * @param comment
	 * @param user
	 * @return Answer
	 */
	public Answer convertDtoToEntity(AnswerDTO answerDto, Comment comment, User user) {
		Answer answer = new Answer();

		if (answerDto.getDescription() != null && !answerDto.getDescription().isEmpty()) {
			answer.setDescription(answerDto.getDescription());
		}
		if (comment != null) {
			answer.setPostJoin(comment.getPostJoin());
		}
		if (comment != null) {
			answer.setAnswersForComment(comment);
		}
		if (user != null) {
			answer.setUserJoinPost(user);
		}
		return answer;
	}

	/**
	 * Method that converts Answer of the type Entity to Answer of the type DTO
	 * 
	 * @param answer
	 * @return AnswerDTO
	 */
	public AnswerDTO convertEntityToDto(Answer answer) {
		AnswerDTO answerDto = new AnswerDTO();
		answerDto.setIdPost(answer.getIdPost());
		answerDto.setCreationTime(answer.getCreationTime());
		answerDto.setDescription(answer.getDescription());
		answerDto.setDeletedPost(answer.isDeletedPost());
		UserDTO userJoin = userDao.convertEntitytoDTO(answer.getUserJoinPost());
		answerDto.setUserJoinPost(userJoin);
		answerDto.setIdIdeaNecessity(answer.getPostJoin().getId());
		answerDto.setIdComment(answer.getAnswersForComment().getIdPost());
		return answerDto;
	}

	/**
	 * Method that converts a Answer list of the type Entity to Answer list of the
	 * type DTO
	 * 
	 * @param answerList
	 * @return AnswerDTO List
	 */
	public List<AnswerDTO> convertEntityToDtoAnswer(List<Answer> answerList) {
		List<AnswerDTO> answerDtoList = new ArrayList<AnswerDTO>();
		for (Answer answer : answerList) {
			answerDtoList.add(convertEntityToDto(answer));
		}
		return answerDtoList;
	}

	/**
	 * Method that returns a list with all Comments And Answes of a IdeaNecessity by id
	 * 
	 * @param id
	 * @return Comment List
	 */
	public List<Comment> findAllCommentsAndAnswesByIdIdeaNecessity(int id) {
		List<Comment> resultList = em.createNamedQuery("Comment.findAllCommentsAndAnswesByIdIdeaNecessity")
				.setParameter("id", id).getResultList();
		return resultList;
	}
}
