package br.pucrs.ages.townsq.controller;

import br.pucrs.ages.townsq.model.Banner;
import br.pucrs.ages.townsq.model.User;
import br.pucrs.ages.townsq.service.BannerService;
import br.pucrs.ages.townsq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;

@Controller
public class AdminController {

    private final UserService service;
    private final BannerService adService;

    @Autowired
    public AdminController(UserService service, BannerService adService){
        this.service = service;
        this.adService = adService;
    }

    @GetMapping("/admin")
    public String getAdminPage() {
        return  "redirect:/admin/banner";
    }

    @GetMapping("/admin/banner")
    public String getAdminBannerPage(Model model) {
        Banner currentBanner = adService.getActiveBanner().orElse(null);
        model.addAttribute("banner", currentBanner);

        model.addAttribute("active", true);
        return  "adminBanner";
    }

    @GetMapping("/admin/mods")
    public String getAdminModsPage(Model model) {
        model.addAttribute("active", true);
        return  "adminMods";
    }

    @PostMapping("/admin/banner")
    public String createAdminBanner(Banner ads, Model model) {
        Banner banner = adService.save(ads);

        model.addAttribute("banner", banner);
        model.addAttribute("success", "Propaganda atualizada com sucesso.");
        model.addAttribute("active", true);

        return "adminBanner";
    }

    @PostMapping("/admin/mods/getUser")
    public String postMods(User usuario, final RedirectAttributes redirectAttr) {
        User user = service.getUserByEmail(usuario.getName()).orElse(null);
        if (user == null) {
            redirectAttr.addFlashAttribute("error", "Usuário não encontrado");
        } else {
            redirectAttr.addFlashAttribute("user", user);
        }
        return "redirect:/admin/mods";
    }

}

