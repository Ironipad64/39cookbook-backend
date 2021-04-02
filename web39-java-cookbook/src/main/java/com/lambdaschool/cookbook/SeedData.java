package com.lambdaschool.cookbook;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.lambdaschool.cookbook.models.*;
import com.lambdaschool.cookbook.repositories.IngredientRepo;
import com.lambdaschool.cookbook.repositories.RecipeRepo;
import com.lambdaschool.cookbook.services.RecipeServiceImpl;
import com.lambdaschool.cookbook.services.RoleService;
import com.lambdaschool.cookbook.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@ConditionalOnProperty(prefix = "command.line.runner", value = "enabled", havingValue = "true", matchIfMissing = true)
@Component
public class SeedData  implements CommandLineRunner {

    private final UserService userService;
    private final RecipeRepo recipeRepo;
    private final RecipeServiceImpl recipeService;
    private final RoleService roleService;
    private final IngredientRepo ingredientRepo;

    @Autowired
    public SeedData(
            UserService userService,
            RecipeRepo recipeRepo,
            RecipeServiceImpl recipeService,
            RoleService roleService,
            IngredientRepo ingredientRepo
    ) {
        this.userService = userService;
        this.recipeRepo = recipeRepo;
        this.roleService = roleService;
        this.recipeService = recipeService;
        this.ingredientRepo = ingredientRepo;
    }

