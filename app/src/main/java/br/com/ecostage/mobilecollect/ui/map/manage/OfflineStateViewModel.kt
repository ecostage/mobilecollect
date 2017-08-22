package br.com.ecostage.mobilecollect.ui.map.manage

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by cmaia on 8/22/17.
 */
data class OfflineStateViewModel(val name: String,
                                 val uf: String,
                                 val offlineAreas: List<OfflineAreaViewModel>) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(OfflineAreaViewModel))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(uf)
        parcel.writeTypedList(offlineAreas)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OfflineStateViewModel> {
        override fun createFromParcel(parcel: Parcel): OfflineStateViewModel {
            return OfflineStateViewModel(parcel)
        }

        override fun newArray(size: Int): Array<OfflineStateViewModel?> {
            return arrayOfNulls(size)
        }
    }
}