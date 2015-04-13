package com.thrive.westm10.thrive;

/**
 * Created by Matthew West on 30/03/2015.
 */
public class GoalObject {
    public float startWeight;
    public int duration;
    public String type;
    public float startDate;
    public float endDate;
    public float targetCals;
    public float rate;

    // Constructor.
    public GoalObject(float startWeight, int duration, String type, float startDate, float endDate, float targetCals, float rate) {
        this.startWeight = startWeight;
        this.duration = duration;
        this.type = type;
        this.startDate= startDate;
        this.endDate = endDate;
        this.targetCals = targetCals;
        this.rate = rate;
    }
}
