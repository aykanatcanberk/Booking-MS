package com.canbe.service;

import com.canbe.dto.SalonDto;
import com.canbe.dto.UserDto;
import com.canbe.exception.SalonNotFoundException;
import com.canbe.modal.Salon;
import com.canbe.repository.SalonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl  implements SalonService {

    private final SalonRepository salonRepository;

    @Override
    public Salon createSalon(SalonDto request, UserDto user) {

        Salon salon = new Salon();

        salon.setName(request.getName());
        salon.setAddress(request.getAddress());
        salon.setEmail(request.getEmail());
        salon.setCity(request.getCity());
        salon.setImages(request.getImages());
        salon.setOwnerId(user.getId());
        salon.setOpenTime(request.getOpenTime());
        salon.setCloseTime(request.getCloseTime());
        salon.setPhoneNumber(request.getPhoneNumber());

        return salonRepository.save(salon);
    }

    @Override
    public Salon updateSalon(SalonDto request, UserDto user, Long salonId) throws Exception {

        Salon salonToUpdate = salonRepository.findById(salonId)
                .orElseThrow(() -> new Exception("Salon not exist"));

        if(salonToUpdate != null && request.getOwnerId().equals(request.getId())) {
            salonToUpdate.setName(request.getName());
            salonToUpdate.setAddress(request.getAddress());
            salonToUpdate.setEmail(request.getEmail());
            salonToUpdate.setCity(request.getCity());
            salonToUpdate.setImages(request.getImages());
            salonToUpdate.setOwnerId(request.getOwnerId());
            salonToUpdate.setOpenTime(request.getOpenTime());
            salonToUpdate.setCloseTime(request.getCloseTime());
            salonToUpdate.setPhoneNumber(request.getPhoneNumber());
        }

        return salonRepository.save(salonToUpdate);
    }


    @Override
    public List<Salon> getAllSalons() {
        return salonRepository.findAll();
    }

    @Override
    public Salon getSalonById(Long salonId) {
        return salonRepository.findById(salonId)
                .orElseThrow(() -> new SalonNotFoundException("Salon not found with id: " + salonId));
    }

    @Override
    public Salon getSalonByOwnerId(Long ownerId) {
        return salonRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Salon> searchSalonByCity(String city) {
        return salonRepository.searchSalons(city);
    }
}
