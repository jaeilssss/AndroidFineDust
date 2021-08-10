package com.example.finedust.data.model.airquality

import androidx.annotation.ColorRes
import com.google.gson.annotations.SerializedName

enum class Grade(val label :String ,
                 val emoji :String ,
                @ColorRes val colorResId : Int
                 ) {

    @SerializedName("1")
    GOOD("좋음","☺️",),
    @SerializedName("2")
    NORMAL,
    @SerializedName("3")
    BAD,
    @SerializedName("4")
    AWFUL,

    UNKNOWN
}