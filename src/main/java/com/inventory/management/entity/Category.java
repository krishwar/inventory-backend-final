package com.inventory.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String status = "ACTIVE";

    @OneToMany(mappedBy = "category")
    @JsonIgnore   
    private List<Product> products;
}