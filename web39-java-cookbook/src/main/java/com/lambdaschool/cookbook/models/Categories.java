package com.lambdaschool.cookbook.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Embeddable;

@Embeddable
public class Categories {

    private String course;
    private String dishtype;
    private String cuisine;
    private String dietaryconcerns;
    private String technique;

    public Categories() {
        this.course = null;
        this.dishtype = null;
        this.cuisine = null;
        this.dietaryconcerns = null;
        this.technique = null;
    }

    public Categories(
            String course,
            String dishtype,
            String cuisine,
            String dietaryconcerns,
            String technique)
    {
        this.course = course;
        this.dishtype = dishtype;
        this.cuisine = cuisine;
        this.dietaryconcerns = dietaryconcerns;
        this.technique = technique;
    }

    @JsonIgnore
    public void setAll() {
        this.course = null;
        this.dishtype = null;
        this.cuisine = null;
        this.dietaryconcerns = null;
        this.technique = null;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDishtype() {
        return dishtype;
    }

    public void setDishtype(String dishtype) { this.dishtype = dishtype; }

    public String getCuisine() { return cuisine; }

    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    public String getDietaryconcerns() { return dietaryconcerns; }

    public void setDietaryconcerns(String dietaryconcerns) { this.dietaryconcerns = dietaryconcerns; }

    public String getTechnique() { return technique; }

    public void setTechnique(String technique) { this.technique = technique; }
}
