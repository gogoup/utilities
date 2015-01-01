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
    
    private int paginatedResultIndex;
    private Set<String> keys;
    
    public AbstractPaginatedResultDelegate(int paginatedResultIndex, String... keys) {
        this.paginatedResultIndex= paginatedResultIndex;
        this.keys = new HashSet<String>(Arrays.asList(keys));
    }

    @Override
    public Object getNextPageCursor(String key, Object[] arguments,
            Object currentPageCursor) {
        checkForUnsupportedKeys(key);
        PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[paginatedResultIndex];
        return osResult.next().getCurrentPageCursor();
    }

    @Override
    public Object getPrevPageCursor(String key, Object[] arguments,
            Object currentPageCursor) {
        checkForUnsupportedKeys(key);
        PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[paginatedResultIndex];
        return osResult.previous().getCurrentPageCursor();
    }

    @Override
    public Object getFirstPageCursor(String key, Object[] arguments) {
        checkForUnsupportedKeys(key);
        PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[paginatedResultIndex];
        return osResult.rewind().getCurrentPageCursor();
    }
    
    private void checkForUnsupportedKeys(String key) {
        if (!keys.contains(key)) {
            throw new UnsupportedOperationException(key);
        }
    }

}
