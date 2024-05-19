package com.aga.apirest.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "mensajes")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "titulo")
    private String title;

    @Column(name = "contenido")
    private String content;

    @Column(name = "fecha_envio")
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "remitente_id")
    private User emitter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destinatario_id")
    private User addressee;

    public Message(String title, String content, LocalDateTime date, User emitter, User addressee) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.emitter = emitter;
        this.addressee = addressee;
    }
}
