package com.lambdaschool.cookbook.repositories;


import com.lambdaschool.cookbook.models.Recipe;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RecipeRepo
        extends PagingAndSortingRepository<Recipe, Long>,
        JpaSpecificationExecutor<Recipe> {

    public List<Recipe> findAllByUser_userid(Long uid);

}
