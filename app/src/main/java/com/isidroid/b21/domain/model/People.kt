package com.isidroid.b21.domain.model

import androidx.annotation.Keep
import com.isidroid.b21.domain.model.test.Car
import java.util.Date

data class People(val name: String, val age: Int, val dateOfBirth: Date, val work: Work?, val position: String?, val car: Car?)
data class Work(val title: String)