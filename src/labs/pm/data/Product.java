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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

import static labs.pm.data.Rating.*;

/**
 * {@code Product} class represents properties and behavious of
 * product objects in the Product Management System.
 * <br>
 * Each prodcut has an id, name and price
 * <br>
 * Each product can have a discount, calculated based on a
 * {@link }
 * @version 4.0
 * @author sarahwang93
 */

 public abstract class Product implements Rateable<Product>{
    /**
     * A constant that defines a {@link java.math.BigDecimal Bigdecimal} value
     * of the discount rate
     * <br>
     * Discount rate is 10%
     */
    public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);
    private int id;
    private String name;
    private BigDecimal price;
    private Rating rating;

    Product(){
        this(0, "no name", BigDecimal.ZERO);
    }

    Product(int id, String name, BigDecimal price, Rating rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    Product(int id, String name, BigDecimal price) {
        //matching with default constrcutor
        this(id, name, price, NOT_RATED);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
      //  price = BigDecimal.ONE;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Calculates discount based on a product price and
     * {@link}
     *
     * @return a {@link java.math.BigDecimal BigDecimal} value of the discount
     */
    public BigDecimal getDiscount(){
    //discount calculation displays here
        return price.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public Rating getRating() {
        return rating;
    }

    public abstract Product applyRating(Rating newRating);
        //return new Product(this.id, this.name, this.price, newRating);
    
    public LocalDate getBestBefore(){
        return LocalDate.now();
    }

    @Override
    public String toString() {
        return  id +
                " " + name  +
                " " + getDiscount() +
                " " + rating.getStars() + " " + getBestBefore();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        //if (o == null || getClass() != o.getClass()) return false;
        if(obj instanceof Product){
            final Product product = (Product) obj;
            return id == product.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
