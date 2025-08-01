package com.canbe.service;

import com.canbe.dto.BookingRequest;
import com.canbe.dto.SalonDto;
import com.canbe.dto.ServiceDto;
import com.canbe.dto.UserDto;
import com.canbe.exception.BookingNotFoundException;
import com.canbe.exception.OutOfWorkingHoursException;
import com.canbe.exception.TimeSlotUnavailableException;
import com.canbe.modal.Booking;
import com.canbe.modal.BookingStatus;
import com.canbe.modal.SalonReport;
import com.canbe.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public Booking createBooking(BookingRequest booking, UserDto user,
                                 SalonDto salon, Set<ServiceDto> serviceDtoSet) throws Exception {


        int totalDuration = serviceDtoSet.stream()
                .mapToInt(ServiceDto::getDuration).sum();

        LocalDateTime startTime = booking.getStartTime();
        LocalDateTime endTime = startTime.plusMinutes(totalDuration);

        Boolean isSlotAvailable = isTimeSlotAvailable(salon, startTime, endTime);

        int totalPrice = serviceDtoSet.stream().mapToInt(ServiceDto::getPrice).sum();

        Set<Long> idList = serviceDtoSet.stream().map(ServiceDto::getId).collect(Collectors.toSet());

        Booking newBooking = new Booking();
        newBooking.setCustomerId(user.getId());
        newBooking.setSalonId(salon.getId());
        newBooking.setServiceIds(idList);
        newBooking.setStatus(BookingStatus.PENDING);
        newBooking.setStartTime(startTime);
        newBooking.setEndTime(endTime);
        newBooking.setTotalPrice(totalPrice);

        return bookingRepository.save(newBooking);
    }

    public boolean isTimeSlotAvailable(SalonDto salonDto, LocalDateTime startTime, LocalDateTime endTime) throws Exception {

        // 1. Determine salon working hours on the given date
        LocalDateTime salonOpen = salonDto.getOpenTime().atDate(startTime.toLocalDate());
        LocalDateTime salonClose = salonDto.getCloseTime().atDate(startTime.toLocalDate());

        // 2. Check if the requested time is within working hours
        if (startTime.isBefore(salonOpen) || endTime.isAfter(salonClose)) {
            throw new OutOfWorkingHoursException("Booking time must be within the salon's working hours.");
        }

        // 3. Retrieve existing bookings for the selected salon
        List<Booking> existingBookings = getBookingsBySalon(salonDto.getId());

        for (Booking booking : existingBookings) {
            LocalDateTime bookedStart = booking.getStartTime();
            LocalDateTime bookedEnd = booking.getEndTime();

            boolean overlaps = startTime.isBefore(bookedEnd) && endTime.isAfter(bookedStart);

            if (overlaps) {
                throw new TimeSlotUnavailableException("The selected time slot is not available due to an overlapping booking.");
            }
        }

        return true;
    }


    @Override
    public List<Booking> getBookingsByCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingsBySalon(Long salonId) {
        return bookingRepository.findBySalonId(salonId);
    }

    @Override
    public Booking getBookingById(Long id) {

        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));
    }


    @Override
    public Booking updateBooking(Long bookingId, BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + bookingId));

        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsByDate(LocalDate date, Long salonId) {

        List<Booking> allBookings = getBookingsBySalon(salonId);

        if (date == null) {
            return allBookings;
        }

        return allBookings.stream()
                .filter(booking -> isSameDate(booking.getStartTime(), date) ||
                        isSameDate(booking.getEndTime(), date))
                .collect(Collectors.toList());

    }

    private boolean isSameDate(LocalDateTime dateTime, LocalDate date) {
        return dateTime.toLocalDate().isEqual(date);
    }

    @Override
    public SalonReport getSalonReport(Long salonId) {

        List<Booking> bookings = getBookingsBySalon(salonId);

        Double totalEarnings = bookings.stream()
                .mapToDouble(Booking::getTotalPrice).sum();

        Integer totalBookings = bookings.size();
        List<Booking> cancelledBookings = bookings.stream()
                .filter(booking -> booking.getStatus().equals(BookingStatus.CANCELED))
                .collect(Collectors.toList());

        Double totalRefund = cancelledBookings.stream()
                .mapToDouble(Booking::getTotalPrice).sum();

        SalonReport salonReport = new SalonReport();
        salonReport.setSalonId(salonId);
        salonReport.setCancelledBookings(cancelledBookings.size());
        salonReport.setTotalBookings(totalBookings);
        salonReport.setTotalEarnings(totalEarnings);
        salonReport.setTotalRefund(totalRefund);


        return salonReport;
    }
}
