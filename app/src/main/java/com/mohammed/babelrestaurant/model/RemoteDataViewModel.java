package com.mohammed.babelrestaurant.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mohammed.babelrestaurant.callback.UserAddress;
import com.mohammed.babelrestaurant.data.entity.MealListItem;
import com.mohammed.babelrestaurant.data.entity.SnackItem;

import java.util.List;

public class RemoteDataViewModel extends ViewModel {
    private final RemoteDataRepository mRemoteDateRepository;
    private final UserRepository mUserRepository;

    public RemoteDataViewModel() {
        mRemoteDateRepository = new RemoteDataRepository();
        mUserRepository = new UserRepository();
    }

    public LiveData<List<MealListItem>> getMealList() {
        return mRemoteDateRepository.getAllMealList();
    }

    public LiveData<List<MealListItem>> getMealTypeList(String type) {
        return mRemoteDateRepository.getMealTypeList(type);
    }

    public LiveData<List<SnackItem>> getStartersLis(){
        return mRemoteDateRepository.getStartersLis();
    }


    public LiveData<String> addItemToFavorites(MealListItem item){
        return mUserRepository.addItemToFavorites(item);
    }

    public void getUserAddress(UserAddress userAddress){
         mUserRepository.getUserAddress(userAddress);
    }


        public LiveData<List<MealListItem>> getFavorites(){
       return mUserRepository.getFavorites();
    }

 }
