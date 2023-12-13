package com.shop.ua.controllers;

import com.shop.ua.component.RepositoryManager;
import com.shop.ua.models.Store;
import com.shop.ua.models.User;
import com.shop.ua.services.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebStoreController {
    @Autowired
    private RepositoryManager repositoryManager;
    @Autowired
    private StoreService storeService;

    @GetMapping("/create-store")
    public String showCreateStoreForm(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String userEmail = authentication.getName();
            User user = repositoryManager.getUserService().findByEmail(userEmail);

            if (user != null && user.getStore() == null) {
                model.addAttribute("user", user);
                return "create-store";
            }
        }

        return "redirect:/shop";
    }

    @PostMapping("/create-store")
    public String createStore(@RequestParam("name") String name,
                              @RequestParam("company_full_name") String companyFullName,
                              @RequestParam("main_office") String mainOffice,
                              @RequestParam("customer_support") String customerSupport,
                              @RequestParam("legal_form") String legalForm,
                              @RequestParam("legal_entity_code") int legalEntityCode,
                              @RequestParam("location") String location,
                              @RequestParam("authorised_persons") String authorisedPersons,
                              @RequestParam("is_registered") String isRegistered,
                              Principal principal) {

        // Отримайте об'єкт користувача з системи аутентифікації
        User owner = repositoryManager.getUserService().findByEmail(principal.getName());

        // Створіть магазин та збережіть його в базі даних
        storeService.createStore(owner, name, companyFullName, mainOffice, customerSupport,
                legalForm, legalEntityCode, location, authorisedPersons, isRegistered);

        return "redirect:/shop"; // Перенаправлення на головну сторінку після успішного створення магазину
    }
}
