package com.aga.apirest.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "usuarios_posts")
public class UserPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_creador")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "id_post")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "id_comentador")
    private User commentator;

    @Column(name = "comentario")
    private String comment;

    @Column(name = "fecha")
    private LocalDateTime date;


    public UserPost(User creator, Post post) {
        this.creator = creator;
        this.post = post;
    }
}
