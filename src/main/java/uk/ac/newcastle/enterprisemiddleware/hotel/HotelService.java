package uk.ac.newcastle.enterprisemiddleware.hotel;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.logging.Logger;

@Dependent
public class HotelService {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    HotelRepository crud;

    @Inject
    HotelValidator validator;


    /**
     * Retrieves a list of all hotels from the database.
     *
     * @return a list of all {@link Hotel} objects.
     */
    public List<Hotel> findAll() {
        return crud.findAll();
    }


    /**
     * Creates a new hotel and stores it in the database after validation.
     *
     * @param hotel the {@link Hotel} object to be created.
     * @return the created {@link Hotel} object.
     * @throws Exception if validation fails or if an error occurs during creation.
     */
    public Hotel create(Hotel hotel) throws Exception {
        log.info("HotelService.create() - Creating " + hotel.getHotelName());

        // Check to make sure the data fits with the parameters in the Contact model and passes validation.
        validator.validateHotel(hotel);

        // Write the hotel to the database.
        return crud.create(hotel);
    }

    /**
     * Finds a hotel in the database by its ID.
     *
     * @param id the ID of the {@link Hotel} to be found.
     * @return the {@link Hotel} object with the specified ID, or null if not found.
     * @throws Exception if an error occurs during the search.
     */
    Hotel findById(String id) throws Exception {
        log.info("HotelService.findById() - Finding " + id);
        return crud.findById(Long.valueOf(id));
    }

    /**
     * Deletes an existing hotel from the database.
     *
     * @param hotel the {@link Hotel} object to be deleted.
     */
    public void deleteBooking(Hotel hotel) {
        log.info("HotelService.deleteBooking() - Deleting " + hotel.getHotelName());
        crud.delete(hotel);
    }
}
