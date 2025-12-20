package pl.edu.pwr.services;

import pl.edu.pwr.dao.Dao;
import pl.edu.pwr.dao.ReservationDao;
import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;
import pl.edu.pwr.exceptions.RecordNotFoundException;
import pl.edu.pwr.exceptions.ReservationException;
import pl.edu.pwr.model.Client;
import pl.edu.pwr.model.Offer;
import pl.edu.pwr.model.Reservation;
import pl.edu.pwr.model.ReservationStatus;
import pl.edu.pwr.util.TimeSimulator;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ClientService {
    private final Dao<Offer> offerDao;
    private final ReservationDao reservationDao;
    private final Dao<Client> clientDao;
    private final OrganizerService organizerService;

    public ClientService(Dao<Offer> offerDao, ReservationDao reservationDao, Dao<Client> clientDao, OrganizerService organizerService) {
        this.offerDao = offerDao;
        this.reservationDao = reservationDao;
        this.clientDao = clientDao;
        this.organizerService = organizerService;
    }

    public List<Offer> getAvailableOffers() throws DataAccessException, DatabaseConnectionException {
        List<Offer> allOffers = offerDao.getAll();
        LocalDate today = TimeSimulator.getSimulatedDate();

        List<Offer> availableOffers = allOffers.stream()
                .filter(offer -> {
                    LocalDate offerDate = offer.getTerm().toLocalDate();
                    return !offerDate.isBefore(today);
                })
                .collect(Collectors.toList());

        return availableOffers;
    }

    public void makeReservation(int clientId, int offerId, int slotsToBook)
            throws ReservationException, DataAccessException, DatabaseConnectionException {

        Offer offer;
        try {
            offer = offerDao.getOne(offerId);
            clientDao.getOne(clientId);
        } catch (RecordNotFoundException e) {
            throw new ReservationException("Nie można stworzyć rezerwacji. Wybrana oferta lub klient nie istnieją.");
        }

        LocalDate today = TimeSimulator.getSimulatedDate();
        LocalDate offerDate = offer.getTerm().toLocalDate();

        if (today.isAfter(offerDate.minusDays(1))) {
            throw new ReservationException("Za późno na rezerwację. Rezerwacje przyjmowane są najpóźniej na jeden dzień przed terminem spływu.");
        }
        if (slotsToBook <= 0) {
            throw new ReservationException("Liczba rezerwowanych miejsc musi być większa od zera.");
        }
        if (slotsToBook > offer.getVacantSlots()) {
            throw new ReservationException("Niewystarczająca liczba wolnych miejsc. Dostępne: " + offer.getVacantSlots());
        }

        int newVacantSlots = offer.getVacantSlots() - slotsToBook;
        offer.setVacantSlots(newVacantSlots);
        offerDao.update(offer);

        int organizerId = offer.getOrganizerId();
        ReservationStatus initialStatus = ReservationStatus.ZALOZONA;

        Reservation newReservation = new Reservation(
                0,
                organizerId,
                offerId,
                clientId,
                slotsToBook,
                initialStatus
        );

        reservationDao.create(newReservation);
    }

    public List<Reservation> getMyReservations(int clientId)
            throws DataAccessException, DatabaseConnectionException {

        return reservationDao.findByClientId(clientId);
    }

    public void checkAndCancelPendingReservations()
            throws DataAccessException, DatabaseConnectionException {
        organizerService.checkAndCancelPendingReservations();
    }
}