package pl.jakubtworek.PasswordManager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="password")
public class Password {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="value")
    private String value;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_username")
    private User user;

    public Password() {
    }

    public Password(int id, String name, String value, Category category, User user) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.category = category;
        this.user = user;
    }

    public Password(String name, String value, Category category, User user) {
        this.name = name;
        this.value = value;
        this.category = category;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCategory() {
        return category.getId();
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getUser() {
        return user.getUsername();
    }

    public void setUser(User user) {
        this.user = user;
    }
}
