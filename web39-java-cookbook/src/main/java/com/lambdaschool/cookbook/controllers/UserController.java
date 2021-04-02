package com.lambdaschool.cookbook.controllers;


import com.lambdaschool.cookbook.models.User;
import com.lambdaschool.cookbook.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Transactional
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listAllUsers() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getSelf() {
        User u = userService.getCurrentUser();
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping(value = "/current", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateSelf(
            @RequestBody
                    User user
    ) {
        User currentUser = userService.getCurrentUser();
        User updatedUser = userService.update(user, currentUser.getUserid());
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    //	@PreAuthorize("hasAnyRole('USER')")
    //	@DeleteMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    //	public ResponseEntity<?> deleteSelf() {
    //		User currentUser = userService.getCurrentUser();
    //		userService.delete(currentUser.getUserid());
    //		return new ResponseEntity<>(HttpStatus.OK);
    //	}

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "{userid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable long userid)
    {
        User u = userService.findUserById(userid);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value = "{userid}")
    public ResponseEntity<?> delete(@PathVariable long userid)
    {
        userService.delete(userid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //	@GetMapping(value = "/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
    //	public ResponseEntity<?> getUserRecipes() {
    //		List<Recipe> userRecipes = new ArrayList<>(userService.getUserRecipes());
    //		return new ResponseEntity<>(userRecipes, HttpStatus.OK);
    //	}

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping(value = "/{userid}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable long userid,
                                    @RequestBody User user
    ) {
        User updatedUser = userService.update(user, userid);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


}