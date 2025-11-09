package com.crm.project.crm_backend.config;

import com.crm.project.crm_backend.multitenancy.TenantContext;
import com.crm.project.crm_backend.multitenancy.TenantFilter;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PreLoadEvent;
import org.hibernate.event.spi.PreLoadEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import java.util.UUID;
/**
 * MultitenancyConfiguration: Ensures the Hibernate Filter is always applied (NFR-001).
 *
 * This configuration class hooks into the Hibernate lifecycle to enable the tenant filter
 * on every database session, guaranteeing data isolation.
 */
@Configuration
public class MultitenancyConfiguration {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /**
     * PostConstruct: Runs once the EntityManagerFactory has been created.
     * This method registers a listener that enables the tenant filter before data is loaded.
     */
    @PostConstruct
    public void setFilter() {
        // Unwrap the EntityManagerFactory to get the native Hibernate SessionFactory
        SessionFactoryImplementor sessionFactory = entityManagerFactory
                .unwrap(SessionFactoryImplementor.class);
        
        // Get the registry for event listeners
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        
        // --- FIX APPLIED HERE: Use the generic get() method with the EventType ---
        // Register our custom listener for PreLoad events
        registry.getEventListenerGroup(EventType.PRE_LOAD)
                .appendListener(new TenantPreLoadListener());
    }

    /**
     * Inner class implementing the PreLoadEventListener to handle the filter enabling logic.
     */
    private static class TenantPreLoadListener implements PreLoadEventListener {
        
        @Override
        public void onPreLoad(PreLoadEvent event) {
            enableTenantFilter(event.getSession());
        }

        private void enableTenantFilter(Session session) {
            // Check if the filter is already enabled
            if (session.getEnabledFilter(TenantFilter.TENANT_FILTER_NAME) == null) {
                UUID tenantId = TenantContext.getTenantId();

                if (tenantId != null) {
                    // Enable the filter and set the parameter value from the ThreadLocal context
                    session.enableFilter(TenantFilter.TENANT_FILTER_NAME)
                           .setParameter(TenantFilter.TENANT_PARAMETER_NAME, tenantId);
                    // System.out.println("Hibernate Filter Enabled for Tenant: " + tenantId); // Useful for debugging
                }
            }
        }
    }
}
