package com.canbe.service;

import com.canbe.dto.CategoryDto;
import com.canbe.dto.SalonDto;
import com.canbe.dto.ServiceDto;
import com.canbe.modal.ServiceOffering;

import java.util.List;
import java.util.Set;

public interface ServiceOfferingService {
    ServiceOffering createService(SalonDto salonDto , ServiceDto serviceDto , CategoryDto categoryDto);
    ServiceOffering updateService(Long serviceId ,  ServiceOffering service);
    Set<ServiceOffering> getAllServiceBySalonId(Long salonId , Long categoryId);
    Set<ServiceOffering> getServicesByIds(Set<Long> ids);
    ServiceOffering getServiceById(Long id);


}
