package com.exampl.hamdi.businesscard.Model;

import java.io.Serializable;

/**
 * Created by Hamdi on 24/04/2017.
 */

public class BusinessCard implements Serializable, IBusinessCard {

    private int id;
    private String firstName;
    private String lastName;
    private String numbers;
    private String mail;
    private String address;
    private String function;
    private String imagePath;

    public BusinessCard(){}

    public BusinessCard(int id, String firstName, String lastName, String numbers, String mail,
                        String address, String function, String imagePath) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.numbers = numbers;
        this.mail = mail;
        this.address = address;
        this.function = function;
        this.imagePath = imagePath;
    }


    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String  getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
