package uk.ac.newcastle.enterprisemiddleware.hotelbooking;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;


/**
 * Repository class for managing {@link HotelBooking} entities.
 * This class provides methods for creating, finding, and deleting hotel bookings in the database.
 *
 * <p>The repository utilizes an {@link EntityManager} for database operations
 * and is set to {@code RequestScoped} for request-level transaction management.</p>
 *
 *
 * @see HotelBooking
 */
@RequestScoped
public class HotelBookingRepository {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;

    /**
     * Finds all hotel bookings associated with a specific customer ID.
     *
     * @param customerId the ID of the customer whose bookings are to be retrieved
     * @return a list of {@link HotelBooking} instances for the specified customer ID
     */
    List<HotelBooking> findAllByCustomerId(long customerId) {
        TypedQuery<HotelBooking> query = em.createNamedQuery(HotelBooking.FIND_ALL_BY_CUSTOMER_ID, HotelBooking.class).setParameter("customerId", customerId);
        return query.getResultList();
    }

    /**
     * Creates a new hotel booking and persists it in the database.
     *
     * @param hotelBooking the {@link HotelBooking} instance to be created
     * @return the persisted {@link HotelBooking} entity
     * @throws Exception if an error occurs while persisting the entity
     */
    HotelBooking create(HotelBooking hotelBooking) throws Exception {
        log.info("HotelRepository.create() - Creating " + hotelBooking.getHotelId());

        // Write the hotelBooking to the database.
        em.persist(hotelBooking);

        return hotelBooking;
    }

    /**
     * Finds a hotel booking by its ID.
     *
     * @param id the unique identifier of the {@link HotelBooking} to find
     * @return the {@link HotelBooking} entity with the specified ID, or {@code null} if not found
     */
    public HotelBooking findById(Long id) {
        return em.find(HotelBooking.class, id);
    }

    /**
     * Deletes a specified hotel booking from the database.
     *
     * @param hotelBooking the {@link HotelBooking} instance to be deleted
     */
    public void deleteBooking(HotelBooking hotelBooking) {
        log.info("HotelRepository.deleteBooking() - Deleting " + hotelBooking.getHotelId());
        em.remove(hotelBooking);
    }

    public HotelBooking findByHotelIdAndDate(String hotelId, LocalDate checkInDate) {
        TypedQuery<HotelBooking> query = em.createNamedQuery(HotelBooking.FIND_BY_HOTEL_ID_AND_DATE, HotelBooking.class);
        query.setParameter("hotelId", hotelId);
        query.setParameter("date", checkInDate);

        try {
            return query.getSingleResult();  // Returns a single result
        } catch (NoResultException e) {
            // Handle the case where no result is found
            return null;
        }
    }
}
