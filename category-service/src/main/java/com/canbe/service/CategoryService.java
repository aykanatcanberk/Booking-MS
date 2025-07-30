package com.canbe.service;

import com.canbe.dto.SalonDto;
import com.canbe.modal.Category;

import java.util.Set;

public interface CategoryService {
    Category saveCategory(Category category , SalonDto salonDto);
    Set<Category> getAllCategoriesBySalon(Long id);
    Category getCategoryById(Long id);
    void deleteCategoryById(Long id , Long salonId);
}
