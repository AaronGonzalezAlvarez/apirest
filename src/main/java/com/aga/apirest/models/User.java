package com.aga.apirest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
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

    public User(){

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Integer getRol() {
        return rol;
    }

    public void setRol(Integer rol) {
        this.rol = rol;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBanned() {
        return banned;
    }

    public void setBanned(int banned) {
        this.banned = banned;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Message> getMessagesEmitter() {
        return messagesEmitter;
    }

    public void setMessagesEmitter(List<Message> messagesEmitter) {
        this.messagesEmitter = messagesEmitter;
    }

    public List<Message> getMessagesAddressee() {
        return messagesAddressee;
    }

    public void setMessagesAddressee(List<Message> messagesAddressee) {
        this.messagesAddressee = messagesAddressee;
    }

    public List<Activity> getActivitiescreated() {
        return activitiescreated;
    }

    public void setActivitiescreated(List<Activity> activitiescreated) {
        this.activitiescreated = activitiescreated;
    }

    public List<Activity> getRegisteredActivities() {
        return registeredActivities;
    }

    public void setRegisteredActivities(List<Activity> registeredActivities) {
        this.registeredActivities = registeredActivities;
    }

}
