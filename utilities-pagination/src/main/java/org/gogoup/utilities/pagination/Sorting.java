package org.gogoup.utilities.pagination;

/**
 * Created by ruisun on 2015-10-23.
 */
public class Sorting {

    private String field;
    private Order order;

    private Sorting() {

    }

    public Sorting on(String field) {
        this.field = field;
        return this;
    }

    public Sorting by(Sorting.Order order) {
        this.order = order;
        return this;
    }

    public String getField() {
        return field;
    }

    public Order getOrder() {
        return order;
    }

    public static Sorting build() {
        return new Sorting();
    }

    public enum Order {
        ASC,
        DESC,
        DEFAULT
    }

}
