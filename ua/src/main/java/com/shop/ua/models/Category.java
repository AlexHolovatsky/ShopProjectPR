package com.shop.ua.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.shop.ua.models.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "categories")
    @JsonBackReference
    private Set<Goods> goods = new HashSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
