package com.canbe.controller;

import com.canbe.dto.SalonDto;
import com.canbe.dto.UserDto;
import com.canbe.mapper.SalonMapper;
import com.canbe.modal.Salon;
import com.canbe.service.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/salons")
@RequiredArgsConstructor
public class SalonController {

    private final SalonService salonService;
    private final SalonMapper salonMapper;

    @PostMapping
    public ResponseEntity<SalonDto> createSalon(@RequestBody SalonDto salonDto) {
        UserDto userDto = new UserDto();
        userDto.setId(1L); // Dummy user
        Salon createdSalon = salonService.createSalon(salonDto, userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salonMapper.toDto(createdSalon));
    }

    @GetMapping
    public ResponseEntity<List<SalonDto>> getAllSalons() {
        List<SalonDto> salons = salonService.getAllSalons()
                .stream()
                .map(salonMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(salons);
    }

    @GetMapping("/{salonId}")
    public ResponseEntity<SalonDto> getSalonById(@PathVariable("salonId") Long salonId) {
        Salon salon = salonService.getSalonById(salonId);
        return ResponseEntity.ok(salonMapper.toDto(salon));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<SalonDto> getSalonByOwnerId(@PathVariable("ownerId") Long ownerId) {

        UserDto userDto = new UserDto();
        userDto.setId(1L);

        Salon salon = salonService.getSalonByOwnerId(ownerId);
        return ResponseEntity.ok(salonMapper.toDto(salon));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SalonDto>> searchSalons(@RequestParam("city") String city) {
        List<SalonDto> results = salonService.searchSalonByCity(city)
                .stream()
                .map(salonMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @PatchMapping("/{salonId}")
    public ResponseEntity<SalonDto> updateSalon(@RequestBody SalonDto salonDto,
                                                @PathVariable("salonId") Long salonId) throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L); // Dummy user

        Salon updated = salonService.updateSalon(salonDto, userDto, salonId);
        return ResponseEntity.ok(salonMapper.toDto(updated));
    }
}
