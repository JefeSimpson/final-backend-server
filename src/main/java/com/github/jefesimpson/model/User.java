package com.github.jefesimpson.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.util.Objects;

@DatabaseTable
public class User {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String login;
    @DatabaseField
    private String password;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private LocalDate createdDate;
    @DatabaseField(dataType = DataType.ENUM_STRING)
    private UserRole userRole;

    public User(int id, String login, String password, LocalDate createdDate, UserRole userRole) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.createdDate = createdDate;
        this.userRole = userRole;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", createdDate=" + createdDate +
                ", userRole=" + userRole +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(createdDate, user.createdDate) &&
                userRole == user.userRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, createdDate, userRole);
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public final static String COLUMN_ID = "id";
    public final static String COLUMN_LOGIN = "login";
    public final static String COLUMN_PASSWORD = "password";
    public final static String COLUMN_CREATED_DATE = "createdDate";
    public final static String COLUMN_USER_ROLE = "userRole";

}
