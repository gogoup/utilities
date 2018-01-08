package org.gogoup.utilities.pagination;

import java.util.*;

public abstract class PaginatedResultMapper<T, S> implements PaginatedResultDelegate<T> {

    public final static int RESULT_INDEX = 0;
    
    private int paginatedResultIndex;
    private Set<String> keys;

    protected PaginatedResultMapper(String... keys) {
        this(RESULT_INDEX, keys);
    }
    
    protected PaginatedResultMapper(int paginatedResultIndex, String... keys) {
        this.paginatedResultIndex= paginatedResultIndex;
        this.keys = new HashSet<>(Arrays.asList(keys));
    }

    @Override
    public T fetchResult(ResultFetchRequest request) {
        checkForUnsupportedKeys(request.getKey());
        @SuppressWarnings("unchecked")
        PaginatedResult<S> result = (PaginatedResult<S>) request.getArguments()[paginatedResultIndex];
        result = result.start(request.getPageOffset(), request.getSorting());
        return toResult(request.getKey(), result.getResult(), request.getArguments());
    }

    abstract protected T toResult(String key, S values, Object[] arguments);
    
    private void checkForUnsupportedKeys(String key) {
        if (!keys.contains(key)) {
            throw new UnsupportedOperationException(key);
        }
    }
}
