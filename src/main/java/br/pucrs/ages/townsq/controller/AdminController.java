package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

    private final UserService service;

    @Autowired
    public AdminController(UserService service){
        this.service = service;
    }

    @GetMapping("/admin")
    public String getAdminPage() {
        return  "redirect:/admin/banner";
    }

    @GetMapping("/admin/banner")
    public String getAdminBannerPage() {
        return  "adminBanner";
    }

    @GetMapping("/admin/mods")
    public String getAdminModsPage(Model model) {
        return  "adminMods";
    }

    @PostMapping("/admin/mods/getUser")
    public String postMods(User usuario, final RedirectAttributes redirectAttr) {
        User user = service.findByEmail(usuario.getName()).orElse(null);
        if (user == null) {
            redirectAttr.addFlashAttribute("error", "Usuário não encontrado");
        } else {
            redirectAttr.addFlashAttribute("user", user);
        }
        return "redirect:/admin/mods";
    }

}

