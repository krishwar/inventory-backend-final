package com.inventory.management.entity;

import jakarta.persistence.*;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String status = "ACTIVE";

    public Customer(){}

    public Long getId(){ return id; }

    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }

    public String getStatus(){ return status; }
    public void setStatus(String status){ this.status = status; }
}