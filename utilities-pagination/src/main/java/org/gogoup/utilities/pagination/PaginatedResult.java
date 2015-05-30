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

    private PaginatedResultDelegate<T> delegate;
    private String key;
    private Object[] arguments;
    private PageOffset currentPageOffset;
    private T result;

    public PaginatedResult(PaginatedResultDelegate<T> delegate, String key, Object... arguments) {
        this.key = key;
        this.arguments = arguments;
        this.delegate = delegate;
        this.currentPageOffset = null;
    }
    
    /**
     * Retrieve the result at the giving page cursor.
     * 
     * Returns the first page of result, if the giving page cursor is null.
     * 
     * @param pageOffset Object
     * @return T
     */
    public T getResult(PageOffset pageOffset) {
        setCurrentPageOffset(pageOffset);
        return getResult();
    }

    /**
     * Retrieve results at the first page with the given size.
     *
     * @param size int
     * @return T
     */
    public T getResult(int size) {
        return getResult(new PageOffset(1, size));
    }
    
    public T getResult() {
        if (null == currentPageOffset) {
            setCurrentPageOffset(getFirstPageOffset());
        }
        checkForNoPageOffset(currentPageOffset);
        checkForNullDelegate();
        result = delegate.fetchResult(key, arguments, currentPageOffset); 
        return result;
    }
    
    public PaginatedResult<T> next() {
        setCurrentPageOffset(getNextPageOffset());
        return this;
    }
    
    public PaginatedResult<T> previous() {
        setCurrentPageOffset(getPrevPageOffset());
        return this;
    }
    
    public PaginatedResult<T> rewind() {
        setCurrentPageOffset(getFirstPageOffset());
        return this;
    }
    
    private void setCurrentPageOffset(PageOffset pageCursor) {
        currentPageOffset = pageCursor;
    }
    
    public PageOffset getCurrentPageOffset() {
        return currentPageOffset;
    }
    
    private PageOffset getFirstPageOffset() {
        checkForNoPageOffset(getCurrentPageOffset());
        return new PageOffset(1, getCurrentPageOffset().getSize());
    }

    private PageOffset getNextPageOffset() {
        checkForNoPageOffset(getCurrentPageOffset());
        return new PageOffset(getCurrentPageOffset().getStart() + getCurrentPageOffset().getSize(), getCurrentPageOffset().getSize());
    }
    
    private PageOffset getPrevPageOffset() {
        checkForNoPageOffset(getCurrentPageOffset());
        return new PageOffset(getCurrentPageOffset().getStart() - getCurrentPageOffset().getSize(), getCurrentPageOffset().getSize());
    }
    
    private void checkForNullDelegate() {
        if (null == delegate) {
            throw new NullPointerException("Need assign a delegate, "
                    + "" + PaginatedResultDelegate.class.getName() + " to this result first.");
        }
    }
    
    private void checkForNoPageOffset(Object pageCursor) {
        if (null == pageCursor) {
            throw new IllegalArgumentException("You need to specify page offset! Try to invoke .getResult(...)");
        }
    }
        
}