    @Override
    public void run(String... args)
            throws Exception {
        userService.deleteAll();
        roleService.deleteAll();
        ingredientRepo.deleteAll();
        recipeRepo.deleteAll();

        Role roleAdmin = new Role("admin");
        roleService.save(roleAdmin);
        Role roleUser = new Role("user");
        roleService.save(roleUser);

        /**
         * ISMAEL
         * ROLE: ADMIN
         */
        {
            User ismael = new User("ismael", "password", "ismael@mail.com");
            ismael.getRoles()
                    .add(new UserRoles(ismael, roleAdmin));
            userService.save(ismael);
        }


        /**
         * USER -- shrek
         * ROLE: ADMIN
         */
        {
            // DECLARE USER
            User shrek = new User("shrek",
                    "password",
                    "shrek@mail.com",
                    "https://images.dog.ceo/breeds/african/n02116738_10640.jpg"
            );
            shrek.getRoles().add(new UserRoles(shrek, roleAdmin));


            // COOKIES 1 RECIPE
            Categories ctgCookie = new Categories("dessert", "cookie", "american", "gluten free", "bake");
            Ingredient igCookie1 = new Ingredient("chocolate chips", 2, "cups");
            Ingredient igCookie2 = new Ingredient("sugar", 2, "cups");
            Ingredient igCookie3 = new Ingredient("butter", 4, "sticks");

            String instCookie1 = "1. bake the cookies \n 2. eat the cookies";
            String cookiesImage = "https://images-gmi-pmc.edge-generalmills.com/087d17eb-500e-4b26-abd1-4f9ffa96a2c6.jpg";

            Recipe rcCookie1 = new Recipe("cookies", false, instCookie1, "dad");

            rcCookie1.setPictureurl(cookiesImage);

            rcCookie1.getIngredients().add(igCookie1);
            rcCookie1.getIngredients().add(igCookie2);
            rcCookie1.getIngredients().add(igCookie3);

            rcCookie1.setCategories(ctgCookie);
            shrek.getRecipes().add(rcCookie1);

            // PEANUT BUTTER BAGEL RECIPE
            Categories ctgPbBagel = new Categories("snack breakfast lunch dinner", "bagel", null, "quick&easy", "toast");
            Ingredient igPbBagel1 = new Ingredient("Thomas bagel", 1, "bagel");
            Ingredient igPbBagel2 = new Ingredient("peanut butter", 3, "dollops");
            String instPbBagel = "1. Turn the dial on the toaster down (b/c you already know my dad put that shit on max). " +
                    "\n" + "2. Toast the bagel to a light golden hue \n" + "3. Scoop out 3 fat scoops of PB \n" +
                    "4. Apply the peanut butter with lots of love and spread until evenly applied. \n" +
                    "5. Eat that bagel like it's the best thing you've ever set eyes on. ";
            Recipe pbBagel = new Recipe("peanut butter bagel", false, instPbBagel);

            pbBagel.getIngredients().addAll(Arrays.asList(igPbBagel1, igPbBagel2));
            pbBagel.setCategories(ctgPbBagel);
            shrek.getRecipes().add(pbBagel);

            // CHOCOLATE MILK RECIPE
            Categories ctgChocomilk = new Categories("beverage snack", "beverage", "magical", null, "stir");
            Ingredient igChocmilk1 = new Ingredient("2% milk", 1, "glass");
            Ingredient igChocmilk2 = new Ingredient("Nesquick chocolate powder", 3, "tbsp");
            String instChocmilk = "1. Fill a large glass to 87% capacity with the milk \n" +
                    "2. Pour all 3 tablespoons of Nesquick choco powder into the glass \n" +
                    "3. Stir thoroughly";
            Recipe chocoMilk = new Recipe("rees' silky chocolate milk", false, instChocmilk);
            chocoMilk.getIngredients().addAll(Arrays.asList(igChocmilk1, igChocmilk2));
            chocoMilk.setCategories(ctgChocomilk);
            shrek.getRecipes().add(chocoMilk);


            // SAVE USER
            userService.save(shrek);
        }


        /**
         * USER -- Snoop Dog
         * ROLE: USER
         * */
        {
            User snoop = new User("snoopdoggydog",
                    "snoopburnstoo",
                    "snoop@doggydog.com",
                    "https://static.billboard.com/files/media/Snoop-Dogg-cr-Kenneth-Cappello-billboard-1548-compressed.jpg"
            );

            snoop.getRoles().add(new UserRoles(snoop, roleUser));

            /*
             * START POT BROWNIES 1 RECIPE — SNOOP DOG
             * */
            Categories ctgPotBrownies = new Categories("dessert, edible-gift", null, "american", "cannabis", "baked (lol)");


            Ingredient pbIng1 = new Ingredient("cannabis butter", 8, "oz");
            Ingredient pbIng2 = new Ingredient("butter", 5, "sticks");
            Ingredient pbIng3 = new Ingredient("eggs", 16, "eggs");
            Ingredient pbIng4 = new Ingredient("sugar", 6, "cups");
            Ingredient pbIng5 = new Ingredient("baking powder", 3, "tbsp");
            Ingredient pbIng6 = new Ingredient("salt", 1.3, "tsp");
            Ingredient pbIng7 = new Ingredient("unsweetened baking chocolate", 16, "oz");

            String potBrownieInstructions =
                    "1. Melt butter in a double boiler, and stir in the cannabis butter. This is the " + "\"Ghee. " + "\"\n " +
                            "2. Combine eggs and sugar in a large bowl. \n" + "3. Slowly melt chocolate in another double boiler. \n" +
                            "4. When the Ghee has cooked for 30min., add flour, powder, salt to eggs and " +
                            "sugar. Then add the Ghee and the chocolate. \n" + "5. Pour into four 9\"x12\" greased baking pans. \n" +
                            "6. Bake at 420F, patting down the batter several times with a spatula to keep it" +
                            " from rising. When the brownies are solid, but still very moist, remove from " +
                            "oven and cover with a towel to keep moisture in. \n...Yields 8 dozen 2\"x2\" " + "brownies...";

            Recipe potBrownies = new Recipe("pot brownies", false, potBrownieInstructions, "grandma snoop");
            potBrownies.setPictureurl("https://image.cannabis.wiki/news/istock-940720360-1570168541-1440w.jpeg");
            potBrownies.getIngredients()
                    .addAll(Arrays.asList(pbIng1, pbIng2, pbIng3, pbIng4, pbIng5, pbIng6, pbIng7));
            potBrownies.setCategories(ctgPotBrownies);

            snoop.getRecipes().add(potBrownies);
            userService.save(snoop);
            /*
             * END POT BROWNIES 1 RECIPE — SNOOP DOG
             * */
        }


        /**
         * USER -- Betty White
         * ROLE: USER
         */
        {
            User betty = new User("bettywhite",
                    "bettyburns",
                    "bettywhite@bettywhite.com",
                    "https://www.google" +
                            ".com/url?sa=i&url=https%3A%2F%2Fwww.biography.com%2Factor%2Fbetty-white&psig=AOvVaw1mh8zFEls2iyXbaTQMXDMz&ust=1608588910011000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCNDyqefK3e0CFQAAAAAdAAAAABAD"
            );

            betty.getRoles().add(new UserRoles(betty, roleUser));

            // Recipe 1 — Betty's Moist Chocolate Cake
            Categories ctgCake = new Categories();
            ctgCake.setCourse("dessert");
            ctgCake.setDishtype("cake");
            ctgCake.setCuisine("german");
            ctgCake.setTechnique("bake");

            Ingredient igCake1 = new Ingredient("Crisco", 1, "cups");
            Ingredient igCake2 = new Ingredient("white sugar", 2, "cups");
            Ingredient igCake3 = new Ingredient("eggs", 2, "eggs");
            Ingredient igCake4 = new Ingredient("cocoa", 0.5, "cups");
            Ingredient igCake5 = new Ingredient("sour milk", 1, "cups");
            Ingredient igCake6 = new Ingredient("vanilla", 1, "tsp");
            Ingredient igCake7 = new Ingredient("sifted flour", 2.5, "cups");
            Ingredient igCake8 = new Ingredient("salt", 1.5, "tsp");
            Ingredient igCake9 = new Ingredient("boiling water", 1, "cups");

            String cakeInstructions =
                    "Mix together Crisco, sugar and eggs, then add cocoa. Put baking soda in sour milk. Add vanilla, flour and salt and the boiling water goes into mixture last.\n" +
                            "Bake at 375°F for 35 to 40 minutes in 9 x 13 pan.";
            Recipe bettysCake = new Recipe();
            bettysCake.setCategories(ctgCake);
            bettysCake.setPrivate(false);
            bettysCake.setInstructions(cakeInstructions);
            bettysCake.setPictureurl("https://thestayathomechef.com/wp-content/uploads/2016/06/The-Most-Amazing-Chocolate-Cake-Square-1.jpg");
            bettysCake.setTitle("Betty's Moist Chocolate Cake");
            bettysCake.getIngredients().addAll(Arrays.asList(igCake1, igCake2, igCake3, igCake4, igCake5, igCake6, igCake7, igCake8, igCake9));

            betty.getRecipes().add(bettysCake);

            // RECIPE 2 — DUMP CAKE
            Categories ctgDumpcake = new Categories();
            ctgDumpcake.setCourse("dessert");
            ctgDumpcake.setDishtype("cake");
            ctgDumpcake.setCuisine("southern");
            ctgDumpcake.setTechnique("bake");
            Ingredient igDump1 = new Ingredient("box of yellow or white cake mix", 1, "box");
            Ingredient igDump2 = new Ingredient("cherry pie filling", 1, "cup");
            Ingredient igDump3 = new Ingredient("pineapple chunks, drained", 1, "cup");
            Ingredient igDump4 = new Ingredient("shredded coconut", 2, "cups");
            Ingredient igDump5 = new Ingredient("chopped walnuts", 2, "cups");
            Ingredient igDump6 = new Ingredient("melted butter", 0.5, "lbs");
            String dumpcakeInstructions = "Bake in 350°F oven until apples are tender - approximately 30 minutes.";

            Recipe dumpCake = new Recipe("Betty's Dump Cake", false, dumpcakeInstructions);
            dumpCake.setPictureurl("https://dearcrissy.com/wp-content/uploads/2019/08/cherry-pineapple-dump-cake-5-735x1103.jpg");
            dumpCake.getIngredients().addAll(Arrays.asList(igDump1, igDump2, igDump3, igDump4, igDump5, igDump6));
            dumpCake.setCategories(ctgDumpcake);
            betty.getRecipes().add(dumpCake);

            // RECIPE 3 — STICKY ROASTED CHICKEN
            Categories ctgChick = new Categories();
            ctgChick.setCourse("dinner");
            ctgChick.setDishtype("chicken");
            ctgChick.setCuisine("southern");
            ctgChick.setDietaryconcerns("gluten free, wheat free");
            ctgChick.setTechnique("roast");

            Ingredient igChick1 = new Ingredient("salt", 2, "tsp");
            Ingredient igChick2 = new Ingredient("paprika", 1, "tsp");
            Ingredient igChick3 = new Ingredient("cayenne pepper", 0.75, "tsp");
            Ingredient igChick4 = new Ingredient("onion powder", 0.5, "tsp");
            Ingredient igChick5 = new Ingredient("thyme", 0.5, "tsp");
            Ingredient igChick6 = new Ingredient("white pepper", 0.25, "tsp");
            Ingredient igChick7 = new Ingredient("garlic powder", 0.25, "tsp");
            Ingredient igChick8 = new Ingredient("black pepper", 0.25, "tsp");
            Ingredient igChick9 = new Ingredient("chicken (whole)", 3, "lbs");
            Ingredient igChick10 = new Ingredient("chopped onion", 1, "cup");

            String chickInstructions = "In a small bowl, mix all seasonings. Rub mixture into chicken - inside and out - patting mixture into skin to make sure it is evenly distributed and down deep into the skin. Place in sealed plastic bag, seal and refrigerate overnight. Stuff cavity with onions. Roast uncovered at 250 degrees for about 5 hours, basting occasionally with pan juices until chicken is golden brown.";

            Recipe chicken = new Recipe("Betty's Sticky Roasted Chicken", false, chickInstructions);
            chicken.setPictureurl(
                    "https://lh4.ggpht.com/_dfV_Eqz0Ypw/TUidWdXO6FI/AAAAAAAABlE/2vAARV2OpZ0/sweet%20sticky%20chicken%20drumsticks%5B4%5D.jpg?imgmax=800");
            betty.getRecipes()
                    .add(chicken);
            chicken.getIngredients()
                    .addAll(Arrays.asList(igChick1,
                            igChick2,
                            igChick3,
                            igChick4,
                            igChick5,
                            igChick6,
                            igChick7,
                            igChick8,
                            igChick9,
                            igChick10
                    ));
            chicken.setCategories(ctgChick);
            userService.save(betty);
        }
    }



    private double parse(String ratio) {
        if (ratio.contains("/")) {
            String[] rat = ratio.split("/");
            return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
        } else {
            return Double.parseDouble(ratio);
        }
    }
}
