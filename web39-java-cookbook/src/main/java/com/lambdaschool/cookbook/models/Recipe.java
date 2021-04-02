package com.lambdaschool.cookbook.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "recipes")
public class Recipe
        extends Auditable
        implements Serializable {

    @JsonIgnore
    private final String DEFAULT_URL = "https://www.thermaxglobal.com/wp-content/uploads/2020/05/image-not-found.jpg";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long recipeid;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean isPrivate;

    @Column
    private String source = "SELF";

    //	@Column(columnDefinition = "TEXT")
    //	@Lob
    @Column(nullable = false, length = 10000)
    private String pictureurl = DEFAULT_URL;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties(value = "recipe", allowSetters = true)
    private List<Ingredient> ingredients = new ArrayList<>();

    // 10485761 max char length

    //	@Lob

    //	@Column(nullable = false, columnDefinition = "TEXT")

    //	@Column(nullable = false, length = 10000)

    @Lob
    @Column(nullable = false)
    private String instructions;
    @Embedded
    private Categories categories;

    @Transient
    private boolean hasPrivate = false;

    @ManyToOne
    @JoinColumn(name = "userid")
    @JsonIgnoreProperties(value = "recipes", allowSetters = true)
    private User user;

    public Recipe() {
    }

    public Recipe(
            String title,
            boolean isPrivate,
            String source,
            String pictureurl,
            String instructions
    ) {
        this.title = title;
        this.isPrivate = isPrivate;
        this.hasPrivate = true;
        this.source = source;
        this.pictureurl = pictureurl;
        this.instructions = instructions;
    }

    public Recipe(
            String title,
            boolean isPrivate,
            String instructions
    ) {
        this.title = title;
        this.isPrivate = isPrivate;
        this.hasPrivate = true;
        this.instructions = instructions;
        //		this.source       = "self";
        //		this.url          = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.blankstyle" +
        //		                    ".com%2Fvalucap-vc25y-valumask-youth-adjustable&psig=AOvVaw33kVJFr9L9yVPOnBP7BS_v&ust=1608661153211000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCKDUgfjX3-0CFQAAAAAdAAAAABAI";
    }

    public Recipe(
            String title,
            boolean isPrivate,
            String instructions,
            String source
    ) {
        this.title = title;
        this.isPrivate = isPrivate;
        this.hasPrivate = true;
        this.instructions = instructions;
        this.source = source;
        this.pictureurl = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.blankstyle" +
                ".com%2Fvalucap-vc25y-valumask-youth-adjustable&psig=AOvVaw33kVJFr9L9yVPOnBP7BS_v&ust=1608661153211000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCKDUgfjX3-0CFQAAAAAdAAAAABAI";
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    @JsonIgnore
    public void setCheckAll( @org.jetbrains.annotations.NotNull Recipe recipe)
    {
        if (recipe.getRecipeid() >= 0) {
            setRecipeid(recipe.getRecipeid());
        }
        if (recipe.getTitle() != null) {
            setTitle(recipe.getTitle());
        }
        if (recipe.getInstructions() != null) {
            setInstructions(recipe.getInstructions());
        }
        if (recipe.hasPrivate) {
            setPrivate(recipe.isPrivate());
        }
        if (recipe.getUser() != null) {
            setUser(recipe.getUser());
        }
        //		if (recipe.getIngredients().size() > 0) {
        //			this.getIngredients().clear();
        //			for (Ingredient ing : recipe.getIngredients()) {
        //
        //			}
        //			setIngredients(recipe.getIngredients());
        //		}
        if (recipe.getCategories() != null) {
            this.setCategories(recipe.getCategories());
        } else {
            Categories nullCategories = new Categories(null, null, null, null, null);
            this.setCategories(nullCategories);
        }
        if (recipe.getPictureurl() != null) {
            setPictureurl(recipe.getPictureurl());
        }
        if (recipe.getSource() != null) {
            setSource(recipe.getSource());
        }

    }





    public long getRecipeid() {
        return recipeid;
    }

    public void setRecipeid(long recipeid) {
        this.recipeid = recipeid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructions() {
        return instructions;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
        this.hasPrivate = true;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public String getPictureurl() {
        return pictureurl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setPictureurl(String pictureurl) {
        this.pictureurl = pictureurl;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        String sCreatedDate = "" + ((createdDate == null) ? "null" : createdDate);
        String sLastModifiedDate = "" + ((lastModifiedBy == null) ? "null" : lastModifiedBy);
        String sTitle = "" + ((title == null) ? "null" : title);
        String sPrivate = "" + isPrivate;
        String sSource = "" + ((source == null) ? "null" : source);
        String sPictureurl = "" + ((pictureurl == null) ? "null" : pictureurl);
        String sInstructions = "" + ((instructions == null) ? "null" : instructions);
        String sCategories = "" + ((categories == null) ? "null" : categories);
        String sUser = "" + ((user == null) ? "null" : user);
        String sIngredients;
        if (ingredients.size() > 0) {
            sIngredients = ingredients.stream()
                    .map(Ingredient::toString)
                    .collect(Collectors.joining("-", "{", "}"));
        } else {
            sIngredients = "None";
        }

        return "Recipe{" + "createdDate=" + sCreatedDate + ", lastModifiedDate=" + sLastModifiedDate + ", recipeid=" +
                recipeid + ", title='" + sTitle + '\'' + ", isPrivate=" + sPrivate + ", source='" + sSource + '\'' +
                ", pictureurl='" + sPictureurl + '\'' + ", ingredients=" + sIngredients + '\'' + ", instructions='" +
                sInstructions + '\'' + ", categories=" + sCategories + ", user=" + sUser + '}';
    }

}
