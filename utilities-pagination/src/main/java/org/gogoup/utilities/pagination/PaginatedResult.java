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
    
    public static final PageOffset NONE_PAGE_OFFSET = null;

    private PaginatedResultDelegate<T> delegate;
    private String key;
    private Object[] arguments;
    private PageOffset currentPageOffset;
    private T result;

    public PaginatedResult(PaginatedResultDelegate<T> delegate, String key, Object... arguments) {
        this.key = key;
        this.arguments = arguments;
        this.delegate = delegate;
        this.currentPageOffset = NONE_PAGE_OFFSET;
    }
    
    /**
     * Retrieve the result at the giving page cursor.
     * 
     * Returns the first page of result, if the giving page cursor is null.
     * 
     * @param pageCursor Object
     * @return T
     */
    public T getResult(PageOffset pageCursor) {
        setCurrentPageCursor(pageCursor);
        return getResult();
    }
    
    public T getResult() {
        if (NONE_PAGE_OFFSET == currentPageOffset) {
            setCurrentPageCursor(getFirstPageOffset());
        }
        checkForNoPageOffset(currentPageOffset);
        checkForNullDelegate();
        result = delegate.fetchResult(key, arguments, currentPageOffset); 
        return result;
    }
    
    public PaginatedResult<T> next() {
        setCurrentPageCursor(getNextPageOffset());
        return this;
    }
    
    public PaginatedResult<T> previous() {
        setCurrentPageCursor(getPrevPageOffset());
        return this;
    }
    
    public PaginatedResult<T> rewind() {
        setCurrentPageCursor(getFirstPageOffset());
        return this;
    }
    
    private void setCurrentPageCursor(PageOffset pageCursor) {
        currentPageOffset = pageCursor;
    }
    
    public PageOffset getPageOffset() {
        return currentPageOffset;
    }
    
    private PageOffset getFirstPageOffset() {
        return delegate.getFirstPageOffset(key, arguments);
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
    private PageOffset getNextPageOffset() {
        checkForNullDelegate();
        checkForNoPageOffset(getPageOffset());
        return delegate.getNextPageOffset(key, arguments, getPageOffset());
    }
    
    private PageOffset getPrevPageOffset() {
        checkForNullDelegate();
        checkForNoPageOffset(getPageOffset());
        return delegate.getPrevPageOffset(key, arguments, getPageOffset());
    }
    
    private void checkForNullDelegate() {
        if (null == delegate) {
            throw new NullPointerException("Need assign a delegate, "
                    + "" + PaginatedResultDelegate.class.getName() + " to this result first.");
        }
    }
    
    private void checkForNoPageOffset(Object pageCursor) {
        if (NONE_PAGE_OFFSET == pageCursor) {
            throw new NullPointerException("Page cursor");
        }
    }
        
}