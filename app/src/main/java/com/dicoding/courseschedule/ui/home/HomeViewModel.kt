package com.dicoding.courseschedule.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.util.QueryType

class HomeViewModel(private val repository: DataRepository): ViewModel() {

    private val _queryType = MutableLiveData<QueryType>()

    init {
        _queryType.value = QueryType.CURRENT_DAY
    }

    fun setQueryType(queryType: QueryType) {
        Log.d("hvmthoriq", "setQueryType: $queryType")
        _queryType.value = queryType
    }

    fun getTodaySchedule() = repository.getNearestSchedule(_queryType.value!!)

}
