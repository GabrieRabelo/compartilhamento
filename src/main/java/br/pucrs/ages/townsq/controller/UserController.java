package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.exception.UserNotFoundException;
import br.pucrs.ages.townsq.model.Banner;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.service.BannerService;
import br.pucrs.ages.townsq.service.UserService;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.MalformedURLException;
import java.util.UUID;

@Controller
public class UserController {

    private final UserService service;
    private final BannerService bannerService;

    private static final String ERROR = "error";
    private static final String SIGNUP = "signup";
    private static final String USER_EDIT = "redirect:/user/edit";

    @Autowired
    public UserController(UserService service, BannerService adService){
        this.bannerService = adService;
        this.service = service;
    }

    /**
     * POST route that redirects the user to the login page after signup
     * @param user <User> data to be saved.
     * @return redirect to sigin page
     */
    @PostMapping("/signup")
    public String postUserSignup(@ModelAttribute User user, Model model, HttpServletRequest request, final RedirectAttributes redirectAttributes) {
        String pass = user.getPassword();
        try {
            service.save(user);
        } catch (DataIntegrityViolationException error) {
            model.addAttribute(ERROR, "E-mail já cadastrado.");
            return SIGNUP;
        } catch (ConstraintViolationException error) {
            ConstraintViolationImpl c = (ConstraintViolationImpl) error.getConstraintViolations().toArray()[0];
            model.addAttribute(ERROR, c.getMessage());
            return SIGNUP;
        } catch (IllegalArgumentException e) {
            model.addAttribute(ERROR, e.getMessage());
            return SIGNUP;
        } catch (Exception e) {
            System.out.println(e.toString());
            model.addAttribute(ERROR, "Erro ao processar cadastro.");
            return SIGNUP;
        }
        try {
            request.login(user.getUsername(), pass);
        } catch (ServletException e) {
            model.addAttribute(ERROR, e.getMessage());
        }
        redirectAttributes.addFlashAttribute("success", "Cadastro realizado com sucesso!");
        return "redirect:/";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", service.getAllModerators());
        return "users";
    }

    @GetMapping(value = {"/user/{id}"})
    public String getUserById(HttpServletRequest request, @PathVariable long id, Model model, HttpSession session) {
        User user = service.getUserById(id).orElse(null);

        if(user == null) throw new UserNotFoundException();

        Banner banner = bannerService.getActiveBanner().orElse(null);
        model.addAttribute("banner", banner);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping(value = {"/user/edit"})
    public String getUserEditById(HttpServletRequest request, Model model, Authentication auth) {
        User user = service.getUserByEmail(auth.getName()).orElse(null);
        model.addAttribute("user", user);
        return "userEdit";
    }

    @PostMapping("/user/edit")
    public String postUserUpdate(@RequestParam("fileimage") MultipartFile file,
                                 @ModelAttribute User user,
                                 Model model,
                                 @AuthenticationPrincipal User userPrincipal,
                                 final RedirectAttributes redirectAttributes) {

        User userEdit = service.getUserById(user.getId()).orElse(null);
        if (userEdit == null) {
            redirectAttributes.addFlashAttribute(ERROR, "Erro ao editar o usuário.");
            return "redirect:/";
        }
        Integer completeProfile = userEdit.getHasCompletedProfile();

        if (!file.isEmpty()) {
            String path = singleFileUpload(file, userEdit);
            user.setImage(path);
        }
        try {
            user = service.update(user, userEdit);
            userPrincipal.setImage(user.getImage());
            model.addAttribute("user", user);
        } catch (MalformedURLException e) {
            redirectAttributes.addFlashAttribute(ERROR, "URL inválida!");
            return USER_EDIT;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(ERROR, e.getMessage());
            return USER_EDIT;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR, "Erro ao atualizar perfil.");
            return USER_EDIT;
        }
        if (!user.getHasCompletedProfile().equals(completeProfile)) {
            redirectAttributes.addFlashAttribute("reputation", "Você ganhou 20 pontos!");
        }

        redirectAttributes.addFlashAttribute("success", "Perfil atualizado!");
        return USER_EDIT;
    }

    public String singleFileUpload(@RequestParam("file") MultipartFile file, User user) {
        String rootToStatic = "./src/main/resources/static";
        String s = "/img/users/";
        String uniqueID = UUID.randomUUID().toString();
        if (file.isEmpty()) {
            return rootToStatic + s + "defaultUser.svg";
        }

        Path path = null;
        String strPath = s + uniqueID + getFileExtension(file.getOriginalFilename());
        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            path = Paths.get(rootToStatic + strPath);
            Files.write(path, bytes);

            String oldImage = user.getImage();
            if (oldImage != null && !oldImage.equals(rootToStatic + s + "defaultUser.svg")) {
                Files.delete(Paths.get(rootToStatic + oldImage));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return strPath;
    }

    private String getFileExtension(String filename) {
        String[] arr = filename.split("\\.");
        return "." + arr[arr.length - 1];
    }

}
