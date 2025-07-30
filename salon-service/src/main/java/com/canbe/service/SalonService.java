package com.canbe.service;

import com.canbe.dto.SalonDto;
import com.canbe.dto.UserDto;
import com.canbe.modal.Salon;

import java.util.List;

public interface SalonService {

    Salon createSalon(SalonDto salon , UserDto user);
    Salon updateSalon(SalonDto salon , UserDto user, Long salonId) throws Exception;
    List<Salon> getAllSalons();
    Salon getSalonById(Long salonId);
    Salon getSalonByOwnerId(Long ownerId);
    List<Salon> searchSalonByCity(String city);

}
