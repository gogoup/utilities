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

public interface PaginatedResultDelegate<T> {
    
    /**
     * Returns a T type of result by specified tag and the giving arguments.
     * 
     * The returned result is located by the giving page cursor.
     * 
     * @param key String
     * @param arguments Object[]
     * @param currentPageCursor Object
     * @return T
     */
    public T fetchResult(String key, Object[] arguments, PageOffset currentPageCursor);
    
    /**
     * Returns next page cursor based on the giving current page cursor.
     * 
     * @param key String
     * @param arguments Object[]
     * @param currentPageOffset Object
     * @return Object - returns the first page cursor, if the giving current page cursor is null.
     */
    public PageOffset getNextPageOffset(String key, Object[] arguments, PageOffset currentPageOffset);
    
    public PageOffset getPrevPageOffset(String key, Object[] arguments, PageOffset currentPageOffset);
    
    public PageOffset getFirstPageOffset(String key, Object[] arguments);
    
}
