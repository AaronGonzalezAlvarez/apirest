package com.aga.apirest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(mappedBy = "post",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<UserPost> userPosts;

    public Post(String title, String text, LocalDateTime date) {
        this.title = title;
        this.text = text;
        this.date = date;
    }

}
