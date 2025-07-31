package com.canbe.controller;

import com.canbe.dto.CategoryDto;
import com.canbe.dto.SalonDto;
import com.canbe.dto.ServiceDto;
import com.canbe.modal.ServiceOffering;
import com.canbe.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/service-offering/salon-owner")
@RequiredArgsConstructor
public class SalonServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;

    @PostMapping()
    public ResponseEntity<ServiceOffering> createService(@RequestBody ServiceDto serviceDto) {

        SalonDto salonDto = new SalonDto();
        salonDto.setId(1L);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(serviceDto.getCategoryId());

        ServiceOffering createdService = serviceOfferingService.createService(salonDto, serviceDto, categoryDto);
        return ResponseEntity.ok(createdService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceOffering> updateService(@PathVariable Long id,
                                                         @RequestBody ServiceOffering serviceOffering) {

        ServiceOffering updatedService = serviceOfferingService.updateService(id, serviceOffering);
        return ResponseEntity.ok(updatedService);
    }

}
