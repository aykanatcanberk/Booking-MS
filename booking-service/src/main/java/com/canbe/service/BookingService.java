package com.canbe.service;

import com.canbe.dto.BookingRequest;
import com.canbe.dto.SalonDto;
import com.canbe.dto.ServiceDto;
import com.canbe.dto.UserDto;
import com.canbe.modal.Booking;
import com.canbe.modal.BookingStatus;
import com.canbe.modal.SalonReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookingService {

    Booking createBooking(BookingRequest booking , UserDto user, SalonDto salon , Set<ServiceDto> serviceDtoSet) throws Exception;
    List<Booking> getBookingsByCustomer(Long customerId);
    List<Booking> getBookingsBySalon(Long salonId);
    Booking getBookingById(Long id);
    Booking updateBooking(Long bookingId, BookingStatus status);
    List<Booking> getBookingsByDate(LocalDate date , Long salonId);
    SalonReport getSalonReport(Long salonId);
}
