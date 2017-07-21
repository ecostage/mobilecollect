package br.com.ecostage.mobilecollect.collect

import android.os.Parcel
import android.os.Parcelable

data class Collect(var id: String? = null, val name: String, val latitude: Double, val longitude: Double, val classification: String, val userId: String? = null) : Parcelable {
    constructor(name: String, latitude: Double, longitude: Double, classification: String, userId: String?) : this(null, name, latitude, longitude, classification, userId)

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(classification)
        parcel.writeString(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Collect> {
        override fun createFromParcel(parcel: Parcel): Collect {
            return Collect(parcel)
        }

        override fun newArray(size: Int): Array<Collect?> {
            return arrayOfNulls(size)
        }
    }

}