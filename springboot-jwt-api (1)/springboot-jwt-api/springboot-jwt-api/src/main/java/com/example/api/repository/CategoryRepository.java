package com.example.api.repository;

import com.example.api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Buscar por nombre exacto
    Optional<Category> findByName(String name);

    // Verificar existencia por nombre
    boolean existsByName(String name);

    // Buscar por nombre que contenga (case insensitive)
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Category> findByNameContainingIgnoreCase(@Param("name") String name);

    // Buscar categorías con descripción no nula
    List<Category> findByDescriptionIsNotNull();

    // Buscar categorías con descripción nula
    List<Category> findByDescriptionIsNull();

    // Contar categorías
    long count();
}