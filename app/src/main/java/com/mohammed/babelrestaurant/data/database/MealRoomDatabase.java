package com.mohammed.babelrestaurant.data.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mohammed.babelrestaurant.data.entity.MealListItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = MealListItem.class, version = 5, exportSchema = false)
public abstract class MealRoomDatabase extends RoomDatabase {

    public abstract MealListDao mealListDao();

    private static MealRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MealRoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MealRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MealRoomDatabase.class,
                            "meal_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
