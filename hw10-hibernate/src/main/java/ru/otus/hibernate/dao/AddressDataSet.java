package ru.otus.hibernate.dao;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class AddressDataSet {

    @Id
    @GeneratedValue
    @Column(name = "address_id")
    private Long id;

    private String street;

    @OneToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDataSet that = (AddressDataSet) o;
        return id.equals(that.id) &&
                street.equals(that.street);
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", user=" + (user != null ? user.getName() : "null") +
                '}';
    }
}
