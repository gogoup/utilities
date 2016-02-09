package org.gogoup.utilities.misc;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruisun on 2015-11-08.
 */
public class TransactionManager {

    private Map<String, TransactionalService> services;
    private Map<String, TransactionState> states;

    public TransactionManager() {
        this.services = new HashMap<>();
    }

    public void register(TransactionalService service) {
        services.put(service.getName(), service);
    }

    public TransactionalService deregister(String name) {
        return services.remove(name);
    }

    public void startTransaction() {
        for (TransactionalService service: services.values()) {
            TransactionStateImpl state = new TransactionStateImpl(service.getName());
            states.put(service.getName(), state);
            service.startTransaction(state);
        }
    }

    public void commit() {
        for (TransactionalService service: services.values()) {
            TransactionState state = states.remove(service.getName());
            service.commit(state);
        }
    }

    public void rollback() {
        for (TransactionalService service: services.values()) {
            TransactionState state = states.remove(service.getName());
            service.rollback(state);
        }
    }

    private static class TransactionStateImpl implements TransactionState {

        private String name;
        private Map<String, Object> properties;

        public TransactionStateImpl(String name) {
            this.name = name;
            this.properties = new HashMap<>();
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setProperty(String name, Object property) {
            properties.put(name, property);
        }

        @Override
        public Object getProperty(String name) {
            return properties.get(name);
        }
    }
}
