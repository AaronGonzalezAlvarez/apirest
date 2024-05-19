package com.aga.apirest.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "titulo")
    private String title;

    @Column(name = "texto")
    private String text;

    @Column(name = "fecha")
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private User user;

    public Post(String title, String text, LocalDateTime date,User user) {
        this.title = title;
        this.text = text;
        this.date = date;
        this.user = user;
    }

}
