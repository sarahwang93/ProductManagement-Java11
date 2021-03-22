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
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author sarahwang93
 */
public final class Food extends Product{

    Food(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        //call constructor from parent class
        super(id, name, price, rating);
        this.bestBefore = bestBefore;
    }

    private LocalDate bestBefore;

    /**
     * Get the value of the best before date for the product
     *
     * @return the value of bestBefore
     */
    public LocalDate getBestBefore() {
        return bestBefore;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + bestBefore;
    }

    @Override
    public BigDecimal getDiscount() {
        LocalTime now = LocalTime.now();
        return (now.isAfter(LocalTime.of(17,30)) && now.isBefore(LocalTime.of(18,30))) ? super.getDiscount() : BigDecimal.ZERO;
    }

    @Override
    public Product applyRating(Rating newRating) {
        return new Food(getId(), getName(), getPrice(), newRating, bestBefore);
    }
}
