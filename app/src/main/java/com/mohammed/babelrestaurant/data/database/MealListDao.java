package com.mohammed.babelrestaurant.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mohammed.babelrestaurant.data.entity.MealListItem;

import java.util.List;

@Dao
public interface MealListDao {

    @Query("SELECT * FROM MealListItem")
    LiveData<List<MealListItem>> getAllMealList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMeal(MealListItem item);

    @Delete
    void deleteMeal(MealListItem item);
}
