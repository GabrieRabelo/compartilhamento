package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.model.Comment;
import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final QuestionService questionService;
    private final AnswerService answerService;

    @Autowired
    public CommentService(CommentRepository commentRepository, QuestionService questionService, AnswerService answerService){
        this.commentRepository = commentRepository;
        this.questionService = questionService;
        this.answerService = answerService;
    }

    /**
     * Saves a Comment on the database
     * @param comment Comment
     * @param user User
     * @param creator String
     * @param creatorId long
     * @return Comment
     */
    public Comment saveComment(Comment comment,
                               User user,
                               String creator,
                               long creatorId) throws IllegalArgumentException{
        if(StringUtils.isEmpty(comment.getText()))
            throw new IllegalArgumentException("O texto do comentário não pode estar vazio.");

        comment.setUser(user);
        if(!creator.equals("question") && !creator.equals("answer"))
            throw new IllegalArgumentException("O tipo do objeto criador é inválido.");
        if(creator.equals("question")){
            Question question = questionService.getQuestionById(creatorId).orElse(null);
            if(question == null) throw new IllegalArgumentException("Pergunta referente ao comentário não encontrada");
            comment.setQuestion(question);
            comment.setAnswer(null);
        }
        else {
            Answer answer = answerService.findById(creatorId).orElse(null);
            if(answer == null) throw new IllegalArgumentException("Resposta referente ao comentário não encontrada");
            comment.setQuestion(null);
            comment.setAnswer(answer);
        }
        return commentRepository.save(comment);
    }

    /**
     * Edits a comment.
     * @param comment String (comment text)
     * @param user User
     * @param id Long comment id
     * @return Comment
     */
    public Comment editComment(String comment,
                               User user,
                               Long id){
        Comment databaseComment = commentRepository.findById(id).orElse(null);
        if(StringUtils.isEmpty(comment.trim()) || databaseComment == null || !databaseComment.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("Não foi possível editar o comentário.");
        }
        databaseComment.setText(comment);
        return commentRepository.save(databaseComment);
    }


    /**
     * Deletes a comment (soft delete)
     * @param commentId long
     * @param user User
     * @return boolean
     */
    public boolean deleteComment(long commentId, User user){
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if(comment != null && comment.getUser().getId().equals(user.getId())){
            comment.setIsActive(0);
            commentRepository.save(comment);
            return true;
        }
        return false;
    }

}
