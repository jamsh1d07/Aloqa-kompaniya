package com.example.aloqa_kompaniya.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    private boolean active;

    public Company(Integer id, String name, Employee director) {
        this.id = id;
        this.name = name;
        this.director = director;
    }
    @OneToOne
    private Employee director;

}
