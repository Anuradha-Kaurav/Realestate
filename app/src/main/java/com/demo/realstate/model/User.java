package com.demo.realstate.model;

/**
 * Created by shabb on 7/21/2018.
 */

public class User {

    int id;
    String name;
    String email;
    String password;
    String phoneNum;
    String userRole;

    public User(int id, String name, String email, String password, String phoneNum, String userRole) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNum = phoneNum;
        this.userRole = userRole;
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", userRole='" + userRole + '\'' +
                '}';
    }
}
