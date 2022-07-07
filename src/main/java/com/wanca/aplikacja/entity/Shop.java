package com.wanca.aplikacja.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@ToString
@Entity
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToMany
    @JoinTable(
            name = "employee",
            joinColumns = @JoinColumn(
                    name = "shop_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Collection<User> employee;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Comment> comments;

}
