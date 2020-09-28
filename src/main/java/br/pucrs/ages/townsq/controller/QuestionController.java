package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.Question;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.service.QuestionService;
import br.pucrs.ages.townsq.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
     * Returns the Question page with it's contents. Will be implemented on future sprints.
     * @return question page
     */
    @GetMapping("/question")
    public String getQuestionPage(){
        return "question";
    }

    /**
     * Returns the question creation page with the topics from the database
     * @param m The UI Model
     * @return String
     */
    @GetMapping("/question/create")
    public String getQuestionCreatePage(Model m){
        m.addAttribute("topics", topicService.getAllTopicsByStatus(1));
        return "questionCreate";
    }

    @PostMapping("/question/create")
    public String postQuestionCreate(@AuthenticationPrincipal User user, @ModelAttribute Question question, Model model){
        question.setUser(user);
        questionService.save(question);
        return "question";
    }

}
