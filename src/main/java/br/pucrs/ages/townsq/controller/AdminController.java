package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.Banner;
import br.pucrs.ages.townsq.model.Role;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.service.BannerService;
import br.pucrs.ages.townsq.service.UserService;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.ConstraintViolationException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;

@Controller
public class AdminController {

    private final UserService service;
    private final BannerService adService;
    private static final String ADMIN_BANNER = "redirect:/admin/banner";
    private static final String ADMIN_MODS = "redirect:/admin/mods";
    private static final String ERROR = "error";
    private static final String SUCCESS = "success";
    private static final String ACTIVE = "active";
    private static final String ROLE_MODERATOR = "ROLE_MODERATOR";




    @Autowired
    public AdminController(UserService service, BannerService adService){
        this.service = service;
        this.adService = adService;
    }

    @GetMapping("/admin")
    public String getAdminPage() {
        return ADMIN_BANNER;
    }

    @GetMapping("/admin/banner")
    public String getAdminBannerPage(Model model) {
        Banner currentBanner = adService.getActiveBanner().orElse(new Banner());
        model.addAttribute("banner", currentBanner);
        model.addAttribute(ACTIVE, true);
        return  "adminBanner";
    }

    @GetMapping("/admin/mods")
    public String getAdminModsPage(Model model) {
        List<User> moderators = service.getAllModerators();
        model.addAttribute("moderators", moderators);
        model.addAttribute(ACTIVE, true);
        return  "adminMods";
    }

    @PostMapping("/admin/mods/create")
    public String postAdminMod(User usuario, Model model, final RedirectAttributes redirectAttributes) {
        User user;

        try {
            user = service.getUserByEmail(usuario.getName()).orElse(null);


            assert user != null;
            Set<Role> userRole = user.getRoles();

            Role role = (Role) userRole.toArray()[0];

            if (role.getName().equals(ROLE_MODERATOR)) {
                redirectAttributes.addFlashAttribute(ERROR, "O usuário " + user.getName() + " já é um moderador.");
                return ADMIN_MODS;
            } else if (role.getName().equals("ROLE_ADMIN")) {
                redirectAttributes.addFlashAttribute(ERROR, "Este usuário é um administrador.");
                return ADMIN_MODS;
            }
            User userUpdated = service.updateUserToMod(user);
            redirectAttributes.addFlashAttribute(SUCCESS, "Usuário " + userUpdated.getName() + " promovido a moderador.");
            return ADMIN_MODS;
        } catch(Exception e) {
            redirectAttributes.addFlashAttribute(ERROR, "Erro ao criar um moderator.");
            return ADMIN_MODS;
        }
    }

    @GetMapping("/admin/mods/delete/{id}")
    public String postModToUser(@PathVariable long id, final RedirectAttributes redirectAttributes) {
        User user;

        try{
            user = service.getUserById(id).orElse(null);

            assert user != null;
            Set<Role> userRole = user.getRoles();

            Role role = (Role) userRole.toArray()[0];

            if (!role.getName().equals(ROLE_MODERATOR)) {
                redirectAttributes.addFlashAttribute(ERROR, "Usuário " + user.getName() + " não é um moderador.");
                return ADMIN_MODS;
            } else {
                User userUpdated = service.updateModToUser(user);
                redirectAttributes.addFlashAttribute(SUCCESS, "Moderador " + userUpdated.getName() + " removido com sucesso.");
            }
            return ADMIN_MODS;

        } catch(Exception e) {
            redirectAttributes.addFlashAttribute(ERROR, "Erro ao deletar um moderador.");
            return ADMIN_MODS;
        }

    }

    @PostMapping("/admin/banner")
    public String createAdminBanner(Banner ads, Model model,
                                    final RedirectAttributes redirectAttributes) {

        Banner banner;
        try {
            banner = adService.save(ads);
            model.addAttribute("banner", banner);
            model.addAttribute(SUCCESS, "Propaganda atualizada com sucesso.");
        } catch (ConstraintViolationException error) {
            ConstraintViolationImpl c = (ConstraintViolationImpl) error.getConstraintViolations().toArray()[0];
            model.addAttribute(ERROR, c.getMessage());
            return ADMIN_BANNER;
        } catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute(ERROR, e.getMessage());
            return ADMIN_BANNER;
        } catch (MalformedURLException e) {
            redirectAttributes.addFlashAttribute(ERROR, "URL inválida!");
            return ADMIN_BANNER;
        }

        model.addAttribute(ACTIVE, true);

        return "adminBanner";
    }

    @PostMapping("/admin/mods/getUser")
    public String postMods(User usuario, final RedirectAttributes redirectAttr) {
        User user = service.getUserByEmail(usuario.getName()).orElse(null);
        if (user == null) {
            redirectAttr.addFlashAttribute(ERROR, "Usuário não encontrado.");
        } else {

            Set<Role> userRole = user.getRoles();

            Role role = (Role) userRole.toArray()[0];

            if (role.getName().equals(ROLE_MODERATOR)) {
                redirectAttr.addFlashAttribute(ERROR, "O usuário " + user.getName() + " já é um moderador.");
                return ADMIN_MODS;
            } else if (role.getName().equals("ROLE_ADMIN")) {
                redirectAttr.addFlashAttribute(ERROR, "Este usuário é um administrador.");
                return ADMIN_MODS;
            } else {
                redirectAttr.addFlashAttribute("user", user);
            }
        }
        return ADMIN_MODS;
    }

}

