package com.exampl.hamdi.businesscard.Model;

/**
 * Created by Jerem on 26/05/2017.
 */

public interface IBusinessCard {

    public String getFunction();

    public void setFunction(String function);

    public String getImagePath();

    public void setImagePath(String imagePath);

    public String getAddress();

    public void setAddress(String address);

    public String getMail();

    public void setMail(String mail);

    public String  getNumbers();

    public void setNumbers(String numbers);

    public String getLastName();

    public void setLastName(String lastName);

    public String getFirstName() ;

    public void setFirstName(String firstName);
    public int getId();

    public void setId(int id) ;
}
