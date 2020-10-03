package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.service.QuestionService;
import br.pucrs.ages.townsq.service.TopicService;
import br.pucrs.ages.townsq.utils.Slugify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class QuestionController {

    private TopicService topicService;
    private QuestionService questionService;

    @Autowired
    public QuestionController(TopicService topicService, QuestionService questionService){
        this.topicService = topicService;
        this.questionService = questionService;
    }

    /**
     * Returns the index page without being logged in.
     * @return index page
     */
    @GetMapping("/")
    public String getIndex(Model model){
        model.addAttribute("questions", questionService.getIndexQuestions());
        return "index";
    }

    /**
     * Returns the question creation page with the topics from the database
     * @param m The UI Model
     * @return String
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/question/create")
    public String getQuestionCreatePage(Model m){
        m.addAttribute("topics", topicService.getAllTopicsByStatus(1));
        m.addAttribute("question", new Question());
        return "questionForm";
    }

    /**
     * Saves / edit the question.
     * @param user The user from the request
     * @param question The question being saved / updated
     * @return The page
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/question/save")
    public String postQuestionCreate(@AuthenticationPrincipal User user,
                                     @ModelAttribute Question question,
                                     final RedirectAttributes redirectAttributes){
        try {
            questionService.save(question, user);
            redirectAttributes.addFlashAttribute("success", "Pergunta cadastrada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Não foi possível cadastrar a pergunta.");
        }
        return "redirect:/";
    }

    /**
     *
     * @param user The authenticated user
     * @param questionId The question id (long)
     * @return Page
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/question/delete/{questionId}")
    public String getDeleteQuestionRoute(@AuthenticationPrincipal User user,
                                         @PathVariable long questionId,
                                         final RedirectAttributes redirectAttributes){
        boolean hasDeleted = questionService.deleteQuestionOfUser(user.getId(), questionId);
        if(hasDeleted)
            redirectAttributes.addFlashAttribute("success", "Pergunta deletada com sucesso!");
        else
            redirectAttributes.addFlashAttribute("error", "Não foi possível deletar a pergunta.");
        return "redirect:/";
    }


    @GetMapping(value = {"/question/{id}", "question/{id}/{slug}"})
    public String getUserById(HttpServletRequest request,
                              @PathVariable long id,
                              @PathVariable(required = false) String slug,
                              Model model){
        Question question = questionService.getQuestionById(id).orElse(null);
        if(question != null){
            String questionSlug = Slugify.toSlug(question.getTitle());
            if(slug == null || !slug.equals(questionSlug)){
                request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.MOVED_PERMANENTLY);
                return "redirect:/question/" + id + "/" + questionSlug;
            }
            model.addAttribute("question", question);
            return "question";
        }
        return "question";
    }

}
