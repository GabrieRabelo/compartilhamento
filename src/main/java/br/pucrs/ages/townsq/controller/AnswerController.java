package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.service.AnswerService;
import br.pucrs.ages.townsq.service.EmailService;
import br.pucrs.ages.townsq.service.QuestionService;
import br.pucrs.ages.townsq.utils.Slugify;
import javassist.NotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;
    private final EmailService emailService;

    public AnswerController(AnswerService answerService, QuestionService questionService, EmailService emailService) {
        this.answerService = answerService;
        this.questionService = questionService;
        this.emailService = emailService;
    }

    /**
     * Post route to create an answer
     * @return String
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/answer/create")
    public String postCreateAnswer(@AuthenticationPrincipal User user,
                                   @ModelAttribute Answer answer,
                                   @ModelAttribute Question question,
                                   final RedirectAttributes redirectAttributes
    ) {
        try {
            Answer createdAnswer = answerService.saveAnswer(answer, user, question);
            emailService.createEmail(createdAnswer);
            redirectAttributes.addFlashAttribute("success", "Resposta criada com sucesso!");
            redirectAttributes.addFlashAttribute("reputation", "Você ganhou 10 pontos!");
            return "redirect:/question/" + question.getId() + "/" + Slugify.toSlug(question.getTitle());
        } catch (IllegalArgumentException ie) {
            redirectAttributes.addFlashAttribute("error", "Resposta não pode ser vazia");
            return "redirect:/question/" + question.getId() + "/" + Slugify.toSlug(question.getTitle());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Não foi possível cadastrar a resposta.");
            return "redirect:/question/" + question.getId() + "/" + Slugify.toSlug(question.getTitle());
        }
    }

    @PostMapping("/answer/edit/{id}")
    public String postEditAnswer(
            @AuthenticationPrincipal User user,
            @ModelAttribute @Valid Answer answer,
            BindingResult bindingResult,
            @PathVariable long id,
            final RedirectAttributes redirectAttributes
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Question questionFrom = questionService.getQuestionById(answer.getQuestion().getId()).orElse(null);
        if(questionFrom == null){
            redirectAttributes.addFlashAttribute("error","Operação inválida.");
            return "redirect:/";
        }
        try{
            answerService.editAnswer(answer.getText(),user,id);
            redirectAttributes.addFlashAttribute("success","Resposta editada com sucesso.");
        }catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("error",e.getMessage());
        }
        return "redirect:/question/" + questionFrom.getId() +  "/" + Slugify.toSlug(questionFrom.getTitle());
    }

    /**
     * Route to soft delete an answer
     * @param user Authenticated user
     * @param answerId Answer id
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/answer/delete/{answerId}")
    public String getDeleteAnswerRoute(@AuthenticationPrincipal User user,
                                       @PathVariable long answerId,
                                       final RedirectAttributes redirectAttributes) {
        Optional<Answer> optAnswer = answerService.findById(answerId);
        Question ansQuestion;
        if(optAnswer.isPresent()) {
            Answer answer = optAnswer.get();
            ansQuestion = answer.getQuestion();

            boolean hasDeleted = answerService.delete(user, answerId);
            if(hasDeleted)
                redirectAttributes.addFlashAttribute("success", "Resposta deletada com sucesso!");
            else
                redirectAttributes.addFlashAttribute("error", "Não foi possível deletar a resposta.");
            return "redirect:/question/" + ansQuestion.getId() + "/" + Slugify.toSlug(ansQuestion.getTitle());
        }
        return "";
    }

    @GetMapping("/answer/favorite/{id}")
    public String favoriteAnswer(
          @AuthenticationPrincipal User user,
          @PathVariable long id,
          final RedirectAttributes redirectAttributes
    )  {
        Optional<Answer> optAnswer = answerService.findById(id);

        if(optAnswer.isPresent()){
            Question questionFrom = questionService.getQuestionById(optAnswer.get().getQuestion().getId()).orElse(null);
            if(questionFrom == null){
                redirectAttributes.addFlashAttribute("error","Operação inválida.");
                return "redirect:/";
            }
            try {
                answerService.favoriteAnswer(user, id, questionFrom);
                redirectAttributes.addFlashAttribute("success", "Resposta favoritada com sucesso.");
            } catch (SecurityException | NotFoundException | IllegalArgumentException  e ) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
            return "redirect:/question/" + questionFrom.getId() + "/" + Slugify.toSlug(questionFrom.getTitle());
        }
        return "";
    }
}
