package com.isidroid.b21.utils

import android.os.Parcel
import android.os.Parcelable

@Suppress("UNCHECKED_CAST")
class ParcelArg : Parcelable {
    var value: Any

    constructor(value: Any) {
        this.value = value
    }

    constructor(parcel: Parcel) {
        this.value = Any()
    }

    fun <T> get() = value as T

    override fun writeToParcel(dest: Parcel?, flags: Int) {}

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ParcelArg> {

        override fun createFromParcel(parcel: Parcel): ParcelArg {
            return ParcelArg(parcel)
        }

        override fun newArray(size: Int): Array<ParcelArg?> {
            return arrayOfNulls(size)
        }
    }
}