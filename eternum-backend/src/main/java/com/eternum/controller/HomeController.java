package com.eternum.controller;

import com.eternum.service.MemorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @Autowired
    private MemorialService memorialService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("featuredMemorials", memorialService.findFeaturedMemorials());
        model.addAttribute("recentMemorials", memorialService.findActiveMemorials(org.springframework.data.domain.PageRequest.of(0, 8)));
        return "pages/home";
    }

    @GetMapping("/login")
    public String login() {
        return "pages/login";
    }

    @GetMapping("/register")
    public String register() {
        return "pages/register";
    }

    @GetMapping("/memorial/{id}")
    public String memorialDetail(@PathVariable Integer id, Model model) {
        memorialService.findByIdWithUser(id).ifPresent(memorial -> {
            model.addAttribute("memorial", memorialService.toResponse(memorial));
        });
        return "pages/memorial-detail";
    }

    @GetMapping("/memorials")
    public String memorialsList(Model model) {
        model.addAttribute("memorials", memorialService.findActiveMemorials(org.springframework.data.domain.PageRequest.of(0, 20)));
        return "pages/memorials-list";
    }

    @GetMapping("/deactivation-request")
    public String deactivationRequest() {
        return "pages/deactivation-request";
    }

}
