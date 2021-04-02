package com.lambdaschool.cookbook.services;


import com.lambdaschool.cookbook.exceptions.ResourceNotFoundException;
import com.lambdaschool.cookbook.models.Categories;
import com.lambdaschool.cookbook.models.Ingredient;
import com.lambdaschool.cookbook.models.Recipe;
import com.lambdaschool.cookbook.models.User;
import com.lambdaschool.cookbook.models.utils.PagingHeaders;
import com.lambdaschool.cookbook.models.utils.PagingResponse;
import com.lambdaschool.cookbook.repositories.IngredientRepo;
import com.lambdaschool.cookbook.repositories.RecipeRepo;
//import com.lambdaschool.cookbook.specs.UserRecipeSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service(value = "recipeService")
public class RecipeServiceImpl {

    private final RecipeRepo recipeRepo;
    private final IngredientRepo ingRepo;

    private final UserService userService;

    @Autowired
    public RecipeServiceImpl(
            RecipeRepo recipeRepo,
            IngredientRepo ingRepo,
            UserService userService
    ) {
        this.recipeRepo = recipeRepo;
        this.ingRepo = ingRepo;
        this.userService = userService;
    }

    public Optional<Recipe> findById(long id) {
        return recipeRepo.findById(id);
    }

    public void delete(Long id) {
        Recipe entity = recipeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(buildErrorMessage(id)));
        recipeRepo.delete(entity);
    }

    private String buildErrorMessage(Long id) {
        return String.format("Cannot find the entity recipe (%s=%s).", "id", id.toString());
    }

    public Recipe get(Long id) {
        return recipeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(buildErrorMessage(id)));
    }

    public PagingResponse get(
            Specification<Recipe> spec,
            HttpHeaders headers,
            Sort sort
    ) {
        if (isRequestPaged(headers)) {
            return get(spec, buildPageRequest(headers, sort));
        } else {
            List<Recipe> entities = get(spec, sort);
            //			Page<Recipe> page = get(spec, sort);

            return new PagingResponse((long) entities.size(), 0L, 0L, 0L, 0L, entities);
        }
    }

    private boolean isRequestPaged(HttpHeaders headers) {
        return headers.containsKey(PagingHeaders.PAGE_NUMBER.getName()) &&
                headers.containsKey(PagingHeaders.PAGE_SIZE.getName());
    }

    /**
     * Get elements using criteria
     *
     * @param spec     *
     * @param pageable pagination data
     * @return retrieve elements with pagination
     */
    public PagingResponse get(
            Specification<Recipe> spec,
            Pageable pageable
    ) {
        Page<Recipe> page = recipeRepo.findAll(spec, pageable);
        List<Recipe> content = page.getContent();
        return new PagingResponse(page.getTotalElements(),
                (long) page.getNumber(),
                (long) page.getNumberOfElements(),
                pageable.getOffset(),
                (long) page.getTotalPages(),
                content
        );
    }

    public List<Recipe> getUserRecipes() {
        User currentUser = userService.getCurrentUser();
        return recipeRepo.findAllByUser_userid(currentUser.getUserid());
    }

    public List<Recipe> getByUser(Long uid) {
        return recipeRepo.findAllByUser_userid(uid);
    }

    private Pageable buildPageRequest(
            HttpHeaders headers,
            Sort sort
    ) {
        int page = Integer.parseInt(Objects.requireNonNull(headers.get(PagingHeaders.PAGE_NUMBER.getName())
                .get(0)));
        int size = Integer.parseInt(Objects.requireNonNull(headers.get(PagingHeaders.PAGE_SIZE.getName())
                .get(0)));
        return PageRequest.of(page, size, sort);
    }

    /**
     * get elements using criteria
     *
     * @param spec *
     * @param sort criteria by which to sort
     * @return elements
     */
    public List<Recipe> get(
            Specification<Recipe> spec,
            Sort sort
    ) {
        return recipeRepo.findAll(spec, sort);
    }

    /**
     * create element
     *
     * @param recipe element to create
     * @return element after creation
     */
    public Recipe create(Recipe recipe) {
        recipe.setRecipeid(0);
        return save(recipe);
    }

    /**
     * create \ update elements
     *
     * @param recipe recipe to save
     * @return element after save
     */
    protected Recipe save(Recipe recipe) {
        Recipe newRecipe = new Recipe();

        if (recipe.getRecipeid() != 0) {
            newRecipe = recipeRepo.findById(recipe.getRecipeid())
                    .orElseThrow(() -> new ResourceNotFoundException(buildErrorMessage(recipe.getRecipeid())));
        }

        newRecipe.setCheckAll(recipe);

        if (recipe.getIngredients()
                .size() > 0) {
            newRecipe.getIngredients()
                    .clear();
            for (Ingredient ing : recipe.getIngredients()) {
                Ingredient newIngredient = new Ingredient();
                newIngredient.setIngredientname(ing.getIngredientname());
                newIngredient.setMeasurement(ing.getMeasurement());
                newIngredient.setQuantity(ing.getQuantity());
                newIngredient.setRecipe(newRecipe);
                Ingredient savedIng = ingRepo.save(newIngredient);
                newRecipe.getIngredients()
                        .add(savedIng);
            }
        }

        return recipeRepo.save(newRecipe);
    }

    public Recipe update(
            Long id,
            Recipe item
    ) {
        item.setRecipeid(id);
        if (item.getRecipeid() <= 0) {
            throw new RuntimeException("Can not update entity, entity without ID");
        } else if (!id.equals(item.getRecipeid())) {
            throw new RuntimeException(String.format(
                    "Can not update entity, the resource ID (%s) not match the object ID " + "(%s).", id, item.getRecipeid()));
        }
        return save(item);
    }
}
