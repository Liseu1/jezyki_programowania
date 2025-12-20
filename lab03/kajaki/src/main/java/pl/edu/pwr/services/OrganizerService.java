package pl.edu.pwr.services;

import pl.edu.pwr.dao.Dao;
import pl.edu.pwr.dao.OfferDao;
import pl.edu.pwr.dao.ReservationDao;
import pl.edu.pwr.exceptions.*;
import pl.edu.pwr.model.*;
import pl.edu.pwr.util.TimeSimulator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OrganizerService {

    private final OfferDao offerDao;
    private final ReservationDao reservationDao;
    private final Dao<Order> orderDao;
    private final Dao<Employee> employeeDao;
    private final Dao<Organizer> organizerDao;

    public OrganizerService(OfferDao offerDao,
                            ReservationDao reservationDao,
                            Dao<Order> orderDao,
                            Dao<Employee> employeeDao,
                            Dao<Organizer> organizerDao) {
        this.offerDao = offerDao;
        this.reservationDao = reservationDao;
        this.orderDao = orderDao;
        this.employeeDao = employeeDao;
        this.organizerDao = organizerDao;
    }

    public void createOffer(LocalDateTime term, String location, int maxSlots, int organizerId)
            throws OfferCreationException, DataAccessException, DatabaseConnectionException {

        LocalDate today = TimeSimulator.getSimulatedDate();
        LocalDate offerDate = term.toLocalDate();

        if (offerDate.isBefore(today)) {
            throw new OfferCreationException("Nie można tworzyć ofert na daty, które już minęły.");
        }
        if (maxSlots <= 0) {
            throw new OfferCreationException("Liczba miejsc musi być dodatnia.");
        }

        Offer newOffer = new Offer(
                0,
                term,
                location,
                maxSlots,
                maxSlots,
                organizerId
        );

        offerDao.create(newOffer);
    }

    public List<Reservation> getReservationsForOffer(int offerId)
            throws DataAccessException, DatabaseConnectionException {

        return reservationDao.findByOfferId(offerId);
    }

    public void updateReservationStatus(int reservationId, ReservationStatus newStatus)
            throws ReservationException, DataAccessException, DatabaseConnectionException {

        try {
            Reservation reservation = reservationDao.getOne(reservationId);

            reservation.setStatus(newStatus);

            reservationDao.update(reservation);

        } catch (RecordNotFoundException e) {
            throw new ReservationException("Nie można zaktualizować statusu. Rezerwacja o ID " + reservationId + " nie istnieje.");
        }
    }

    public List<Employee> getAllEmployees() throws DataAccessException, DatabaseConnectionException {
        return employeeDao.getAll();
    }

    public List<Offer> getAllOffers() throws DataAccessException, DatabaseConnectionException {
        return offerDao.getAll();
    }

    public void createOrderAndConfirmReservations(int offerId, int employeeId, int organizerId)
            throws OrderCreationException, DataAccessException, DatabaseConnectionException {

        try {
            offerDao.getOne(offerId);
            employeeDao.getOne(employeeId);
            organizerDao.getOne(organizerId);
        } catch (RecordNotFoundException e) {
            throw new OrderCreationException("Nie można stworzyć zlecenia. Oferta, pracownik lub organizator nie istnieją.");
        }

        Order newOrder = new Order(
                0,
                organizerId,
                employeeId,
                offerId,
                OrderStatus.OTWARTE
        );
        orderDao.create(newOrder);

        List<Reservation> reservations = reservationDao.findByOfferId(offerId);
        for (Reservation res : reservations) {
            if (res.getStatus() == ReservationStatus.ZALOZONA) {
                res.setStatus(ReservationStatus.POTWIERDZONA);
                reservationDao.update(res);
            }
        }
    }

    private void validateDecisionDate(int offerId) throws ReservationException, DataAccessException, DatabaseConnectionException {
        Offer offer;
        try {
            offer = offerDao.getOne(offerId);
        } catch (RecordNotFoundException e) {
            throw new ReservationException("Oferta o podanym ID nie istnieje.");
        }

        LocalDate today = TimeSimulator.getSimulatedDate();
        LocalDate offerDate = offer.getTerm().toLocalDate();
        LocalDate lastDecisionDay = offerDate.minusDays(1);

        if (today.isAfter(lastDecisionDay)) {
            throw new ReservationException("Jest za późno na zmianę statusów. Decyzję można podjąć najpóźniej na dzień przed spływem.");
        }
    }

    public void confirmAllReservationsForOffer(int offerId)
            throws ReservationException, DataAccessException, DatabaseConnectionException {

        validateDecisionDate(offerId);

        List<Reservation> reservations = reservationDao.findByOfferId(offerId);
        int count = 0;
        for (Reservation res : reservations) {
            if (res.getStatus() == ReservationStatus.ZALOZONA) {
                res.setStatus(ReservationStatus.POTWIERDZONA);
                reservationDao.update(res);
                count++;
            }
        }
        if (count == 0) {
            throw new ReservationException("Brak rezerwacji o statusie ZALOZONA do potwierdzenia dla tej oferty.");
        }
    }

    public void cancelAllReservationsForOffer(int offerId)
            throws ReservationException, DataAccessException, DatabaseConnectionException {

        validateDecisionDate(offerId);

        List<Reservation> reservations = reservationDao.findByOfferId(offerId);
        int count = 0;
        for (Reservation res : reservations) {
            if (res.getStatus() == ReservationStatus.ZALOZONA || res.getStatus() == ReservationStatus.POTWIERDZONA) {
                res.setStatus(ReservationStatus.ODWOLANA);
                reservationDao.update(res);
                count++;
            }
        }
        if (count == 0) {
            throw new ReservationException("Brak aktywnych rezerwacji (ZALOZONA lub POTWIERDZONA) do odwołania dla tej oferty.");
        }
    }
    public void checkAndCancelPendingReservations()
            throws DataAccessException, DatabaseConnectionException {

        LocalDate today = TimeSimulator.getSimulatedDate();

        List<Offer> offersDueToday = offerDao.findByDate(today);

        int cancelledCount = 0;

        for (Offer offer : offersDueToday) {
            List<Reservation> reservations = reservationDao.findByOfferId(offer.getId());
            for (Reservation res : reservations) {
                if (res.getStatus() == ReservationStatus.ZALOZONA) {
                    res.setStatus(ReservationStatus.ODWOLANA);
                    reservationDao.update(res);
                    cancelledCount++;
                }
            }
        }

        if (cancelledCount > 0) {
            System.out.println("Serwis: Automatycznie anulowano " + cancelledCount + " rezerwacji (brak decyzji organizatora).");
        }
    }
}