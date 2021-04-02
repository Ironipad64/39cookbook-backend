package com.lambdaschool.cookbook.controllers;


import com.lambdaschool.cookbook.exceptions.ResourceNotFoundException;
import com.lambdaschool.cookbook.models.Recipe;
import com.lambdaschool.cookbook.models.User;
import com.lambdaschool.cookbook.models.utils.PagingHeaders;
import com.lambdaschool.cookbook.models.utils.PagingResponse;
import com.lambdaschool.cookbook.services.RecipeServiceImpl;
import com.lambdaschool.cookbook.services.UserService;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Transactional
@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final UserService userService;
    private final RecipeServiceImpl recipeService;

    @Autowired
    public RecipeController(
            UserService userService,
            RecipeServiceImpl recipeService
    ) {
        this.userService = userService;
        this.recipeService = recipeService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Transactional
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Recipe create(
            @Valid
            @RequestBody
                    Recipe recipe
    ) {
        User currentUser = userService.getCurrentUser();
        System.out.println(currentUser);
        recipe.setUser(currentUser);
        //		recipe.setRecipeid(0);
        Recipe createdRecipe = recipeService.create(recipe);
        System.out.println(createdRecipe);
        return createdRecipe;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Transactional
    @GetMapping(value = "currentuser", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getCurrentUserRecipes() {
        List<Recipe> recipes = recipeService.getUserRecipes();
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Transactional
    @PatchMapping(value = "/{recipeid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Recipe update(
            @PathVariable(name = "recipeid")
                    Long recipeid,
            @RequestBody
                    Recipe item
    ) {
        if (checkRecipeOwnership(recipeid)) {
            return recipeService.update(recipeid, item);
        } else {
            return null;
        }
    }


    private boolean checkRecipeOwnership(Long recipeid)
            throws ResourceNotFoundException {
        User currentUser = userService.getCurrentUser();
        Recipe recipe = recipeService.findById(recipeid)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find recipe " + recipeid));
        if (currentUser.getUserid() == recipe.getUser()
                .getUserid()) {
            return true;
        } else {
            throw new ResourceNotFoundException("INVALID ACCESS");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Transactional
    @DeleteMapping(value = "/{recipeid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable(name = "recipeid")
                    Long recipeid
    ) {
        if (checkRecipeOwnership(recipeid)) {
            recipeService.delete(recipeid);
        }
    }

    @Transactional
    @GetMapping(value = "/id/{recipeid}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getRecipeById(
            @PathVariable(name = "recipeid")
                    Long recipeid
    ) {
        Recipe recipe = recipeService.get(recipeid);
        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }


}