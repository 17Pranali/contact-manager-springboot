package com.contactapp.contactmanager.controller;

import com.contactapp.contactmanager.model.Contact;
import com.contactapp.contactmanager.repository.ContactRepository;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactRepository repo;
    
    

    @GetMapping
    public String listContacts(Model model) {
        model.addAttribute("contacts", repo.findAll());
        return "index";
    }

    @GetMapping("/add")
    public String showForm(Contact contact) {
        return "form";
    }

    @PostMapping("/save")
    public String save(@Valid Contact contact, BindingResult result, Model model) {

        // Check for duplicates
        if (repo.existsByEmail(contact.getEmail()) && (contact.getId() == null || !repo.findById(contact.getId()).get().getEmail().equals(contact.getEmail()))) {
            result.rejectValue("email", "error.contact", "Email already exists");
        }

        if (repo.existsByPhone(contact.getPhone()) && (contact.getId() == null || !repo.findById(contact.getId()).get().getPhone().equals(contact.getPhone()))) {
            result.rejectValue("phone", "error.contact", "Phone already exists");
        }

        if (result.hasErrors()) {
            return "form";
        }

        repo.save(contact);
        
        return "redirect:/contacts";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Contact contact = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID: " + id));
        model.addAttribute("contact", contact);
        return "form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/contacts";
    }
}
