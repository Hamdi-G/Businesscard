package com.exampl.hamdi.businesscard.Model;

import com.exampl.hamdi.businesscard.Model.BusinessCard;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hamdi on 24/04/2017.
 */

public class User implements Serializable, IUser {

    private int bc_id;
    private String password;

    public User() {
        this.bc_id = 1;
        this.password = "admin";
    }

    public int getBc_id() {
        return bc_id;
    }

    public void setBc_id(int bc_id) {
        this.bc_id = bc_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




}