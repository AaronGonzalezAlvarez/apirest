package com.aga.apirest.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Column(name = "hora_inicio")
    private LocalTime startTime;

    @Column(name = "hora_fin")
    private LocalTime endTime;

    @Column(name = "fecha")
    private LocalDate date;

    private Integer total;

    private String img;

    @Column(name = "ciudad_pueblo")
    private String zone;

    @Column(name = "provincia")
    private String province;

    private String material;

    @Column(name = "desplazamiento")
    private String displacement;

    @Column(name = "precio_hora")
    private int hourlyPrice;

    @Column(name = "precio_actividad")
    private int activityprice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_ofertante")
    private User creator;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "inscripciones",
            joinColumns = @JoinColumn(name = "actividad_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<User> users;


    public Activity(String name, String summary, String description, String location, LocalTime startTime, LocalTime endTime, LocalDate date, Integer total, String zone, String province, String material, String displacement, int hourlyPrice, int activityprice, User creator) {
        this.name = name;
        this.summary = summary;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.total = total;
        this.zone = zone;
        this.province = province;
        this.material = material;
        this.displacement = displacement;
        this.hourlyPrice = hourlyPrice;
        this.activityprice = activityprice;
        this.creator = creator;
    }
}
