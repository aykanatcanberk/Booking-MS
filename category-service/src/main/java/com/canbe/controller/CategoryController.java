package com.canbe.controller;

import com.canbe.dto.SalonDto;
import com.canbe.modal.Category;
import com.canbe.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category,
                                                   @RequestParam("salonId") Long salonId) {

        SalonDto salonDto = new SalonDto();
        salonDto.setId(salonId);

        Category savedCategory = categoryService.saveCategory(category, salonDto);
        return ResponseEntity.ok(savedCategory);
    }

    @GetMapping("/salon/{id}")
    public ResponseEntity<Set<Category>> getAllCategoriesBySalon(@PathVariable Long id) {
        Set<Category> categories = categoryService.getAllCategoriesBySalon(id);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id,
                                                   @RequestParam("salonId") Long salonId) {
        categoryService.deleteCategoryById(id, salonId);
        return ResponseEntity.noContent().build();
    }

}
