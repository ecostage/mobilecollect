package br.com.ecostage.mobilecollect.ui.map.manage

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by cmaia on 8/22/17.
 */
data class OfflineAreaViewModel(val latitude: Double, val longitude: Double): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OfflineAreaViewModel> {
        override fun createFromParcel(parcel: Parcel): OfflineAreaViewModel {
            return OfflineAreaViewModel(parcel)
        }

        override fun newArray(size: Int): Array<OfflineAreaViewModel?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is OfflineAreaViewModel) return false

        if (other.latitude != latitude) return false
        if (other.longitude != longitude) return false

        return true
    }

    override fun hashCode(): Int {
        val result = 1
        return 31 * result + latitude.hashCode() + longitude.hashCode()
    }
}