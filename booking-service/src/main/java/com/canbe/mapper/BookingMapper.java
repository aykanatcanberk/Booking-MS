package com.canbe.mapper;

import com.canbe.dto.BookingDto;
import com.canbe.modal.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public static BookingDto toDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setSalonId(booking.getSalonId());
        dto.setCustomerId(booking.getCustomerId());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());
        dto.setServiceIds(booking.getServiceIds());
        dto.setStatus(booking.getStatus());

        return dto;
    }

    public Booking toEntity(BookingDto dto) {
        if (dto == null) {
            return null;
        }

        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setSalonId(dto.getSalonId());
        booking.setCustomerId(dto.getCustomerId());
        booking.setStartTime(dto.getStartTime());
        booking.setEndTime(dto.getEndTime());
        booking.setServiceIds(dto.getServiceIds());
        booking.setStatus(dto.getStatus());

        return booking;
    }
}
