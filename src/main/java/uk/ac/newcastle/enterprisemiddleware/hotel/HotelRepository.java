package uk.ac.newcastle.enterprisemiddleware.hotel;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;


/**
 * Repository class for managing {@link Hotel} entities. Provides methods to perform
 * CRUD operations on the hotel data stored in the database.
 */
@RequestScoped
public class HotelRepository {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;

    /**
     * Retrieves a list of all hotels in the database.
     *
     * @return a list of all {@link Hotel} entities.
     */
    List<Hotel> findAll() {
        TypedQuery<Hotel> query = em.createNamedQuery(Hotel.FIND_ALL, Hotel.class);
        return query.getResultList();
    }

    /**
     * Finds a hotel by its phone number.
     *
     * @param phoneNumber the phone number to search for.
     * @return the {@link Hotel} entity with the specified phone number.
     */
    Hotel findAllByPhoneNumber(String phoneNumber) {
        TypedQuery<Hotel> query = em.createNamedQuery(Hotel.FIND_ALL_BY_PHONE_NUMBER, Hotel.class).setParameter("phoneNumber", phoneNumber);
        return query.getSingleResult();
    }

    /**
     * Persists a new hotel entity in the database.
     *
     * @param hotel the hotel entity to be created.
     * @return the created {@link Hotel} entity.
     * @throws Exception if an error occurs during persistence.
     */
    Hotel create(Hotel hotel) throws Exception {
        log.info("HotelRepository.create() - Creating " + hotel.getHotelName());

        // Write the hotel to the database.
        em.persist(hotel);

        return hotel;
    }

    /**
     * Finds a hotel by its unique ID.
     *
     * @param id the ID of the hotel to find.
     * @return the {@link Hotel} entity with the specified ID, or null if not found.
     */
    public Hotel findById(Long id) {
        return em.find(Hotel.class, id);
    }

    /**
     * Removes a hotel from the database.
     *
     * @param hotel the hotel entity to be deleted.
     */
    public void delete(Hotel hotel) {
        log.info("HotelRepository.delete() - Deleting " + hotel.getHotelName());
        em.remove(hotel);
    }
}
