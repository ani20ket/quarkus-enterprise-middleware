package uk.ac.newcastle.enterprisemiddleware.customer;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Logger;

/**
 * <p>This is a Repository class and connects the Service/Control layer (see {@link CustomerService} with the
 * Domain/Entity Object (see {@link Customer}).<p/>
 *
 * <p>There are no access modifiers on the methods making them 'package' scope.  They should only be accessed by a
 * Service/Control object.<p/>
 *
 * @see Customer
 * @see EntityManager
 */
@RequestScoped
public class CustomerRepository {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;

    /**
     * Retrieves all customers from the database ordered by name.
     *
     * @return a list of all customers ordered by name
     */
    List<Customer> findAllOrderedByName() {
        TypedQuery<Customer> query = em.createNamedQuery(Customer.FIND_ALL, Customer.class);
        return query.getResultList();
    }

    /**
     * Finds a customer by their unique ID.
     *
     * @param id the ID of the customer to find
     * @return the customer with the specified ID, or null if not found
     */
    public Customer findById(Long id) {
        return em.find(Customer.class, id);
    }

    /**
     * Finds a customer by their email address.
     *
     * @param email the email address of the customer to find
     * @return the customer with the specified email, or null if not found
     */
    Customer findByEmail(String email) {
        TypedQuery<Customer> query = em.createNamedQuery(Customer.FIND_BY_EMAIL, Customer.class).setParameter("email", email);
        return query.getSingleResult();
    }

    /**
     * Finds all customers with the specified name.
     *
     * @param name the name of the customers to find
     * @return a list of customers with the specified name
     */
    List<Customer> findAllByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = cb.createQuery(Customer.class);
        Root<Customer> customer = criteria.from(Customer.class);
        criteria.select(customer).where(cb.equal(customer.get("name"), name));
        return em.createQuery(criteria).getResultList();
    }

    /**
     * Creates a new customer in the database.
     * Logs the creation process and returns the created customer.
     *
     * @param customer the customer to be created
     * @return the created customer object
     * @throws Exception if an error occurs during creation
     */
    Customer create(Customer customer) throws Exception {
        log.info("CustomerRepository.create() - Creating " + customer.getName());

        // Write the customer to the database.
        em.persist(customer);

        return customer;
    }

    /**
     * Deletes the specified customer from the database if an ID is present.
     * Logs the deletion process and returns the customer object after attempting deletion.
     *
     * @param customer the customer to be deleted
     * @return the customer object after the deletion attempt
     * @throws Exception if an error occurs during deletion
     */
    Customer delete(Customer customer) throws Exception {
        log.info("ContactRepository.delete() - Deleting " + customer.getName());

        if (customer.getId() != null) {
            em.remove(em.merge(customer));

        } else {
            log.info("ContactRepository.delete() - No ID was found so can't Delete.");
        }

        return customer;
    }

}
