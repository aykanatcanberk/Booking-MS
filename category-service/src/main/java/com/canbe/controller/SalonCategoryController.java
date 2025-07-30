package com.canbe.controller;

import com.canbe.dto.SalonDto;
import com.canbe.modal.Category;
import com.canbe.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/categories/salon-owner")
@RequiredArgsConstructor
public class SalonCategoryController {
    private  final CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {

        SalonDto salonDto = new SalonDto();
        salonDto.setId(1L);

        Category saveCategory = categoryService.saveCategory(category, salonDto);
        return ResponseEntity.ok(saveCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long id) {

        SalonDto salonDto = new SalonDto();
        salonDto.setId(1L);

        categoryService.deleteCategoryById(id , salonDto.getId());

        return ResponseEntity.ok("Category Deleted Successfully.");
    }
}
