package br.pucrs.ages.townsq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuestionController {

    /**
     * Returns the Question page with it's contents. Will be implemented on future sprints.
     * @return question page
     */
    @GetMapping("/question")
    public String getQuestionPage(){
        return "question";
    }
    @GetMapping("/question/create")
    public String getQuestionCreatePage(){
        return "questionCreate";
    }

}
