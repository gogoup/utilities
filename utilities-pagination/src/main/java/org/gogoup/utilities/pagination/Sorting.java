package org.gogoup.utilities.pagination;

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

    public enum Order {
        ASC,
        DESC
    }

}
