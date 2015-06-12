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
        this.keys = new HashSet<String>(Arrays.asList(keys));
    }

    @Override
    public T fetchResult(ResultFetchRequest request) {
        checkForUnsupportedKeys(request.getKey());
        PaginatedResult<S> result = (PaginatedResult<S>) request.getArguments()[RESULT_INDEX];
        result = result.start(request.getPageOffset());
        return toResult(request.getKey(), result.getResult(), request.getArguments());
    }

    abstract protected T toResult(String key, S values, Object[] arguments);
    
    private void checkForUnsupportedKeys(String key) {
        if (!keys.contains(key)) {
            throw new UnsupportedOperationException(key);
        }
    }
}
