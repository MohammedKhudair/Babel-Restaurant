package com.mohammed.babelrestaurant.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.mohammed.babelrestaurant.data.database.MealListDao;
import com.mohammed.babelrestaurant.data.database.MealRoomDatabase;
import com.mohammed.babelrestaurant.data.entity.MealListItem;

import java.util.List;

public class MealRepository {
    private final MealListDao mealListDao;
    private final LiveData<List<MealListItem>> mealListItems;

    public MealRepository(Application application){
        MealRoomDatabase db = MealRoomDatabase.getInstance(application);
        mealListDao = db.mealListDao();
        mealListItems = mealListDao.getAllMealList();
    }

    public LiveData<List<MealListItem>> getMealList(){
        return mealListItems;
    }

    public void addMeal(MealListItem item){
        MealRoomDatabase.databaseWriteExecutor.execute(() -> mealListDao.addMeal(item));
    }

    public void deleteMeal(MealListItem item){
        MealRoomDatabase.databaseWriteExecutor.execute(() -> mealListDao.deleteMeal(item));
    }

}
