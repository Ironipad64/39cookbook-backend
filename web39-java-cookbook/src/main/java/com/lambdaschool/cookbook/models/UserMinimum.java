package com.lambdaschool.cookbook.models;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;


@ApiModel
public class UserMinimum {

    /**
     * The username (String)
     */
    @ApiModelProperty(notes = "username for the new user (must not exist already)")
    private String username;

    /**
     * The user's password (String)
     */
    @ApiModelProperty(notes = "password for the new user (no requirements)")
    private String password;

    @ApiModelProperty(notes = "email must be in the traditional email format.")
    @Email
    private String email;

    /**
     * Getter for the username
     *
     * @return the username (String) associated with this user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for the username
     *
     * @param username the new username (String) associated with this user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the password of this user
     *
     * @return the password (String) for this user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for the password of this user. This object is a temporary model used to create a new user.
     * The password must remain in clear text until saved into the database.
     *
     * @param password the new password (String in clear texts) for this user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for the email of this user
     *
     * @return the email (String) for this user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for the email of this user. This object is a temporary model used to create a new user.
     *
     * @param email the new email (String in email format) for this user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

}
