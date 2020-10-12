package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.Comment;
import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @PostMapping("/comment/create/{referer}/{referer_id}")
    public String postCreateComment(@ModelAttribute Comment comment,
                                    @ModelAttribute Question question,
                                    @PathVariable String referer,
                                    @PathVariable long referer_id,
                                    final RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("success", "Comentário cadastrado com sucesso!");
        return "redirect:/question/" + question.getId();
    }

    @GetMapping("/comment/delete/{id}")
    public String getDeleteComment(@PathVariable Long id){
        //call service to delete comment
        //redirect to question page
        return "/";
    }

}
