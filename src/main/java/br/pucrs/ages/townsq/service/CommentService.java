package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Comment;
import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private QuestionService questionService;

    @Autowired
    public CommentService(CommentRepository commentRepository, QuestionService questionService){
        this.commentRepository = commentRepository;
        this.questionService = questionService;
    }

    /**
     * Saves a Comment on the database
     * @param comment Comment
     * @param user User
     * @param referer String
     * @param refererId long
     * @return Comment
     */
    public Comment saveComment(Comment comment,
                               User user,
                               String referer,
                               long refererId) throws IllegalArgumentException{
        if(StringUtils.isEmpty(comment.getText()))
            throw new IllegalArgumentException("O texto do comentário não pode estar vazio.");
        if(referer.equals("question")){
            Question question = questionService.getQuestionById(refererId).orElse(null);
            if(question == null) throw new IllegalArgumentException("Pergunta não encontrada");
            comment.setQuestion(question);
            comment.setAnswer(null);
            comment.setUser(user);
        }
        return commentRepository.save(comment);
    }

}
