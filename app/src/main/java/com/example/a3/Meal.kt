package com.example.a3
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

class Meal : Parcelable {
    var mealName: String?
        private set
    var mealTime: String?
        private set

    constructor(mealName: String?, mealTime: String?) {
        this.mealName = mealName
        this.mealTime = mealTime
    }

    protected constructor(`in`: Parcel) {
        mealName = `in`.readString()
        mealTime = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(mealName)
        dest.writeString(mealTime)
    }


    companion object CREATOR : Creator<Meal> {
        override fun createFromParcel(parcel: Parcel): Meal {
            return Meal(parcel)
        }

        override fun newArray(size: Int): Array<Meal?> {
            return arrayOfNulls(size)
        }
    }
}