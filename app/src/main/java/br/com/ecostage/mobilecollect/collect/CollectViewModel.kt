package br.com.ecostage.mobilecollect.collect

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by cmaia on 7/22/17.
 */
data class CollectViewModel(var id: String? = null, var name: String?, var latitude: Double?, var longitude: Double?, var classification: String?, var userId: String? = null, var date: Date? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString(),
            parcel.readString(),
            Date(parcel.readLong()))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        parcel.writeString(classification)
        parcel.writeString(userId)

        val time = date?.time

        if (time != null)
            parcel.writeLong(time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CollectViewModel> {
        override fun createFromParcel(parcel: Parcel): CollectViewModel {
            return CollectViewModel(parcel)
        }

        override fun newArray(size: Int): Array<CollectViewModel?> {
            return arrayOfNulls(size)
        }
    }


}