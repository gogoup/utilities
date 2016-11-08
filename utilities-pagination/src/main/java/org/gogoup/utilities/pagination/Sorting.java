package org.gogoup.utilities.pagination;

/**
 * Created by ruisun on 2015-10-23.
 */
public class Sorting {

    private String field;
    private Order order;

    public Sorting(String field, Order order) {
        this.field = field;
        this.order = order;
    }

    public String getField() {
        return field;
    }

    public Order getOrder() {
        return order;
    }

    public static enum Order {
        ASC,
        DESC
    }

}
