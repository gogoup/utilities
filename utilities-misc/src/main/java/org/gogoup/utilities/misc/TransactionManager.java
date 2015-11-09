package org.gogoup.utilities.misc;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruisun on 2015-11-08.
 */
public class TransactionManager implements TransactionalService {

    private Map<String, TransactionalService> services;

    public TransactionManager() {
        this.services = new HashMap<>();
    }

    public void register(TransactionalService service) {
        services.put(service.getName(), service);
    }

    public TransactionalService deregister(String name) {
        return services.remove(name);
    }

    @Override
    public String getName() {
        return "TransactionManager";
    }

    @Override
    public void startTransaction() {
        for (TransactionalService service: services.values()) {
            service.startTransaction();
        }
    }

    @Override
    public void commit() {
        for (TransactionalService service: services.values()) {
            service.commit();
        }
    }

    @Override
    public void rollback() {
        for (TransactionalService service: services.values()) {
            service.rollback();
        }
    }
}
