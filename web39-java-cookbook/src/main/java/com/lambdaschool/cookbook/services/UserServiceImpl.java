package com.lambdaschool.cookbook.services;


import com.lambdaschool.cookbook.exceptions.ResourceFoundException;
import com.lambdaschool.cookbook.exceptions.ResourceNotFoundException;
import com.lambdaschool.cookbook.handlers.HelperFunctions;
import com.lambdaschool.cookbook.models.Recipe;
import com.lambdaschool.cookbook.models.Role;
import com.lambdaschool.cookbook.models.User;
import com.lambdaschool.cookbook.models.UserRoles;
import com.lambdaschool.cookbook.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * Implements UserService Interface
 */
@Transactional
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    /**
     * Connects this service to the User table.
     */
    @Autowired
    UserRepo userRepo;

    /**
     * Connects this service to the Role table
     */
    @Autowired
    RoleService roleService;
    /**
     * Connects this service to the Recipe table
     */
    @Autowired
    RecipeServiceImpl recipeService;
    /**
     * Allows this service to utilize helper functionis
     */
    @Autowired
    HelperFunctions helper;

//	@Autowired
//	public UserServiceImpl(
//			UserRepo userRepo,
//			RoleService roleService,
//			RecipeServiceImpl recipeService,
//			HelperFunctions helper
//	) {
//		this.userRepo      = userRepo;
//		this.roleService   = roleService;
//		this.recipeService = recipeService;
//		this.helper        = helper;
//	}

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        userRepo.findAll()
                .iterator()
                .forEachRemaining(list::add);
        return list;
    }

    @Override
    public List<User> findByNameContaining(String username) {
        return userRepo.findByUsernameContainingIgnoreCase(username.toLowerCase());
    }

    public User findUserById(long id)
            throws ResourceNotFoundException {
        User currentUser = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found!"));

        if (helper.isAuthorizedToMakeChange(currentUser.getUsername())) {
            return currentUser;
        } else {
            // note we should never get to this line but is needed for the compiler
            // to recognize that this exception can be thrown
            throw new ResourceNotFoundException("This user is not authorized to make change");
        }
    }

    @Override
    public User findByName(String name) {
        User uu = userRepo.findByUsername(name.toLowerCase());
        if (uu == null)
        {
            throw new ResourceNotFoundException("User name " + name + " not found!");
        }
        return uu;
    }


    @Transactional
    @Override
    public void delete(long id) {
        userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found!"));
        userRepo.deleteById(id);
    }



    @Transactional
    @Override
    public User save(User user) {

        User newUser = new User();

        if (user.getUserid() != 0) {
            newUser = userRepo.findById(user.getUserid())
                    .orElseThrow(() -> new ResourceNotFoundException("User id " + user.getUserid() + " not found!"));
        }

        newUser.setUsername(user.getUsername().toLowerCase());
        newUser.setPasswordNoEncrypt(user.getPassword());
        newUser.setEmail(user.getEmail().toLowerCase());
        newUser.setProfilepicture(user.getProfilepicture());

        newUser.getRoles().clear();
        for (UserRoles ur : user.getRoles()) {
            Role addRole = roleService.findRoleById(ur.getRole().getRoleid());

            newUser.getRoles().add(new UserRoles(newUser, addRole));
        }

        newUser.getRecipes().clear();
        for (Recipe recipe : user.getRecipes()) {
            Recipe newRecipe = recipeService.save(recipe);
            newRecipe.setUser(newUser);
            newUser.getRecipes().add(newRecipe);
        }

        return userRepo.save(newUser);
    }





    @Transactional
    @Override
    public User update(User user, long id)
    {
        if (user.getRecipes().size() > 0)
        {
            throw new ResourceFoundException("Recipes are not updated through this endpoint");
        }

        User currentUser = findUserById(id);

        if (helper.isAuthorizedToMakeChange(currentUser.getUsername())) {
            if (user.getUsername() != null) {
                currentUser.setUsername(user.getUsername().toLowerCase());
            }

            if (user.getPassword() != null) {
                currentUser.setPasswordNoEncrypt(user.getPassword());
            }

            if (user.getEmail() != null) {
                currentUser.setEmail(user.getEmail().toLowerCase());
            }

            if (user.getProfilepicture() != null) {
                currentUser.setProfilepicture(user.getProfilepicture());
            }

            if (user.getRoles().size() > 0) {
                currentUser.getRoles().clear();
                for (UserRoles ur : user.getRoles()) {
                    Role addRole = roleService.findRoleById(ur.getRole().getRoleid());

                    currentUser.getRoles().add(new UserRoles(currentUser, addRole));
                }
            }

            return userRepo.save(currentUser);
        } else {
            // note we should never get to this line but is needed for the compiler
            // to recognize that this exception can be thrown
            throw new ResourceNotFoundException("This user is not authorized to make change");
        }
    }




    @Transactional
    @Override
    public void deleteAll() {
        userRepo.deleteAll();
    }

    @Override
    public User getCurrentUser() {
        return this.findByName(SecurityContextHolder.getContext()
                .getAuthentication()
                .getName());
    }

}
