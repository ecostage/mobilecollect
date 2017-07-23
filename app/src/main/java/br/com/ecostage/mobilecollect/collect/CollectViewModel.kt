package br.com.ecostage.mobilecollect.collect

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by cmaia on 7/22/17.
 */
data class CollectViewModel(var id: String? = null, var name: String?, var latitude: Double?, var longitude: Double?, var classification: String?, var userId: String? = null) : Parcelable {

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
        parcel.writeDouble(latitude ?: 0.0) // This is wrong
        parcel.writeDouble(longitude ?: 0.0) // This too
        parcel.writeString(classification)
        parcel.writeString(userId)
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