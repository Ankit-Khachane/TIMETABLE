package com.ankit.timetable.network;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ankit.timetable.utils.AppConstants;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApiHelper {


    private ParseQuery<ParseObject> parseQuery;


    public ApiHelper(Context context) {
        parseQuery = ParseQuery.getQuery(AppConstants.TABLE_NAME);

    }

    public LiveData<List<ParseObject>> loadDataFromServer() {
        //check this if condition in repository model for this method
        LiveData<List<ParseObject>> result = new MutableLiveData<>(new ArrayList<>());
        //if (!mPreferenceHelper.getDataSynced()) {
        parseQuery.orderByAscending(AppConstants.Table.SEQUENCE);
        parseQuery.findInBackground((scheduleList, e) -> {
            if (scheduleList != null && e == null) {
                //result.addAll(scheduleList);
                Objects.requireNonNull(result.getValue())
                        .addAll(scheduleList);
            }
        });
        return result;
    /*} else {
      Timber.i("Data is Already Synced");
    }*/
    }
}
