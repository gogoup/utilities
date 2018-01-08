package org.gogoup.utilities.pagination;

public class ResultFetchRequest {

    private String id;
    private String key;
    private Object[] arguments;
    private PageOffset pageOffset;
    private Sorting sorting;

    public ResultFetchRequest(String id, String key, Object[] arguments, PageOffset pageOffset, Sorting sorting) {
        this.id = id;
        this.key = key;
        this.arguments = arguments;
        this.pageOffset = pageOffset;
        this.sorting = sorting;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public PageOffset getPageOffset() {
        return pageOffset;
    }

    public Sorting getSorting() {
        return sorting;
    }
}
