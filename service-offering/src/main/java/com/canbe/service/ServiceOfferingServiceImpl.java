package com.canbe.service;

import com.canbe.dto.CategoryDto;
import com.canbe.dto.SalonDto;
import com.canbe.dto.ServiceDto;
import com.canbe.exception.ServiceNotFoundException;
import com.canbe.modal.ServiceOffering;
import com.canbe.repository.ServiceOfferingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOfferingServiceImpl implements ServiceOfferingService {

    private final ServiceOfferingRepository serviceOfferingRepository;

    @Override
    public ServiceOffering createService(SalonDto salonDto, ServiceDto serviceDto, CategoryDto categoryDto) {

        ServiceOffering serviceOffering = new ServiceOffering();

        serviceOffering.setName(serviceDto.getName());
        serviceOffering.setSalonId(salonDto.getId());
        serviceOffering.setDescription(serviceDto.getDescription());
        serviceOffering.setCategoryId(categoryDto.getId());
        serviceOffering.setImage(serviceDto.getImage());
        serviceOffering.setPrice(serviceDto.getPrice());
        serviceOffering.setDuration(serviceDto.getDuration());

        return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public ServiceOffering updateService(Long serviceId, ServiceOffering service) {

        ServiceOffering serviceOffering = serviceOfferingRepository.findById(serviceId)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with id: " + serviceId));

        serviceOffering.setName(service.getName());
        serviceOffering.setDescription(service.getDescription());
        serviceOffering.setImage(service.getImage());
        serviceOffering.setPrice(service.getPrice());
        serviceOffering.setDuration(service.getDuration());

        return serviceOfferingRepository.save(serviceOffering);
    }


    @Override
    public Set<ServiceOffering> getAllServiceBySalonId(Long salonId, Long categoryId) {
        Set<ServiceOffering> services = serviceOfferingRepository.findBySalonId(salonId);

        if (categoryId != null) {
            services = services.stream()
                    .filter((service) -> service.getCategoryId() != null &&
                            service.getCategoryId().equals(categoryId)).collect(Collectors.toSet());
        }
        return services;
    }

    @Override
    public Set<ServiceOffering> getServicesByIds(Set<Long> ids) {
        List<ServiceOffering> services = serviceOfferingRepository.findAllById(ids);
        return new HashSet<>(services);
    }

    @Override
    public ServiceOffering getServiceById(Long id) {
        ServiceOffering serviceOffering = serviceOfferingRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with id: " + id));

        return serviceOffering;
    }
}
