package com.aga.apirest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String name;

    @Column(name = "apellido")
    private String surname;

    @Column(name = "telefono")
    private Integer phone;

    private Integer rol;

    @Column(name = "provincia")
    private String province;

    private String nick;

    private String email;

    private String password;

    @Column(name = "baneado")
    private int banned;

    private String img;

    //posts
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Post> posts;

    //mensajes
    @OneToMany(mappedBy = "emitter",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Message> messagesEmitter;

    @OneToMany(mappedBy = "addressee",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Message> messagesAddressee;

    //actividades
    @OneToMany(mappedBy = "creator",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Activity> activitiescreated;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Activity> registeredActivities;

}
