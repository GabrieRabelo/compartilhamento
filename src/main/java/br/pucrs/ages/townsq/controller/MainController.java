package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.Banner;
import br.pucrs.ages.townsq.service.QuestionService;
import br.pucrs.ages.townsq.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import br.pucrs.ages.townsq.service.BannerService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MainController {

    private final QuestionService questionService;
    private final BannerService bannerService;
    private final TopicService topicService;

    @Autowired
    public MainController(QuestionService qService, BannerService adService, TopicService topicService){
        this.bannerService = adService;
        this.questionService = qService;
        this.topicService = topicService;
    }

    /**
     * Returns the index page without being logged in.
     * @return index page
     */
    @GetMapping("/")
    public String getIndex(Model model, @RequestParam(value="topic", required = false) List<Long> params){
        Banner banner = bannerService.getActiveBanner().orElse(null);

        model.addAttribute("banner", banner);
        model.addAttribute("topics", topicService.getAllTopics());
        model.addAttribute("questions", questionService.getIndexQuestions(params));
        return "index";
    }

    /**
     * Logout route for GET requests.
     * @param request <HttpServletRequest> The GET request
     * @return redirect to the login page.
     */
    @GetMapping("/logout")
    public String getLogout(HttpServletRequest request){
        HttpSession session = request.getSession();
        SecurityContextHolder.clearContext();
        if(session != null) session.invalidate();
        for(Cookie cookie : request.getCookies()) cookie.setMaxAge(0);
        return "redirect:/signin";
    }

    /**
     * GET route that returns the application login page
     * @return signin page
     */
    @GetMapping("/signin")
    public String getUserSigninPage(@RequestParam(required = false) String error, Model model){
        if (error != null && error.equals("credentials")) {
            model.addAttribute("error", "E-mail ou senha inválidos.");
        }
        return "signin";
    }

    @GetMapping("/signup")
    public String getUserSignupPage(){
        return "signup";
    }

}
