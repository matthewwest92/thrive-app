package com.thrive.westm10.thrive;

/**
 * Created by Matthew West on 30/03/2015.
 */
public class ProfileObject {
    public String firstName;
    public String surname;
    public float height;
    public float weight;
    public String gender;
    public float dob;

    // Constructor.
    public ProfileObject(String firstName, String surname, float height, float weight, String gender, float dob) {
        this.firstName = firstName;
        this.surname = surname;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.dob = dob;
    }
}