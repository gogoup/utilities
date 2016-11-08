package org.gogoup.utilities.misc;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruisun on 2015-11-08.
 */
public class DefaultTransactionManager implements TransactionManager {

    private Map<String, TransactionalService> services;
    private Map<String, TransactionStateImpl> states;
    private TransactionContext context;

    public DefaultTransactionManager() {
        this.services = new LinkedHashMap<>();
        this.states = new LinkedHashMap<>();
        this.context = null;
    }

    @Override
    public void register(TransactionalService service) {
        services.put(service.getName(), service);
    }

    @Override
    public TransactionalService deregister(String name) {
        return services.remove(name);
    }

    @Override
    public void startTransaction() throws TransactionalServiceException {
        context = new TransactionContextImpl();
        for (TransactionalService service: services.values()) {
            TransactionStateImpl state =
                    new TransactionStateImpl(service.getName());
            states.put(service.getName(), state);
            service.startTransaction(context, state);
        }
    }

    @Override
    public void commit() throws TransactionalServiceException {
        for (TransactionalService service: services.values()) {
            TransactionStateImpl state = states.remove(service.getName());
            service.commit(context, state);
        }
        this.context = null;
    }

    @Override
    public void rollback() throws TransactionalServiceException {
        for (TransactionalService service: services.values()) {
            TransactionStateImpl state = states.remove(service.getName());
            service.rollback(context, state);
        }
        this.context = null;
    }

    private void onError(List<TransactionalService> services, Exception exception) {
        for (TransactionalService service: services) {
            TransactionState state = states.get(service.getName());
            service.onError(context, state, exception);
        }
        this.context = null;
    }

    private static class TransactionStateImpl implements TransactionState {

        private String name;
        private Map<String, Object> properties;

        public TransactionStateImpl(String name) {
            this.name = name;
            this.properties = new HashMap<>();
        }

        @Override
        public String getServiceName() {
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

        @Override
        public Object removeProperty(String name) {
            return properties.remove(name);
        }
    }

    private static class TransactionContextImpl implements TransactionContext {

        private Map<String, Object> properties;

        public TransactionContextImpl() {
            this.properties = new HashMap<>();
        }

        @Override
        public void setProperty(String name, Object property) {
            properties.put(name, property);
        }

        @Override
        public Object getProperty(String name) {
            return properties.get(name);
        }

        @Override
        public Object removeProperty(String name) {
            return properties.remove(name);
        }
    }
}
