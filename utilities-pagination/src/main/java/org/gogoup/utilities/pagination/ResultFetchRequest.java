package org.gogoup.utilities.pagination;

/**
 * Created by ruisun on 2015-06-01.
 */
public class ResultFetchRequest {

    private String id;
    private String key;
    private Object[] arguments;
    private PageOffset pageOffset;

    public ResultFetchRequest(String id, String key, Object[] arguments, PageOffset pageOffset) {
        this.id = id;
        this.key = key;
        this.arguments = arguments;
        this.pageOffset = pageOffset;
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
}
