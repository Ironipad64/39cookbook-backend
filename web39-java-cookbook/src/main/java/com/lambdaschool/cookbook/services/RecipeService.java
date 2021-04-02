package com.lambdaschool.cookbook.services;


import com.lambdaschool.cookbook.models.Recipe;
//import com.lambdaschool.cookbook.models.utils.PagingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.net.http.HttpHeaders;
import java.util.List;


public interface RecipeService {
    Page<Recipe> findRecipes();

    Recipe get(Long id);

//    PagingResponse get(Specification<Recipe> spec, HttpHeaders headers, Sort sort);

    List<Recipe> get(Specification<Recipe> spec, Sort sort);

//    PagingResponse get(Specification<Recipe> spec, Pageable pageable);

    Recipe save(Recipe recipe);

    Recipe create(Recipe item);

    Recipe update(Recipe item);
}

