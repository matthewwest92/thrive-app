package com.thrive.westm10.thrive;

/**
 * Created by Matthew West on 30/03/2015.
 */
public class FoodObject {
    public String foodName;
    public float serving;
    public int cals;
    public float fat;
    public float cholesterol;
    public float sodium;
    public float potassium;
    public float carbs;
    public float sugar;
    public float protein;
    public float calcium;
    public float iron;

    // Constructor.
    public FoodObject(String foodName, float serving, int cals, float fat, float cholesterol, float sodium, float potassium, float carbs, float sugar, float protein, float calcium, float iron) {
        this.foodName = foodName;
        this.serving = serving;
        this.cals = cals;
        this.fat = fat;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.potassium = potassium;
        this.carbs = carbs;
        this.sugar = sugar;
        this.protein = protein;
        this.calcium = calcium;
        this.iron = iron;
    }
}
