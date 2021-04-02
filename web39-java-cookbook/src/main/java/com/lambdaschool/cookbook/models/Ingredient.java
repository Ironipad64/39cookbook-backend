package com.lambdaschool.cookbook.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;


@Entity
@Table(name = "ingredients")
@JsonIgnoreProperties(value = "ingredientid")
public class Ingredient extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ingredientid;

    @Column(nullable = false)
    private String ingredientname;

    @Column(nullable = false)
    private double quantity;

    @Column(nullable = false)
    private String measurement;

    @ManyToOne
    @JoinColumn(name = "recipeid")
    @JsonIgnoreProperties(value = "ingredients", allowSetters = true)
    private Recipe recipe;

    public Ingredient() {
    }

    public Ingredient(
            String ingredientname,
            double quantity,
            String measurement
    ) {
        this.ingredientname = ingredientname;
        this.quantity = quantity;
        this.measurement = measurement;
    }

    public long getIngredientid() { return ingredientid; }

    public void setIngredientid(long ingredientid) { this.ingredientid = ingredientid; }

    public String getIngredientname() { return ingredientname; }

    public void setIngredientname(String ingredientname) { this.ingredientname = ingredientname; }

    public double getQuantity() { return quantity; }

    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getMeasurement() { return measurement; }

    public void setMeasurement(String measurement) { this.measurement = measurement; }

    public Recipe getRecipe() { return recipe; }

    public void setRecipe(Recipe recipe) { this.recipe = recipe; }

//    @Override
//    public String toString() {
//        String singredientname = ingredientname == null ? "null" : ingredientname;
//        String smeasurement = measurement == null ? "null" : measurement;
//        //		String srecipe = recipe == null ? "null" : recipe.toString();
//
//        return "Ingredient{" + "ingredientid=" + ingredientid + ", ingredientname='" + singredientname + '\'' +
//                ", quantity=" + quantity + ", measurement='" + smeasurement + '\'' + '}';
//    }
}
