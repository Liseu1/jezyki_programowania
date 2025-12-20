package pl.edu.pwr.services;

import pl.edu.pwr.dao.Dao;
import pl.edu.pwr.dao.OrderDao;
import pl.edu.pwr.dao.ReservationDao;
import pl.edu.pwr.exceptions.DataAccessException;
import pl.edu.pwr.exceptions.DatabaseConnectionException;
import pl.edu.pwr.exceptions.RecordNotFoundException;
import pl.edu.pwr.exceptions.ReservationException;
import pl.edu.pwr.model.Offer;
import pl.edu.pwr.model.Order;
import pl.edu.pwr.model.OrderStatus;
import pl.edu.pwr.model.Reservation;
import pl.edu.pwr.model.ReservationStatus;

import java.util.List;

public class EmployeeService {

    private final OrderDao orderDao;
    private final ReservationDao reservationDao;
    private final Dao<Offer> offerDao;

    public EmployeeService(OrderDao orderDao, ReservationDao reservationDao, Dao<Offer> offerDao) {
        this.orderDao = orderDao;
        this.reservationDao = reservationDao;
        this.offerDao = offerDao;
    }

    public List<Order> getMyAssignedOrders(int employeeId) throws DataAccessException, DatabaseConnectionException {
        return orderDao.findByEmployeeId(employeeId);
    }

    public List<Reservation> getReservationsForOffer(int offerId) throws DataAccessException, DatabaseConnectionException {
        return reservationDao.findByOfferId(offerId);
    }

    public Offer getOfferDetails(int offerId) throws DataAccessException, DatabaseConnectionException {
        return offerDao.getOne(offerId);
    }

    public void startOrderRealization(int orderId) throws DataAccessException, DatabaseConnectionException, ReservationException {
        Order order;
        try {
            order = orderDao.getOne(orderId);
        } catch (RecordNotFoundException e) {
            throw new ReservationException("Zlecenie o podanym ID nie istnieje.");
        }

        order.setStatus(OrderStatus.REALIZOWANE);
        orderDao.update(order);

        List<Reservation> reservations = reservationDao.findByOfferId(order.getOfferId());
        for (Reservation res : reservations) {
            if (res.getStatus() == ReservationStatus.POTWIERDZONA) {
                res.setStatus(ReservationStatus.REALIZOWANA);
                reservationDao.update(res);
            }
        }
    }

    public void completeOrderRealization(int orderId) throws DataAccessException, DatabaseConnectionException, ReservationException {
        Order order;
        try {
            order = orderDao.getOne(orderId);
        } catch (RecordNotFoundException e) {
            throw new ReservationException("Zlecenie o podanym ID nie istnieje.");
        }

        order.setStatus(OrderStatus.ZAMKNIETE);
        orderDao.update(order);

        List<Reservation> reservations = reservationDao.findByOfferId(order.getOfferId());
        for (Reservation res : reservations) {
            if (res.getStatus() == ReservationStatus.REALIZOWANA) {
                res.setStatus(ReservationStatus.ZREALIZOWANA);
                reservationDao.update(res);
            }
        }
    }
}