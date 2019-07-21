package ru.otus.hibernate.dao;

import javax.persistence.*;

@Entity
@Table(name = "phones")
public class PhoneDataSet {

    @Id
    @GeneratedValue
    @Column(name = "phone_id")
    private Long id;

    private String number;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
        PhoneDataSet that = (PhoneDataSet) o;
        return id.equals(that.id) &&
                number.equals(that.number);
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", user=" + (user != null ? user.getName() : "null") +
                '}';
    }
}
