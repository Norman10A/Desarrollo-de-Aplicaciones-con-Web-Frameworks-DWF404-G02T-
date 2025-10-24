// 📄 src/main/java/com/example/api/entity/Category.java
package com.example.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "categories")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la categoría es requerido")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Size(max = 500, message = "La descripción no puede tener más de 500 caracteres")
    @Column(length = 500)
    private String description;

    public Category() {}

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return id != null && id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}