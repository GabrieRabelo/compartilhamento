package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.Answer;
import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.service.AnswerService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AnswerController {

    private AnswerService answerService;

    public AnswerController(AnswerService answerService) { this.answerService = answerService; }

    /**
     * Post route to create an answer
     * @return String
     */
    @PostMapping("/answer/create")
    public String postCreateAnswer(@AuthenticationPrincipal User user,
                                   @ModelAttribute Answer answer,
                                   @ModelAttribute Question question
                                   ) {

        answerService.saveAnswer(answer, user, question);
        return "redirect:/question/" + question.getId();
    }
}
