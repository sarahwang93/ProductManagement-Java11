/*
 * Copyright (c) 2021. *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *            http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package labs.pm.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;


public class ProductManager {

    private Product product;
    //private Review[] reviews = new Review[5];
    private Map<Product, List<Review>> products = new HashMap<>();
    private ResourceFormatter formatter;
    private static Map<String, ResourceFormatter> formatters
            = Map.of("en-GB", new ResourceFormatter(Locale.UK),
            "en-US", new ResourceFormatter(Locale.US),
            "fr-FR", new ResourceFormatter(Locale.FRANCE),
            "ru-RU", new ResourceFormatter(new Locale("ru", "RU")),
            "zh-CN", new ResourceFormatter(Locale.CHINA));


    public ProductManager(Locale locale) {
        this(locale.toLanguageTag());
    }

    public ProductManager(String languageTag) {
        changeLocale(languageTag);
    }

    public static Set<String> getSupportedLocales(){
        return formatters.keySet();
    }

    public void changeLocale(String languageTag){
        formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));
    }
    //create instance of product
    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore){
        Product product = new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating){
        Product product = new Drink(id, name, price, rating);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product reviewProduct(int id, Rating rating, String comments){
        return reviewProduct(findProduct(id), rating, comments);
    }

    public Product reviewProduct(Product product, Rating rating, String comments){
       /* if(reviews[reviews.length -1]!=null){
            reviews = Arrays.copyOf(reviews, reviews.length + 5);
        }*/
      //  review = new Review(rating, comments);
        List<Review> reviews = products.get(product);
        products.remove(product, reviews);
        reviews.add(new Review(rating, comments));
        int sum = 0;
        for(Review review:reviews){
            sum += review.getRating().ordinal();
        }
        product = product.applyRating(Rateable.convert(Math.round((float)sum/reviews.size())));
        products.put(product, reviews);
        return product;
    }

    public Product findProduct(int id){
        Product result = null;
        for(Product product: products.keySet()){
            if(product.getId() == id){
                result = product;
                break;
            }
        }
        return result;
    }

    public void printProductReport(int id){
        printProductReport(findProduct(id));
    }

    public void printProductReport(Product product){
        List<Review> reviews = products.get(product);
        //formatting and printing logic
        StringBuilder txt = new StringBuilder();
        //print the first title of product with average review information
        txt.append(formatter.formatProduct(product));
        txt.append('\n');
        Collections.sort(reviews);
        //add each review with each line for the product defined
        for(Review review: reviews){
            //logic for appending reviews
            txt.append(formatter.formatReview(review));
            txt.append('\n');
            }
        if(reviews.isEmpty()){
            txt.append(formatter.getText("no.reviews"));
            txt.append('\n');
        }
        System.out.println(txt);
    }

    private static class ResourceFormatter{
        private Locale locale;
        private ResourceBundle resources;
        private DateTimeFormatter dateFormat;
        private NumberFormat moneyFormat;

        private ResourceFormatter(Locale locale){
            this.locale = locale;
            resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
            moneyFormat = NumberFormat.getCurrencyInstance(locale);
        }

        private String formatProduct(Product product){
            return MessageFormat.format(resources.getString("product"),
                    product.getName(),
                    moneyFormat.format(product.getPrice()),
                    product.getRating().getStars(),
                    dateFormat.format(product.getBestBefore())
            );
        }

        private String formatReview(Review review){
            return MessageFormat.format(resources.getString("review"),
                    review.getRating().getStars(),
                    review.getComments());
        }

        private String getText(String key){
            return resources.getString(key);
        }
    }
}
