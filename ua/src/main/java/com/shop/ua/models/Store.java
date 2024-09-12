package com.shop.ua.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.springframework.cache.annotation.CacheConfig;

import javax.persistence.*;

@Entity
@Table(name = "stores")
@Data
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "company_full_name")
    private String companyFullName;

    @Column(name = "main_office")
    private String mainOffice;

    @Column(name = "customer_support")
    private String customerSupport;

    @Column(name = "legal_form")
    private String legalForm;

    @Column(name = "legal_entity_code")
    private int legalEntityCode;

    @Column(name = "location")
    private String location;

    @Column(name = "authorised_persons")
    private String authorisedPersons;

    @Column(name = "is_registered")
    private String isRegistered;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonManagedReference
    private User owner;

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }
}
