package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.service.UserService;
import br.pucrs.ages.townsq.utils.Slugify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public String postUserSignup(@ModelAttribute User user){
        service.save(user);
        return "redirect:/signin";
    }

    /**
     * GET route that returns the application login page
     * @return signin page
     */
    @GetMapping("/signin")
    public String getUserSigninPage(){
        return "signin";
    }

    /**
     * Logout route for GET requests.
     * @param <HttpServletRequest> request The GET request
     * @param <HttpServletResponse> response The returned response
     * @return redirect to the login page.
     */
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

    @GetMapping(value = {"/users/{id}", "users/{id}/{slug}"})
    public String getUserById(HttpServletRequest request,
                              @PathVariable long id,
                              @PathVariable(required = false) String slug,
                              Model model){
        User user = service.findById(id).orElse(null);
        if(user != null){
            String userSlug = Slugify.toSlug(user.getName());
            if(slug == null || !slug.equals(userSlug)){
                request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.MOVED_PERMANENTLY);
                return "redirect:/users/" + id + "/" + userSlug;
            }
            model.addAttribute("user", user);
            return "user";
        }
        return "user";
    }

}
