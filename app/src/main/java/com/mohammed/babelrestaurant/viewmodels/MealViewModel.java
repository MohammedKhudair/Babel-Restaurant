package com.mohammed.babelrestaurant.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mohammed.babelrestaurant.data.MealRepository;
import com.mohammed.babelrestaurant.data.entity.MealListItem;

import java.util.List;

public class MealViewModel extends AndroidViewModel {

    private final MealRepository mRepository;
    private final LiveData<List<MealListItem>> mealListItems;

    public MealViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MealRepository(application);
        mealListItems = mRepository.getMealList();
    }

    public LiveData<List<MealListItem>> getMealList() {
        return mealListItems;
    }

    public void addMeal(MealListItem item) {
        mRepository.addMeal(item);
    }

    public void deleteMeal(MealListItem item) {
        mRepository.deleteMeal(item);
    }

}
