package com.canbe.controller;

import com.canbe.dto.*;
import com.canbe.mapper.BookingMapper;
import com.canbe.modal.Booking;
import com.canbe.modal.BookingStatus;
import com.canbe.modal.SalonReport;
import com.canbe.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping()
    public ResponseEntity<Booking> createBooking(@RequestParam Long salonId,
                                                 @RequestBody BookingRequest bookingRequest) throws Exception {

        UserDto user = new UserDto();
        user.setId(1L);

        SalonDto salon = new SalonDto();
        salon.setId(salonId);
        salon.setOpenTime(LocalTime.now());
        salon.setCloseTime(LocalTime.now().plusHours(12));

        Set<ServiceDto> serviceDtoSet = new HashSet<>();

        ServiceDto serviceDto = new ServiceDto();
        serviceDto.setId(1L);
        serviceDto.setPrice(200);
        serviceDto.setDuration(30);
        serviceDto.setName("Hair cut");

        serviceDtoSet.add(serviceDto);

        Booking booking = bookingService.createBooking(bookingRequest, user, salon, serviceDtoSet);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDto>> getBookingsByCustomer() {

        List<Booking> bookings = bookingService.getBookingsByCustomer(1L);

        return ResponseEntity.ok(getBookingDtos(bookings));
    }

    @GetMapping("/salon")
    public ResponseEntity<Set<BookingDto>> getBookingsBySalon() {

        List<Booking> bookings = bookingService.getBookingsBySalon(1L);

        return ResponseEntity.ok(getBookingDtos(bookings));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBookingsById(@PathVariable Long bookingId) {

        Booking booking = bookingService.getBookingById(bookingId);

        return ResponseEntity.ok(BookingMapper.toDto(booking));
    }

    @PutMapping("/{bookingId}/status")
    public ResponseEntity<BookingDto> updateBookingStatus(@PathVariable Long bookingId,
                                                          @RequestParam BookingStatus status) {

        Booking booking = bookingService.updateBooking(bookingId, status);
        return ResponseEntity.ok(BookingMapper.toDto(booking));
    }

    @GetMapping("/slots/salon/{salonId}/date/{date}")
    public ResponseEntity<List<BookingSlotDto>> getBookedSlot(@PathVariable Long salonId,
                                                    @RequestParam(required = false) LocalDate date ) {

        List<Booking> bookings = bookingService.getBookingsByDate(date,salonId);

        List<BookingSlotDto> bookingSlotDtos = bookings.stream()
                .map(booking -> {
                    BookingSlotDto bookingSlotDto = new BookingSlotDto();
                    bookingSlotDto.setStartTime(booking.getStartTime());
                    bookingSlotDto.setEndTime(booking.getEndTime());
                    return bookingSlotDto;
                }).collect(Collectors.toList());

        return ResponseEntity.ok(bookingSlotDtos);
    }

    @GetMapping("/report")
    public ResponseEntity<SalonReport> getSalonReport() {

        SalonReport report = bookingService.getSalonReport(1L);

        return ResponseEntity.ok(report);
    }

    private Set<BookingDto> getBookingDtos(List<Booking> bookings) {
        return bookings.stream().map(booking -> {
            return BookingMapper.toDto(booking);
        }).collect(Collectors.toSet());
    }
}
