package com.netflix.webapp.controllers;

import com.netflix.webapp.dtos.GenreDTO;
import com.netflix.webapp.services.GenreService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService, PasswordEncoder passwordEncoder) {
        this.genreService = genreService;
    }

    @GetMapping("/genres")
    public String genresPage(Model model) {
        GenreDTO newGenre = new GenreDTO();
        model.addAttribute("newGenre", newGenre);

        List<GenreDTO> genres = genreService.findAll();
        model.addAttribute("genres", genres);

        return "genres";
    }

    @GetMapping("/genres/{genreId}")
    public String editGenrePage(@PathVariable("genreId") String genreId, Model model) {
        GenreDTO genre = genreService.findOne(genreId);
        model.addAttribute("genre", genre);

        return "edit_genre";
    }

    @PostMapping("/genres/create")
    public String createGenre(@Valid @ModelAttribute("newGenre") GenreDTO newGenre, BindingResult result, Model model) {
        GenreDTO genre = genreService.findOneByName(newGenre.getName());

        if (genre != null) {
            result.rejectValue("name", null, "There is already a category named " + newGenre.getName());
        }

        if (result.hasErrors()) {
            model.addAttribute("newGenre", newGenre);

            List<GenreDTO> genres = genreService.findAll();
            model.addAttribute("genres", genres);

            return "/genres";
        }

        genreService.save(newGenre);

        return "redirect:/genres?success";
    }

    @PostMapping("/genres/update")
    public String updateGenre(@Valid @ModelAttribute("updatedGenre") GenreDTO updatedGenre, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("updatedGenre", updatedGenre);
            return "genres";
        }

        genreService.update(updatedGenre);

        return "redirect:/genres";
    }

    @PostMapping("/genres/delete")
    public String deleteGenre(@Valid @ModelAttribute("genreId") String genreId, BindingResult result, Model model) {
        GenreDTO genre = genreService.findOne(genreId);

        if (genre == null) {
            result.rejectValue("name", null, "The category does not exist");
        }

        if (result.hasErrors()) {
            List<GenreDTO> genres = genreService.findAll();
            model.addAttribute("genres", genres);

            return "/genres";
        }

        genreService.delete(genreId);

        return "redirect:/genres";
    }
}