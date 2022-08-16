package pl.jakubtworek.PasswordManager.entity;

import javax.persistence.*;

@Entity
@Table(name="category")
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;
}
