package com.thrive.westm10.thrive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.style.TtsSpan;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Matthew West on 11/04/2015.
 */
public class DatabaseAdapter  {

    DatabaseHelper helper;

    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
    }

    public void saveProfile(ProfileObject profile) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.PROFILE_FIRST_NAME, DatabaseHelper.PROFILE_SURNAME, DatabaseHelper.PROFILE_HEIGHT, DatabaseHelper.PROFILE_WEIGHT, DatabaseHelper.PROFILE_GENDER, DatabaseHelper.PROFILE_DOB};
        Cursor cursor=db.query(DatabaseHelper.PROFILE_TABLE_NAME, columns, null, null, null, null, null);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.PROFILE_FIRST_NAME, profile.firstName);
        contentValues.put(DatabaseHelper.PROFILE_SURNAME, profile.surname);
        contentValues.put(DatabaseHelper.PROFILE_HEIGHT, profile.height);
        contentValues.put(DatabaseHelper.PROFILE_WEIGHT, profile.weight);
        contentValues.put(DatabaseHelper.PROFILE_GENDER, profile.gender);
        contentValues.put(DatabaseHelper.PROFILE_DOB, profile.dob);

        if(cursor.moveToNext()) {   //profile exists
            db.update(DatabaseHelper.PROFILE_TABLE_NAME, contentValues, DatabaseHelper.PROFILE_UID+" ='1'",null);
        } else {    //profile doesn't exist
            db.insert(DatabaseHelper.PROFILE_TABLE_NAME, null, contentValues);
        }
    }

    public void saveGoal(GoalObject goal) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.GOAL_START_WEIGHT, DatabaseHelper.GOAL_DURATION, DatabaseHelper.GOAL_TYPE, DatabaseHelper.GOAL_START, DatabaseHelper.GOAL_END, DatabaseHelper.GOAL_SIZE, DatabaseHelper.GOAL_TARGET_CALS};
        Cursor cursor=db.query(DatabaseHelper.GOAL_TABLE_NAME, columns, null, null, null, null, null);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.GOAL_START_WEIGHT, goal.startWeight);
        contentValues.put(DatabaseHelper.GOAL_DURATION, goal.duration);
        contentValues.put(DatabaseHelper.GOAL_TYPE, goal.type);
        contentValues.put(DatabaseHelper.GOAL_START, goal.startDate);
        contentValues.put(DatabaseHelper.GOAL_END, goal.endDate);
        contentValues.put(DatabaseHelper.GOAL_SIZE, goal.rate);
        contentValues.put(DatabaseHelper.GOAL_TARGET_CALS, goal.targetCals);

        if(cursor.moveToNext()) {   //profile exists
            db.update(DatabaseHelper.GOAL_TABLE_NAME, contentValues, DatabaseHelper.GOAL_UID+" ='1'",null);
        } else {    //profile doesn't exist
            db.insert(DatabaseHelper.GOAL_TABLE_NAME, null, contentValues);
        }
    }

    public GoalObject getGoal() {
        GoalObject goal;
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.GOAL_START_WEIGHT, DatabaseHelper.GOAL_DURATION, DatabaseHelper.GOAL_TYPE, DatabaseHelper.GOAL_START, DatabaseHelper.GOAL_END, DatabaseHelper.GOAL_SIZE, DatabaseHelper.GOAL_TARGET_CALS};
        Cursor cursor=db.query(DatabaseHelper.GOAL_TABLE_NAME, columns, null, null, null, null, null);
        if(cursor.moveToNext()) {
            int startWeightIndex = cursor.getColumnIndex(DatabaseHelper.GOAL_START_WEIGHT);
            int durationIndex = cursor.getColumnIndex(DatabaseHelper.GOAL_DURATION);
            int typeIndex = cursor.getColumnIndex(DatabaseHelper.GOAL_TYPE);
            int startIndex = cursor.getColumnIndex(DatabaseHelper.GOAL_START);
            int endIndex = cursor.getColumnIndex(DatabaseHelper.GOAL_END);
            int targetIndex = cursor.getColumnIndex(DatabaseHelper.GOAL_TARGET_CALS);
            int sizeIndex = cursor.getColumnIndex(DatabaseHelper.GOAL_SIZE);

            goal = new GoalObject(cursor.getFloat(startWeightIndex),cursor.getInt(durationIndex),cursor.getString(typeIndex),cursor.getFloat(startIndex), cursor.getFloat(endIndex),cursor.getFloat(targetIndex),cursor.getFloat(sizeIndex));

        } else {
            goal = new GoalObject( 0, 0, "" , 0 ,0,0000, 0);
        }

        return goal;


    }

    public void insertFoodData(BufferedReader buffer) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String line = "";
        db.beginTransaction();
        try {
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split("\\|");
                ContentValues cv = new ContentValues(3);
                cv.put(DatabaseHelper.NUTRITION_FOOD_NAME, colums[0].trim());
                cv.put(DatabaseHelper.NUTRITION_SERVING_SIZE, colums[1].trim());
                cv.put(DatabaseHelper.NUTRITION_CALORIES, colums[2].trim());
                cv.put(DatabaseHelper.NUTRITION_FAT, colums[3].trim());
                cv.put(DatabaseHelper.NUTRITION_CHOLESTEROL, colums[4].trim());
                cv.put(DatabaseHelper.NUTRITION_SODIUM, colums[5].trim());
                cv.put(DatabaseHelper.NUTRITION_POTASSIUM, colums[6].trim());
                cv.put(DatabaseHelper.NUTRITION_CARBOHYDRATES, colums[7].trim());
                cv.put(DatabaseHelper.NUTRITION_SUGAR, colums[8].trim());
                cv.put(DatabaseHelper.NUTRITION_PROTEIN, colums[9].trim());
                cv.put(DatabaseHelper.NUTRITION_CALCIUM, colums[10].trim());
                cv.put(DatabaseHelper.NUTRITION_IRON, colums[11].trim());
                db.insert(DatabaseHelper.NUTRITION_TABLE_NAME, null, cv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }




    public void storeExercise(String name, float calories, float time) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FITNESS_TRACKER_NAME, name);
        contentValues.put(DatabaseHelper.FITNESS_TRACKER_CALORIES_MIN, calories);
        contentValues.put(DatabaseHelper.FITNESS_TRACKER_TIMESTAMP, time);
        db.insert(DatabaseHelper.FITNESS_TRACKER_TABLE_NAME, null, contentValues);
    }

    public void deleteExercise(String name, float calories, float time) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(DatabaseHelper.FITNESS_TRACKER_TABLE_NAME,DatabaseHelper.FITNESS_TRACKER_TIMESTAMP+" ="+time+" AND "+DatabaseHelper.FITNESS_TRACKER_CALORIES_MIN+" ="+calories+" AND "+DatabaseHelper.FITNESS_TRACKER_NAME+" ='"+name+"'", null);
    }

    public FitnessItem[] getFitnessDay(float time) {
        ArrayList<String> nameList= new ArrayList<String>();
        ArrayList<String> calsList= new ArrayList<String>();
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String[] columns = {DatabaseHelper.FITNESS_TRACKER_NAME,DatabaseHelper.FITNESS_TRACKER_CALORIES_MIN};
        Cursor cursor=db.query(DatabaseHelper.FITNESS_TRACKER_TABLE_NAME, columns, DatabaseHelper.FITNESS_TRACKER_TIMESTAMP+" ="+time+"", null, null, null, null);
        if(cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.FITNESS_TRACKER_NAME);
                int calsIndex = cursor.getColumnIndex(DatabaseHelper.FITNESS_TRACKER_CALORIES_MIN);

                nameList.add(cursor.getString(nameIndex));
                calsList.add(String.valueOf(cursor.getFloat(calsIndex)));
            }

            FitnessItem results[] = new FitnessItem[nameList.size()];
            for(int j =0;j<nameList.size();j++){
                results[j] = new FitnessItem(nameList.get(j), calsList.get(j));
            }
            return results;
        } else {
            FitnessItem results[] = new FitnessItem[1];
            results[0] = new FitnessItem("No Exercise recorded yet.", "");
            return results;
        }

    }

    public FoodItem[] getFoodDay(float time) {
        ArrayList<String> nameList= new ArrayList<String>();
        ArrayList<String> calsList= new ArrayList<String>();
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String[] columns = {DatabaseHelper.NUTRITION_TRACKER_FOOD_NAME,DatabaseHelper.NUTRITION_TRACKER_CALORIES};
        Cursor cursor=db.query(DatabaseHelper.NUTRITION_TRACKER_TABLE_NAME, columns, DatabaseHelper.NUTRITION_TRACKER_TIMESTAMP+" ="+time+"", null, null, null, null);
        if(cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.NUTRITION_TRACKER_FOOD_NAME);
                int calsIndex = cursor.getColumnIndex(DatabaseHelper.NUTRITION_TRACKER_CALORIES);

                nameList.add(cursor.getString(nameIndex));
                calsList.add(String.valueOf(cursor.getFloat(calsIndex)));
            }

            FoodItem results[] = new FoodItem[nameList.size()];
            for(int j =0;j<nameList.size();j++){
                results[j] = new FoodItem(nameList.get(j), calsList.get(j));
            }
            return results;
        } else {
            FoodItem results[] = new FoodItem[1];
            results[0] = new FoodItem("No food recorded yet.", "");
            return results;
        }

    }

    public void deleteFood(String name, float calories, float time) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(DatabaseHelper.NUTRITION_TRACKER_TABLE_NAME,DatabaseHelper.NUTRITION_TRACKER_TIMESTAMP+" ="+time+" AND "+DatabaseHelper.NUTRITION_TRACKER_CALORIES+" ="+calories+" AND "+DatabaseHelper.NUTRITION_TRACKER_FOOD_NAME+" ='"+name+"'", null);
    }

    public void storeFood(String name, float calories, float time) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NUTRITION_TRACKER_FOOD_NAME, name);
        contentValues.put(DatabaseHelper.NUTRITION_TRACKER_CALORIES, calories);
        contentValues.put(DatabaseHelper.NUTRITION_TRACKER_TIMESTAMP, time);
        db.insert(DatabaseHelper.NUTRITION_TRACKER_TABLE_NAME, null, contentValues);
    }

    public double getDaysFoodCalories(float time) {
        ArrayList<Double> calsList = new ArrayList<Double>();
        double result=0;
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String[] columns = {DatabaseHelper.NUTRITION_TRACKER_FOOD_NAME,DatabaseHelper.NUTRITION_TRACKER_CALORIES};
        Cursor cursor=db.query(DatabaseHelper.NUTRITION_TRACKER_TABLE_NAME, columns, DatabaseHelper.NUTRITION_TRACKER_TIMESTAMP+" ="+time+"", null, null, null, null);

        if(cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int calsIndex = cursor.getColumnIndex(DatabaseHelper.NUTRITION_TRACKER_CALORIES);
                result += cursor.getFloat(calsIndex);
            }
        } else {
            result = 0000;
        }

        return result;
    }

    public double getDaysExerciseCalories(float time) {
        ArrayList<Double> calsList = new ArrayList<Double>();
        double result=0;
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String[] columns = {DatabaseHelper.FITNESS_TRACKER_NAME,DatabaseHelper.FITNESS_TRACKER_CALORIES_MIN};
        Cursor cursor=db.query(DatabaseHelper.FITNESS_TRACKER_TABLE_NAME, columns, DatabaseHelper.FITNESS_TRACKER_TIMESTAMP+" ="+time+"", null, null, null, null);

        if(cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int calsIndex = cursor.getColumnIndex(DatabaseHelper.FITNESS_TRACKER_CALORIES_MIN);
                result += cursor.getFloat(calsIndex);
            }
        } else {
            result = 0000;
        }

        return result;
    }

    public ProfileObject getProfile() {
        ProfileObject profile;
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.PROFILE_FIRST_NAME, DatabaseHelper.PROFILE_SURNAME, DatabaseHelper.PROFILE_HEIGHT, DatabaseHelper.PROFILE_WEIGHT, DatabaseHelper.PROFILE_GENDER, DatabaseHelper.PROFILE_DOB};
        Cursor cursor=db.query(DatabaseHelper.PROFILE_TABLE_NAME, columns, null, null, null, null, null);
        if(cursor.moveToNext()) {
            int firstIndex = cursor.getColumnIndex(DatabaseHelper.PROFILE_FIRST_NAME);
            int surIndex = cursor.getColumnIndex(DatabaseHelper.PROFILE_SURNAME);
            int heightIndex = cursor.getColumnIndex(DatabaseHelper.PROFILE_HEIGHT);
            int weightIndex = cursor.getColumnIndex(DatabaseHelper.PROFILE_WEIGHT);
            int genderIndex = cursor.getColumnIndex(DatabaseHelper.PROFILE_GENDER);
            int dobIndex = cursor.getColumnIndex(DatabaseHelper.PROFILE_DOB);
            profile = new ProfileObject(cursor.getString(firstIndex),cursor.getString(surIndex),cursor.getFloat(heightIndex),cursor.getFloat(weightIndex), cursor.getString(genderIndex),cursor.getFloat(dobIndex));

        } else {
            profile = new ProfileObject("","", 0 ,0, "",0);
        }

        return profile;


    }

    public long insertAchievement() {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ACHIEVEMENT_NAME, achievementNames[currentIndex]);
        contentValues.put(DatabaseHelper.ACHIEVEMENT_DESCRIPTION, achievementDescriptions[currentIndex]);
        contentValues.put(DatabaseHelper.ACHIEVEMENT_STATUS, "0");
        long id=db.insert(DatabaseHelper.ACHIEVEMENT_TABLE_NAME, null, contentValues);
        return id;
    }

    public long insertExercise() {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FITNESS_NAME, exerciseNames[currentIndex]);
        contentValues.put(DatabaseHelper.FITNESS_CALORIES_MIN, exerciseCals[currentIndex]);
        long id=db.insert(DatabaseHelper.FITNESS_TABLE_NAME, null, contentValues);
        return id;
    }

    public void insertAchievementsData() {
        for(currentIndex=0; currentIndex <achievementNames.length; currentIndex++) {
            insertAchievement();
        }
    }

    public void insertExerciseData() {
        for(currentIndex=0; currentIndex <exerciseNames.length; currentIndex++) {
            insertExercise();
        }
    }

    public AchievementItem[] getAchievementsData() {
        currentIndex=0;
        AchievementItem[] AchievementList = new AchievementItem[10];
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.ACHIEVEMENT_NAME,DatabaseHelper.ACHIEVEMENT_DESCRIPTION,DatabaseHelper.ACHIEVEMENT_STATUS};
        Cursor cursor=db.query(DatabaseHelper.ACHIEVEMENT_TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.ACHIEVEMENT_NAME);
            int desciptionIndex = cursor.getColumnIndex(DatabaseHelper.ACHIEVEMENT_DESCRIPTION);
            int statusIndex = cursor.getColumnIndex(DatabaseHelper.ACHIEVEMENT_STATUS);

            if(cursor.getString(statusIndex).equals("0")) {
                AchievementList[currentIndex] = new AchievementItem(R.drawable.test_icon_0, cursor.getString(nameIndex), cursor.getString(desciptionIndex));
            } else {
                AchievementList[currentIndex] = new AchievementItem(R.drawable.test_icon, cursor.getString(nameIndex), cursor.getString(desciptionIndex));
            }
            if(currentIndex+1 < AchievementList.length) {
                currentIndex++;
            }
            Log.e("Current Index" + currentIndex, "MSG");
        }
        return AchievementList;
    }

    public void unlockAchievement(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ACHIEVEMENT_STATUS, "1");
        db.update(DatabaseHelper.ACHIEVEMENT_TABLE_NAME, contentValues, DatabaseHelper.ACHIEVEMENT_NAME+" ='"+name+"'",null);
    }

    public Boolean isUnlocked(String name) {
        Boolean decision=false;
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ACHIEVEMENT_STATUS, "1");
        String[] columns = {DatabaseHelper.ACHIEVEMENT_NAME, DatabaseHelper.ACHIEVEMENT_STATUS};
        Cursor cursor=db.query(DatabaseHelper.ACHIEVEMENT_TABLE_NAME, columns, DatabaseHelper.ACHIEVEMENT_STATUS+" = 1 AND "+ DatabaseHelper.ACHIEVEMENT_NAME+" ='"+name+"'", null, null, null, null);

        if(cursor.getCount() != 0) {
            decision=true;
        }

        return decision;
    }

    public float getExcerciseData(String name) {
        float result = 0.0f;
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String[] columns = {DatabaseHelper.FITNESS_CALORIES_MIN};
        Cursor cursor=db.query(DatabaseHelper.FITNESS_TABLE_NAME, columns, DatabaseHelper.FITNESS_NAME+" ='"+name+"'", null, null, null, null);
        if(cursor.moveToNext()) {
            int calorieIndex = cursor.getColumnIndex(DatabaseHelper.FITNESS_CALORIES_MIN);
            result = cursor.getFloat(calorieIndex);
        }
        return result;
    }

    public String[] getExerciseNameList() {
        currentIndex=0;

        ArrayList<String> arrlist= new ArrayList<String>();

        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.FITNESS_NAME};
        Cursor cursor=db.query(DatabaseHelper.FITNESS_TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.FITNESS_NAME);
            arrlist.add(cursor.getString(nameIndex));
        }

        String results[] = new String[arrlist.size()];
        for(int j =0;j<arrlist.size();j++){
            results[j] = arrlist.get(j);
        }

        return results;
    }

    public String[] getFoodNameList() {
        currentIndex=0;

        ArrayList<String> arrlist= new ArrayList<String>();

        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.NUTRITION_FOOD_NAME};
        Cursor cursor=db.query(DatabaseHelper.NUTRITION_TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.NUTRITION_FOOD_NAME);
            arrlist.add(cursor.getString(nameIndex));
        }

        String results[] = new String[arrlist.size()];
        for(int j =0;j<arrlist.size();j++){
            results[j] = arrlist.get(j);
        }

        return results;
    }

    public FoodObject getFoodData(String name) {
        FoodObject result;
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String[] columns = {DatabaseHelper.NUTRITION_FOOD_NAME, DatabaseHelper.NUTRITION_CALORIES, DatabaseHelper.NUTRITION_FAT, DatabaseHelper.NUTRITION_CHOLESTEROL, DatabaseHelper.NUTRITION_SODIUM, DatabaseHelper.NUTRITION_POTASSIUM, DatabaseHelper.NUTRITION_CARBOHYDRATES, DatabaseHelper.NUTRITION_SUGAR, DatabaseHelper.NUTRITION_PROTEIN, DatabaseHelper.NUTRITION_CALCIUM, DatabaseHelper.NUTRITION_IRON};
        Cursor cursor=db.query(DatabaseHelper.NUTRITION_TABLE_NAME, columns, DatabaseHelper.NUTRITION_FOOD_NAME+" ='"+name+"'", null, null, null, null);
        if(cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.NUTRITION_FOOD_NAME);
            int calorieIndex = cursor.getColumnIndex(DatabaseHelper.NUTRITION_CALORIES);
            int fatIndex = cursor.getColumnIndex(DatabaseHelper.NUTRITION_FAT);
            int cholesterolIndex = cursor.getColumnIndex(DatabaseHelper.NUTRITION_CHOLESTEROL);
            int sodiumIndex = cursor.getColumnIndex(DatabaseHelper.NUTRITION_SODIUM);
            int potassiumIndex = cursor.getColumnIndex(DatabaseHelper.NUTRITION_POTASSIUM);
            int carbIndex = cursor.getColumnIndex(DatabaseHelper.NUTRITION_CARBOHYDRATES);
            int sugarIndex = cursor.getColumnIndex(DatabaseHelper.NUTRITION_SUGAR);
            int proteinIndex = cursor.getColumnIndex(DatabaseHelper.NUTRITION_PROTEIN);
            int calciumIndex = cursor.getColumnIndex(DatabaseHelper.NUTRITION_CALCIUM);
            int ironIndex = cursor.getColumnIndex(DatabaseHelper.NUTRITION_IRON);
            result = new FoodObject(cursor.getString(nameIndex), 0.0f,cursor.getInt(calorieIndex), cursor.getFloat(fatIndex), cursor.getFloat(cholesterolIndex), cursor.getFloat(sodiumIndex), cursor.getFloat(potassiumIndex), cursor.getFloat(carbIndex), cursor.getFloat(sugarIndex), cursor.getFloat(proteinIndex), cursor.getFloat(calciumIndex),cursor.getFloat(ironIndex));
        } else {
            result = new FoodObject("", 100, 0, 0.0f, 0.0f, 0.0f, 0.0f,0.0f,0.0f,0.0f,0.0f,0.0f);
        }
        return result;
    }

    int currentIndex=0;

    String[] achievementNames = {
            "You're Set!",
            "Here we go again!",
            "First steps",
            "Full up",
            "Perfection",
            "Fitness Fanatic",
            "Man vs Food",
            "Completionist",
            "Attentive",
            "High Achiever"
    };
    String[] achievementDescriptions = {
            "Complete your profile",
            "Set a second goal after completing a first!",
            "Set a goal to complete",
            "Fill out the Nutrition Diary for a day",
            "Complete a goal from start to finish without any 'slip ups'",
            "Complete three or more different types of exercise in one day",
            "Consume over 3000 calories in a day",
            "Complete both your fitness and nutrition diary for a day",
            "Fill out your fitness diary for 7 days in a row",
            "Unlock all other achievements"
    };

    String[] exerciseNames= {
            "Badminton",
            "Basketball",
            "Bicycling (5.5 mph)",
            "Bicycling (9.5 mph)",
            "Climbing hills (no load)",
            "Climbing hills (with 9 lb. load)",
            "Climbing hills (with 22 lb. load)",
            "Cooking",
            "Dancing, ballroom",
            "Dance, Aerobic, medium",
            "Dance, Aerobic, intense",
            "Golf",
            "Grocery shopping",
            "Jumping Rope (70 jumps per minute)",
            "Jumping Rope (125 jumps per minute)",
            "Jumping Rope (145 jumps per minute)",
            "Mowing the lawn",
            "Racquetball",
            "Raking leaves",
            "Running (6-minute mile)",
            "Running (8-minute mile)",
            "Running (9-minute mile)",
            "Sitting Still",
            "Skiing, cross-country, walking",
            "Skiing, cross-country, uphill",
            "Snowshoeing, soft snow",
            "Squats",
            "Swimming, crawl, slow",
            "Swimming, crawl, fast",
            "Swimming, breast stroke, fast",
            "Table tennis",
            "Walking, normal pace, asphalt road",
            "Walking, normal pace, fields & hills",
            "Weeding",
            "Weight training, free weights",
            "Weight training, circuit training",
            "Volleyball"
    };

    float[] exerciseCals = {
            0.044f,
            0.063f,
            0.029f,
            0.045f,
            0.055f,
            0.058f,
            0.064f,
            0.022f,
            0.023f,
            0.046f,
            0.061f,
            0.038f,
            0.028f,
            0.074f,
            0.08f,
            0.089f,
            0.051f,
            0.081f,
            0.025f,
            0.115f,
            0.095f,
            0.087f,
            0.009f,
            0.065f,
            0.125f,
            0.075f,
            0.096f,
            0.058f,
            0.071f,
            0.074f,
            0.031f,
            0.036f,
            0.037f,
            0.033f,
            0.039f,
            0.042f,
            0.023f

    };

    static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL("CREATE TABLE "+ACHIEVEMENT_TABLE_NAME+"("+ACHIEVEMENT_UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ACHIEVEMENT_NAME+" VARCHAR(255), "+ACHIEVEMENT_DESCRIPTION+" VARCHAR(255), "+ACHIEVEMENT_STATUS+" VARCHAR(255));");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try{
                db.execSQL("CREATE TABLE "+PROFILE_TABLE_NAME+"("+PROFILE_UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+PROFILE_FIRST_NAME+" VARCHAR(255), "+PROFILE_SURNAME+" VARCHAR(255), "+PROFILE_HEIGHT+" REAL, "+PROFILE_WEIGHT+" REAL, "+PROFILE_GENDER+" VARCHAR(255), "+PROFILE_DOB+" REAL);");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try{
                db.execSQL("CREATE TABLE "+GOAL_TABLE_NAME+"("+GOAL_UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+GOAL_START_WEIGHT+" REAL, "+GOAL_DURATION+" INTEGER, "+GOAL_TYPE+" VARCHAR(255), "+GOAL_START+" REAL, "+GOAL_END+" REAL, "+GOAL_TARGET_CALS+" REAL, "+GOAL_SIZE+" REAL);");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try{
                db.execSQL("CREATE TABLE "+NUTRITION_TABLE_NAME+"("+NUTRITION_UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NUTRITION_FOOD_NAME+" VARCHAR(255), "+NUTRITION_SERVING_SIZE+" REAL, "+NUTRITION_CALORIES+" INTEGER, "+NUTRITION_FAT+" REAL, "+NUTRITION_CHOLESTEROL+" REAL, "+NUTRITION_SODIUM+" REAL, "+NUTRITION_POTASSIUM+" REAL, "+NUTRITION_CARBOHYDRATES+" REAL, "+NUTRITION_SUGAR+" REAL, "+NUTRITION_PROTEIN+" REAL, "+NUTRITION_CALCIUM+" REAL, "+NUTRITION_IRON+" REAL);");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try{
                db.execSQL("CREATE TABLE "+NUTRITION_TRACKER_TABLE_NAME+"("+NUTRITION_TRACKER_UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NUTRITION_TRACKER_FOOD_NAME+" VARCHAR(255), "+NUTRITION_TRACKER_CALORIES+" INTEGER, "+NUTRITION_TRACKER_TIMESTAMP+" REAL);");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try{
                db.execSQL("CREATE TABLE "+FITNESS_TABLE_NAME+"("+FITNESS_UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+FITNESS_NAME+" VARCHAR(255), "+FITNESS_CALORIES_MIN+" REAL);");

            } catch (SQLException e) {
                e.printStackTrace();
            }
            try{
                db.execSQL("CREATE TABLE "+FITNESS_TRACKER_TABLE_NAME+"("+FITNESS_TRACKER_UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+FITNESS_TRACKER_NAME+" VARCHAR(255), "+FITNESS_TRACKER_CALORIES_MIN+" REAL, "+FITNESS_TRACKER_TIMESTAMP+" REAL);");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ACHIEVEMENT_TABLE_NAME+";");
            db.execSQL("DROP TABLE IF EXISTS "+PROFILE_TABLE_NAME+";");
            db.execSQL("DROP TABLE IF EXISTS "+GOAL_TABLE_NAME+";");
            db.execSQL("DROP TABLE IF EXISTS "+NUTRITION_TABLE_NAME+";");
            db.execSQL("DROP TABLE IF EXISTS "+FITNESS_TABLE_NAME+";");
            db.execSQL("DROP TABLE IF EXISTS "+FITNESS_TRACKER_TABLE_NAME+";");
            db.execSQL("DROP TABLE IF EXISTS "+NUTRITION_TRACKER_TABLE_NAME+";");
            onCreate(db);
        }

        private static final String DATABASE_NAME = "thrive_data";
        private static final int DATABASE_VERSION=1;

        private static final String ACHIEVEMENT_TABLE_NAME = "achievement";
        private static final String PROFILE_TABLE_NAME = "profile";
        private static final String GOAL_TABLE_NAME = "goal";
        private static final String NUTRITION_TABLE_NAME = "nutrition";
        private static final String FITNESS_TABLE_NAME = "fitness";
        private static final String FITNESS_TRACKER_TABLE_NAME = "fitness_tracker";
        private static final String NUTRITION_TRACKER_TABLE_NAME = "nutrition_tracker";


        private static final String ACHIEVEMENT_UID = "_id";
        private static final String ACHIEVEMENT_NAME = "name";
        private static final String ACHIEVEMENT_DESCRIPTION = "description";
        private static final String ACHIEVEMENT_STATUS = "status";

        private static final String PROFILE_UID = "_id";
        private static final String PROFILE_FIRST_NAME = "first_name";
        private static final String PROFILE_SURNAME = "surname_name";
        private static final String PROFILE_HEIGHT = "height";
        private static final String PROFILE_WEIGHT = "weight";
        private static final String PROFILE_GENDER = "gender";
        private static final String PROFILE_DOB = "DOB";

        private static final String GOAL_UID = "_id";
        private static final String GOAL_START_WEIGHT = "start_weight";
        private static final String GOAL_DURATION = "duration";
        private static final String GOAL_TYPE = "type";
        private static final String GOAL_START = "start";
        private static final String GOAL_END = "end";
        private static final String GOAL_SIZE = "size";
        private static final String GOAL_TARGET_CALS = "target_cals";

        private static final String NUTRITION_UID = "_id";
        private static final String NUTRITION_FOOD_NAME = "name";
        private static final String NUTRITION_SERVING_SIZE = "serving_size";
        private static final String NUTRITION_CALORIES = "calories";
        private static final String NUTRITION_FAT = "fat";
        private static final String NUTRITION_CHOLESTEROL = "cholesterol";
        private static final String NUTRITION_SODIUM = "sodium";
        private static final String NUTRITION_POTASSIUM = "potassium";
        private static final String NUTRITION_CARBOHYDRATES = "carbohydrates";
        private static final String NUTRITION_SUGAR = "sugar";
        private static final String NUTRITION_PROTEIN = "protein";
        private static final String NUTRITION_CALCIUM = "calcium";
        private static final String NUTRITION_IRON = "iron";

        private static final String NUTRITION_TRACKER_UID = "_id";
        private static final String NUTRITION_TRACKER_FOOD_NAME = "name";
        private static final String NUTRITION_TRACKER_CALORIES = "calories";
        private static final String NUTRITION_TRACKER_TIMESTAMP = "timestamp";

        private static final String FITNESS_UID = "_id";
        private static final String FITNESS_NAME = "name";
        private static final String FITNESS_CALORIES_MIN = "calories_min";

        private static final String FITNESS_TRACKER_UID = "_id";
        private static final String FITNESS_TRACKER_NAME = "name";
        private static final String FITNESS_TRACKER_CALORIES_MIN = "calories_min";
        private static final String FITNESS_TRACKER_TIMESTAMP = "timestamp";

    }



}
