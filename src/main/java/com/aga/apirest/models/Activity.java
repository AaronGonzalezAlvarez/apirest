package com.aga.apirest.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "actividades")
public class Activity {

    public Activity(){

    }

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public int getHourlyPrice() {
        return hourlyPrice;
    }

    public void setHourlyPrice(int hourlyPrice) {
        this.hourlyPrice = hourlyPrice;
    }

    public int getActivityprice() {
        return activityprice;
    }

    public void setActivityprice(int activityprice) {
        this.activityprice = activityprice;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
