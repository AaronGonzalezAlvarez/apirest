package com.aga.apirest.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "actividades")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String name;

    @Column(name = "resumen")
    private String summary;

    @Column(name = "descripcion")
    private String description;

    @Column(name = "ubicacion")
    private String location;

    @Column(name = "hora")
    private String hour;

    @Column(name = "fecha")
    private LocalDateTime date;

    private Integer total;

    private String img;

    @Column(name = "ciudad_pueblo")
    private String zone;

    @Column(name = "provincia")
    private String province;

    @OneToMany(mappedBy = "activity",fetch = FetchType.EAGER)
    private List<UserActivity> userActivity;

}
