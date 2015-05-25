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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractPaginatedResultDelegate<T> implements PaginatedResultDelegate<T> {

    public final static int RESULT_INDEX = 0;
    
    private int paginatedResultIndex;
    private Set<String> keys;

    protected AbstractPaginatedResultDelegate(String... keys) {
        this(RESULT_INDEX, keys);
    }
    
    protected AbstractPaginatedResultDelegate(int paginatedResultIndex, String... keys) {
        this.paginatedResultIndex= paginatedResultIndex;
        this.keys = new HashSet<String>(Arrays.asList(keys));
    }

    @Override
    public PageOffset getNextPageOffset(String key, Object[] arguments,
            PageOffset currentPageOffset) {
        checkForUnsupportedKeys(key);
        PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[paginatedResultIndex];
        return osResult.next().getPageOffset();
    }

    @Override
    public PageOffset getPrevPageOffset(String key, Object[] arguments,
            PageOffset currentPageOffset) {
        checkForUnsupportedKeys(key);
        PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[paginatedResultIndex];
        return osResult.previous().getPageOffset();
    }

    @Override
    public PageOffset getFirstPageOffset(String key, Object[] arguments) {
        checkForUnsupportedKeys(key);
        PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[paginatedResultIndex];
        return osResult.rewind().getPageOffset();
    }
    
    private void checkForUnsupportedKeys(String key) {
        if (!keys.contains(key)) {
            throw new UnsupportedOperationException(key);
        }
    }

}
