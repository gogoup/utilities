/*******************************************************************************
 * Copyright 2014 Rui Sun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.gogoup.utilities.pagination;

public class PaginatedResult<T> {
    
    public static final Object NONE_PAGE_CURSOR = null;

    private PaginatedResultDelegate<T> delegate;
    private String key;
    private Object[] arguments;
    private Object currentPageCursor;
    private T result;

    public PaginatedResult(PaginatedResultDelegate<T> delegate, String key, Object... arguments) {
        this.key = key;
        this.arguments = arguments;
        this.delegate = delegate;
        this.currentPageCursor = NONE_PAGE_CURSOR;
    }
    
    /**
     * Retrieve the result at the giving page cursor.
     * 
     * Returns the first page of result, if the giving page cursor is null.
     * 
     * @param pageCursor Object
     * @return T
     */
    public T getResult(Object pageCursor) {
        setCurrentPageCursor(pageCursor);
        return getResult();
    }
    
    public T getResult() {
        checkForNoPageCursor(currentPageCursor);
        checkForNullDelegate();
        result = delegate.fetchResult(key, arguments, currentPageCursor); 
        return result;
    }
    
    public PaginatedResult<T> next() {
        setCurrentPageCursor(getNextPageCursor());
        return this;
    }
    
    public PaginatedResult<T> previous() {
        setCurrentPageCursor(getPrevPageCursor());
        return this;
    }
    
    public PaginatedResult<T> rewind() {
        setCurrentPageCursor(getFirstPageCursor());
        return this;
    }
    
    private void setCurrentPageCursor(Object pageCursor) {
        currentPageCursor = pageCursor;
    }
    
    public Object getCurrentPageCursor() {
        return currentPageCursor;
    }
    
    private Object getFirstPageCursor() {
        return delegate.getFirstPageCursor(key, arguments);
    }
    
    /**
     * Retrieves the next page cursor.
     * 
     * Returns the first page cursor, if the current page cursor is null.
     * 
     * Returns null value, if there no page of results to go.
     * 
     * @return Object
     */
    private Object getNextPageCursor() {
        checkForNullDelegate();
        checkForNoPageCursor(getCurrentPageCursor());
        return delegate.getNextPageCursor(key, arguments, getCurrentPageCursor());
    }
    
    private Object getPrevPageCursor() {
        checkForNullDelegate();
        checkForNoPageCursor(getCurrentPageCursor());
        return delegate.getPrevPageCursor(key, arguments, getCurrentPageCursor());
    }
    
    private void checkForNullDelegate() {
        if (null == delegate) {
            throw new NullPointerException("Need assign a delegate, "
                    + "" + PaginatedResultDelegate.class.getName() + " to this result first.");
        }
    }
    
    private void checkForNoPageCursor(Object pageCursor) {
        if (NONE_PAGE_CURSOR == pageCursor) {
            throw new NullPointerException("Page cursor");
        }
    }
        
}