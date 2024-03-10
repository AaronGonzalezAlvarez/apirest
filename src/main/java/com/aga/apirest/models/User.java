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

    @OneToMany(mappedBy = "creator",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<UserPost> userPostsCreator;

    @OneToMany(mappedBy = "commentator",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<UserPost> userPostsCommentator;

    //actividad
    @OneToMany(mappedBy = "creator",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<UserActivity> userActivityCreator;

    @OneToMany(mappedBy = "registered",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<UserActivity> userActivityRegistered;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phone=" + phone +
                ", rol=" + rol +
                ", province='" + province + '\'' +
                ", nick='" + nick + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userPostsCreator=" + userPostsCreator.size() +
                ", userPostsCommentator=" + userPostsCommentator.size() +
                ", userActivityCreator=" + userActivityCreator.size() +
                ", userActivityRegistered=" + userActivityRegistered.size() +
                '}';
    }
}
