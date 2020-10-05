package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final QuestionService questionService;

    @Autowired
    public IndexController(QuestionService qService){
        this.questionService = qService;
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

}
