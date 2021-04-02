package com.lambdaschool.cookbook.handlers;

import com.lambdaschool.cookbook.exceptions.ResourceNotFoundException;
import com.lambdaschool.cookbook.models.err.ValidationError;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

@Component
public class HelperFunctions {
    /**
     * A public field used to store data from another API. This will have to be populated each time the application is run.
     * Population is done manually for each country code using an endpoint.
     */
    public List<ValidationError> getConstraintViolation(Throwable cause) {
        // Find any data violations that might be associated with the error and report them
        // data validations get wrapped in other exceptions as we work through the Spring
        // exception chain. Hence we have to search the entire Spring Exception Stack
        // to see if we have any violation constraints.
        while ((cause != null) && !(cause instanceof org.hibernate.exception.ConstraintViolationException ||
                cause instanceof MethodArgumentNotValidException
        )) {
            System.out.println(cause.getClass().toString());
            cause = cause.getCause();
        }

        List<ValidationError> listVE = new ArrayList<>();

        // we know that cause either null or an instance of ConstraintViolationException
        if (cause != null) {
            if (cause instanceof org.hibernate.exception.ConstraintViolationException) {
                org.hibernate.exception.ConstraintViolationException ex = (ConstraintViolationException) cause;
                ValidationError newVe = new ValidationError();
                newVe.setCode(ex.getMessage());
                newVe.setMessage(ex.getConstraintName());
                listVE.add(newVe);
            } else {
                MethodArgumentNotValidException ex = (MethodArgumentNotValidException) cause;
                List<FieldError> fieldErrors = ex.getBindingResult()
                        .getFieldErrors();
                for (FieldError err : fieldErrors) {
                    ValidationError newVe = new ValidationError();
                    newVe.setCode(err.getField());
                    newVe.setMessage(err.getDefaultMessage());
                    listVE.add(newVe);
                }
            }

        }
        return listVE;
    }




    public boolean isAuthorizedToMakeChange(String username) {
        // Check to see if the user whose information being requested is the current user
        // Check to see if the requesting user is an admin
        // if either is true, return true
        // otherwise stop the process and throw an exception
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (username.equalsIgnoreCase(authentication.getName()
                .toLowerCase()) || authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            // this user can make this change
            return true;
        } else {
            // stop user is not authorized to make this change so stop the whole process and throw an exception
            throw new ResourceNotFoundException(authentication.getName() + " not authorized to make change");
        }
    }
}
