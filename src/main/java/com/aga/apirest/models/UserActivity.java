package com.aga.apirest.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "usuarios_actividades")
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_creador")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "id_actividad")
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "id_inscrito")
    private User registered;

}
