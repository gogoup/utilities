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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PaginatedResult<T> {

    private PaginatedResultDelegate<T> delegate;
    private String key;
    private Object[] arguments;
    private PageOffset currentPageOffset;
    private String pagingId;
    private Sorting sorting;

    public PaginatedResult(PaginatedResultDelegate<T> delegate, String key, Object... arguments) {
        this.key = key;
        this.arguments = arguments;
        this.delegate = delegate;
        this.currentPageOffset = null;
    }

    private void generateId() {
        StringBuilder strBuilder = new StringBuilder();
        for (Object arg: arguments) {
            if (null != arg) {
                strBuilder.append(arg.hashCode());
            }
        }
        strBuilder.append(key);
        strBuilder.append(getCurrentPageOffset().getStart());
        strBuilder.append(getCurrentPageOffset().getSize());
        if (null != getSorting()) {
            strBuilder.append(getSorting().getField());
            strBuilder.append(getSorting().getOrder().toString());
        }
        pagingId = toMD5(strBuilder.toString());
    }

    public String getPagingId() {
        if (null == pagingId) {
            generateId();
        }
        return pagingId;
    }
    
    /**
     * Initialize the current page offset to the given one.
     * 
     * @param pageOffset Object
     * @return PaginatedResult<T>
     */
    public PaginatedResult<T> start(PageOffset pageOffset) {
        return start(pageOffset, null);
    }

    public PaginatedResult<T> start(PageOffset pageOffset, Sorting sorting) {
        setCurrentPageOffset(pageOffset);
        this.sorting = sorting;
        return this;
    }

    /**
     * Initialize the current page offset to the first page with the given size.
     *
     * @param size int
     * @return PaginatedResult<T>
     */
    public PaginatedResult<T> start(int size) {
        return start(new PageOffset(1, size));
    }

    public PaginatedResult<T> start(int size, Sorting sorting) {
        return start(new PageOffset(1, size), sorting);
    }
    
    public T getResult() {
        checkForNoPageOffset(currentPageOffset);
        checkForNullDelegate();
        return delegate.fetchResult
                (new ResultFetchRequest(getPagingId(), key, arguments, currentPageOffset, sorting));
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

    public Sorting getSorting() {
        return sorting;
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

    private String toMD5(String message) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));
            StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i < hashedBytes.length; i++) {
                strBuilder.append(Integer.toString((hashedBytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            return strBuilder.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

}