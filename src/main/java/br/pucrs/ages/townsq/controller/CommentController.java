package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    /**
     * Post route to create a comment
     * @return String
     */
    @PostMapping("/comment/create")
    public String postCreateComment(){
        // calls commentService.save(comment)
        return "";
    }

}
