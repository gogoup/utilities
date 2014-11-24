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

public abstract class AutoPaginatedResultDelegate<T> implements PaginatedResultDelegate<T> {
    
    private int argumentIndex;
    private String[] tags;
    
    public AutoPaginatedResultDelegate(int argumentIndex, String ... tags) {
        this.argumentIndex= argumentIndex;
        this.tags = tags;
    }

    @Override
    public boolean isFetchAllResultsSupported(String tag, Object[] arguments) {
        checkForTags(tag);
        PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[argumentIndex];
        return osResult.isGetAllResultsSupported();
    }

    @Override
    public Object getNextPageCursor(String tag, Object[] arguments,
            Object pageCursor, T result) {
        checkForTags(tag);
        PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[argumentIndex];
        return osResult.getNextPageCursor();
    }

    @Override
    public Object getPrevPageCursor(String tag, Object[] arguments,
            Object pageCursor, T result) {
        checkForTags(tag);
        PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[argumentIndex];
        return osResult.getPrevPageCursor();
    }

    @Override
    public Object getFirstPageCursor(String tag, Object[] arguments) {
        checkForTags(tag);
        PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[argumentIndex];
        return osResult.getFirstPageCursor();
    }
    
    private void checkForTags(String tag) {
        for (String t: tags) {
            if (t.equals(tag)) {
                return;
            }
        }
        throw new UnsupportedOperationException(tag);
    }

}
