package com.thrive.westm10.thrive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Matthew West on 11/04/2015.
 */
public class DatabaseAdapter  {

    DatabaseHelper helper;

    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
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

    public void insertAchievementsData() {
        for(currentIndex=0; currentIndex <achievementNames.length; currentIndex++) {
            insertAchievement();
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
                db.execSQL("CREATE TABLE "+GOAL_TABLE_NAME+"("+GOAL_UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+GOAL_START_WEIGHT+" REAL, "+GOAL_DURATION+" INTEGER, "+GOAL_TYPE+" VARCHAR(255), "+GOAL_START+" REAL, "+GOAL_END+" REAL, "+GOAL_SIZE+" REAL);");
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
                db.execSQL("CREATE TABLE "+NUTRITION_TRACKER_TABLE_NAME+"("+NUTRITION_TRACKER_UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NUTRITION_TRACKER_FOOD_NAME+" VARCHAR(255), "+NUTRITION_TRACKER_CALORIES+" INTEGER, "+NUTRITION_TRACKER_TIMESTAMP+" REAL);");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try{
                db.execSQL("CREATE TABLE "+FITNESS_TABLE_NAME+"("+FITNESS_UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+FITNESS_NAME+" VARCHAR(255), "+FITNESS_CALORIES_MIN+" INTEGER, "+FITNESS_TIMESTAMP+" REAL);");

            } catch (SQLException e) {
                e.printStackTrace();
            }
            try{
                db.execSQL("CREATE TABLE "+FITNESS_TRACKER_TABLE_NAME+"("+FITNESS_TRACKER_UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+FITNESS_TRACKER_NAME+" VARCHAR(255), "+FITNESS_TRACKER_CALORIES_MIN+" INTEGER, "+FITNESS_TRACKER_TIMESTAMP+" REAL);");
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
        private static final String NUTRITION_TRACKER_TIMESTAMP = "calories";

        private static final String FITNESS_UID = "_id";
        private static final String FITNESS_NAME = "name";
        private static final String FITNESS_CALORIES_MIN = "calories_min";
        private static final String FITNESS_TIMESTAMP = "timestamp";

        private static final String FITNESS_TRACKER_UID = "_id";
        private static final String FITNESS_TRACKER_NAME = "name";
        private static final String FITNESS_TRACKER_CALORIES_MIN = "calories_min";
        private static final String FITNESS_TRACKER_TIMESTAMP = "timestamp";

    }



}
