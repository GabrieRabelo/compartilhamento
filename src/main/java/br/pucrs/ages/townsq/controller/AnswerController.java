package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.service.AnswerService;
import org.springframework.stereotype.Controller;
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
    public String postCreateAnswer() {
        //calls answerService.save(answer);
        return "";
    }
}
