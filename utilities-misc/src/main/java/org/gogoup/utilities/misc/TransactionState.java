package org.gogoup.utilities.misc;

import java.util.Date;

/**
 * Created by ruisun on 2016-01-04.
 */
public interface TransactionState {

    public String getName();

    public void setProperty(String name, Object property);

    public Object getProperty(String name);

}
