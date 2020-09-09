package br.pucrs.ages.townsq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuestionController {

    @GetMapping("/question")
    public String getQuestionPage(){
        return "question";
    }

}
