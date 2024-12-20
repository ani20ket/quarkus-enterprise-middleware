package uk.ac.newcastle.enterprisemiddleware.travelagent;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

/**
 * Repository class for managing Travel Agent entities.
 * <p>
 * This class provides methods to create, retrieve, and manage `TravelAgent` entities in the database.
 * It allows interactions such as finding a `TravelAgent` by its ID or by a customer's ID.
 * </p>
 */
@RequestScoped
public class TravelAgentRepository {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;

    /**
     * Retrieves a TravelAgent entity by its ID.
     *
     * @param id the ID of the TravelAgent to be retrieved
     * @return the TravelAgent entity with the specified ID, or null if not found
     */
    public TravelAgent findById(Long id) {
        return em.find(TravelAgent.class, id);
    }

    /**
     * Creates a new TravelAgent entity and persists it to the database.
     *
     * @param travelAgent the TravelAgent entity to be created
     * @return the created TravelAgent entity
     * @throws Exception if there is an error while persisting the entity
     */
    TravelAgent create(TravelAgent travelAgent) throws Exception {
        log.info("TravelAgentRepository.create() - Creating ");

        // Write the customer to the database.
        em.persist(travelAgent);

        return travelAgent;
    }

    /**
     * Retrieves a list of TravelAgent entities associated with a specific customer ID.
     *
     * @param id the ID of the customer whose TravelAgent entities are to be retrieved
     * @return a list of TravelAgent entities associated with the specified customer ID
     */
    public List<TravelAgent> findByCustomerId(long id) {
        TypedQuery<TravelAgent> query = em.createNamedQuery(TravelAgent.FIND_ALL_BY_CUSTOMER_ID, TravelAgent.class).setParameter("customerId", id);
        return query.getResultList();
    }

    public void delete(TravelAgent travelAgent) {
        log.info("TravelAgentRepository.delete() - Deleting ");
        em.remove(travelAgent);
    }
}
