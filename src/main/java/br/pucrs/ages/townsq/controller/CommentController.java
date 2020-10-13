package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.Comment;
import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.service.CommentService;
import br.pucrs.ages.townsq.service.QuestionService;
import br.pucrs.ages.townsq.utils.Slugify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CommentController {

    private CommentService commentService;
    private QuestionService questionService;

    @Autowired
    public CommentController(CommentService commentService, QuestionService questionService){
        this.commentService = commentService;
        this.questionService = questionService;
    }

    /**
     * Post route to create a comment
     * @param user User
     * @param comment Comment
     * @param question Question
     * @param referer String
     * @param refererId long
     * @param redirectAttributes RedirectAttributes
     * @return String
     */
    @PostMapping("/comment/create/{referer}/{refererId}")
    public String postCreateComment(@AuthenticationPrincipal User user,
                                    @ModelAttribute Comment comment,
                                    @ModelAttribute Question question,
                                    @PathVariable String referer,
                                    @PathVariable long refererId,
                                    final RedirectAttributes redirectAttributes){
        Question questionOfComment = questionService.getQuestionById(question.getId()).orElse(null);
        if(questionOfComment == null){
            redirectAttributes.addFlashAttribute("error", "Houve um problema na operação.");
            return "redirect:/";
        }
        try{
            commentService.saveComment(comment, user, referer, refererId);
            redirectAttributes.addFlashAttribute("success", "Comentário cadastrado com sucesso.");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Não foi possível cadastrar o comentário.");
        }
        return "redirect:/question/" + questionOfComment.getId() + "/" + Slugify.toSlug(questionOfComment.getTitle());
    }

    /**
     * Get route to delete comment
     * @param user User
     * @param id Long, comment id
     * @param redirectAttributes RedirectAttributes
     * @return String
     */
    @GetMapping("/comment/delete/{id}")
    public String getDeleteComment(@AuthenticationPrincipal User user,
                                   @PathVariable Long id,
                                   final RedirectAttributes redirectAttributes){
        if(commentService.deleteComment(id, user)){
            redirectAttributes.addFlashAttribute("success", "Comentário deletado com sucesso!");
        }
        else{
            redirectAttributes.addFlashAttribute("error", "Não foi possível deletar o comentário.");
        }
        return "redirect:/";
    }

}
