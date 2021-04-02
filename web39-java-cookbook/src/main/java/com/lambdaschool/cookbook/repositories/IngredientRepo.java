package com.lambdaschool.cookbook.repositories;


import com.lambdaschool.cookbook.models.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IngredientRepo
        extends CrudRepository<Ingredient, Long> {
}
