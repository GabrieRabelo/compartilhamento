package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.service.UserService;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;

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
     * @param user <User> data to be saved.
     * @return redirect to sigin page
     */
    @PostMapping("/signup")
    public String postUserSignup(@ModelAttribute User user, Model model){
        try {
            service.save(user);
        } catch (DataIntegrityViolationException error) {
            model.addAttribute("error", "E-mail já cadastrado.");
            return "signup";
        } catch (ConstraintViolationException error) {
            ConstraintViolationImpl c = (ConstraintViolationImpl) error.getConstraintViolations().toArray()[0];
            model.addAttribute("error", c.getMessage());
            return "signup";
        }
        catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
        catch (Exception e) {
            System.out.println(e.toString());
            model.addAttribute("error", "Erro ao processar cadastro.");
            return "signup";
        }
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

    /**
     * Logout route for GET requests.
     * @param request <HttpServletRequest> The GET request
     * @param response <HttpServletResponse> The returned response
     * @return redirect to the login page.
     */
    @GetMapping("/logout")
    public String getLogout(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        SecurityContextHolder.clearContext();
        if(session != null) session.invalidate();
        for(Cookie cookie : request.getCookies()) cookie.setMaxAge(0);
        return "redirect:/signin";
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

    @PostMapping("/user/edit")
    public String postUserUpdate(@ModelAttribute User user, Model model, Authentication auth){
        try {
            service.update(user, auth.getName());
        } catch (Exception e) {
            model.addAttribute("error", "Erro");
            return "redirect:/users";
        }
        return "redirect:/user/edit";
    }

    @PostMapping("/user/editPassword")
    public String postUserUpdatePassword(@ModelAttribute User user, Model model, Authentication auth){
        try {
            service.updatePassword(user, auth.getName());
        } catch (Exception e) {
            model.addAttribute("error", "Erro");
            return "redirect:/users";
        }
        return "redirect:/user/edit";
    }
}
