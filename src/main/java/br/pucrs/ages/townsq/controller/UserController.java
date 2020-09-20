package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.service.UserService;
import br.pucrs.ages.townsq.utils.Slugify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.View;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service){
        this.service = service;
    }

    @GetMapping("/signup")
    public String getUserSignupPage(){
        return "signup";
    }

    /**
     * POST route that redirects the user to the login page after signup
     * @param <User> user data to be saved.
     * @return redirect to sigin page
     */
    @PostMapping("/signup")
    public String postUserSignup(@ModelAttribute User user, Model model){
        try {
            service.save(user);
        } catch (Exception e) {
            model.addAttribute("error", "E-mail já cadastrado");
            return "signup";
        }
        return "redirect:/signin";
    }

    @PostMapping("/user/edit")
    public String postUserUpdate(@ModelAttribute User user, Model model, Authentication auth){
        try {
            service.update(user, auth.getName());
        } catch (Exception e) {
            model.addAttribute("error", "Erro");
            return "users";
        }
        return "redirect:/user/edit";
    }

    @GetMapping("/signin")
    public String getUserSigninPage(){
        return "signin";
    }

    @GetMapping("/logout")
    public String getLogout(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        SecurityContextHolder.clearContext();
        if(session != null) session.invalidate();
        for(Cookie cookie : request.getCookies()) cookie.setMaxAge(0);
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model){
        model.addAttribute("users", service.findAll());
        return "users";
    }

    @GetMapping(value = {"/user/{id}"})
    public String getUserById(HttpServletRequest request, @PathVariable long id,Model model, HttpSession session){
        User user = service.findById(id).orElse(null);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping(value = {"/user/edit"})
    public String getUserEditById(HttpServletRequest request, Model model, Authentication auth){
        User user = service.findByEmail(auth.getName()).orElse(null);
        model.addAttribute("user", user);
        return "userEdit";
    }
}
