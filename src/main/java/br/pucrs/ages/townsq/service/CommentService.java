package br.pucrs.ages.townsq.service;

import br.pucrs.ages.townsq.model.Comment;
import br.pucrs.ages.townsq.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    /**
     * Saves a Comment on the database
     * @param c Comment
     * @return Comment
     */
    public Comment saveComment(Comment c){
        return this.commentRepository.save(c);
    }

}
