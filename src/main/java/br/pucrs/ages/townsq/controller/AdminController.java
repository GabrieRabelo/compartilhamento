package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.Banner;
import br.pucrs.ages.townsq.model.Role;
import br.pucrs.ages.townsq.model.Topic;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.service.BannerService;
import br.pucrs.ages.townsq.service.TopicService;
import br.pucrs.ages.townsq.service.UserService;
import org.hibernate.exception.DataException;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final TopicService topicService;

    @Autowired
    public AdminController(UserService service, BannerService adService, TopicService topicService){
        this.service = service;
        this.adService = adService;
        this.topicService = topicService;
    }

    @GetMapping("/admin")
    public String getAdminPage() {
        return  "redirect:/admin/banner";
    }

    @GetMapping("/admin/banner")
    public String getAdminBannerPage(Model model) {
        Banner currentBanner = adService.getActiveBanner().orElse(new Banner());
        model.addAttribute("banner", currentBanner);
        model.addAttribute("active", true);
        return  "adminBanner";
    }

    @GetMapping("/admin/mods")
    public String getAdminModsPage(Model model) {
        List<User> moderators = service.getAllModerators();
        model.addAttribute("moderators", moderators);
        model.addAttribute("active", true);
        return  "adminMods";
    }

    @GetMapping("/admin/topics")
    public String getAdminTopicsPage(Model model) {
        List<Topic> topics = topicService.getAllTopicsByStatus(1);
        model.addAttribute("topics",topics);
        model.addAttribute("active", true);
        return  "adminTopic";
    }

    @PostMapping("/admin/topics/create")
    public String createNewTopic(Topic topic, final RedirectAttributes redirectAttributes){
        try{
            topicService.create(topic);
            redirectAttributes.addFlashAttribute("success","Tópico criado com sucesso.");
        } catch ( IllegalArgumentException e ){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch ( DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Não foi possível criar o tópico.");
        }
        return "redirect:/admin/topics";
    }

    @GetMapping("/admin/topics/delete/{id}")
    public String removeTopic(@PathVariable long id, final RedirectAttributes redirectAttributes){
        boolean deleted = topicService.setTopicToInactive(id);
        if (deleted){
            redirectAttributes.addFlashAttribute("success","Tópico removido com sucesso.");
        }
        else {
            redirectAttributes.addFlashAttribute("error","Erro ao remover tópico.");
        }
        return "redirect:/admin/topics";
    }

    @PostMapping("/admin/mods/create")
    public String postAdminMod(User usuario, Model model, final RedirectAttributes redirectAttributes) {
        User user;

        try {
            user = service.getUserByEmail(usuario.getName()).orElse(null);

            Set<Role> userRole = user.getRoles();

            Role role = (Role) userRole.toArray()[0];

            if (role.getName().equals("ROLE_MODERATOR")) {
                redirectAttributes.addFlashAttribute("error", "O usuário " + user.getName() + " já é um moderador.");
                return "redirect:/admin/mods";
            } else if (role.getName().equals("ROLE_ADMIN")) {
                redirectAttributes.addFlashAttribute("error", "Este usuário é um administrador.");
                return "redirect:/admin/mods";
            }
            User userUpdated = service.updateUserToMod(user);
            redirectAttributes.addFlashAttribute("success", "Usuário " + userUpdated.getName() + " promovido a moderador.");
            return "redirect:/admin/mods";
        } catch(Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao criar um moderator.");
            return "redirect:/admin/mods";
        }
    }

    @GetMapping("/admin/mods/delete/{id}")
    public String postModToUser(@PathVariable long id, final RedirectAttributes redirectAttributes) {
        User user;

        try{
            user = service.getUserById(id).orElse(null);
            Set<Role> userRole = user.getRoles();

            Role role = (Role) userRole.toArray()[0];

            if (!role.getName().equals("ROLE_MODERATOR")) {
                redirectAttributes.addFlashAttribute("error", "Usuário " + user.getName() + " não é um moderador.");
                return "redirect:/admin/mods";
            } else {
                User userUpdated = service.updateModToUser(user);
                redirectAttributes.addFlashAttribute("success", "Moderador " + userUpdated.getName() + " removido com sucesso.");
            }
            return "redirect:/admin/mods";

        } catch(Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao deletar um moderador.");
            return "redirect:/admin/mods";
        }

    }

    @PostMapping("/admin/banner")
    public String createAdminBanner(Banner ads, Model model,
                                    final RedirectAttributes redirectAttributes) {

        Banner banner;
        try {
            banner = adService.save(ads);
            model.addAttribute("banner", banner);
            model.addAttribute("success", "Propaganda atualizada com sucesso.");
        } catch (ConstraintViolationException error) {
            ConstraintViolationImpl c = (ConstraintViolationImpl) error.getConstraintViolations().toArray()[0];
            model.addAttribute("error", c.getMessage());
            return "redirect:/admin/banner";
        } catch (IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/banner";
        } catch (MalformedURLException e) {
            redirectAttributes.addFlashAttribute("error", "URL inválida!");
            return "redirect:/admin/banner";
        }

        model.addAttribute("active", true);

        return "adminBanner";
    }

    @PostMapping("/admin/mods/getUser")
    public String postMods(User usuario, final RedirectAttributes redirectAttr) {
        User user = service.getUserByEmail(usuario.getName()).orElse(null);
        if (user == null) {
            redirectAttr.addFlashAttribute("error", "Usuário não encontrado.");
        } else {

            Set<Role> userRole = user.getRoles();

            Role role = (Role) userRole.toArray()[0];

            if (role.getName().equals("ROLE_MODERATOR")) {
                redirectAttr.addFlashAttribute("error", "O usuário " + user.getName() + " já é um moderador.");
                return "redirect:/admin/mods";
            } else if (role.getName().equals("ROLE_ADMIN")) {
                redirectAttr.addFlashAttribute("error", "Este usuário é um administrador.");
                return "redirect:/admin/mods";
            } else {
                redirectAttr.addFlashAttribute("user", user);
            }
        }
        return "redirect:/admin/mods";
    }

}

