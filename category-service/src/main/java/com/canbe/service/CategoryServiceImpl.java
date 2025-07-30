package com.canbe.service;

import com.canbe.dto.SalonDto;
import com.canbe.exception.CategoryNotFoundException;
import com.canbe.exception.CategoryPermissionDeniedException;
import com.canbe.modal.Category;
import com.canbe.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category, SalonDto salonDto) {

        Category categoryToSave = new Category();
        categoryToSave.setName(category.getName());
        categoryToSave.setSalonId(salonDto.getId());
        categoryToSave.setImage(category.getImage());

        return categoryRepository.save(categoryToSave);
    }

    @Override
    public Set<Category> getAllCategoriesBySalon(Long id) {
        return categoryRepository.findBySalonId(id);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public void deleteCategoryById(Long id, Long salonId) {
        Category categoryToDelete = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if (!categoryToDelete.getSalonId().equals(salonId)) {
            throw new CategoryPermissionDeniedException();
        }

        categoryRepository.delete(categoryToDelete);
    }


}
