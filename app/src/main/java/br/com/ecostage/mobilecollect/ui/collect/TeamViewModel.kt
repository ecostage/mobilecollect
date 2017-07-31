package br.com.ecostage.mobilecollect.ui.collect

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by cmaia on 7/30/17.
 */
class TeamViewModel() : Parcelable {
    var id: String? = null
    var name: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TeamViewModel> {
        override fun createFromParcel(parcel: Parcel): TeamViewModel {
            return TeamViewModel(parcel)
        }

        override fun newArray(size: Int): Array<TeamViewModel?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return name ?: ""
    }
}