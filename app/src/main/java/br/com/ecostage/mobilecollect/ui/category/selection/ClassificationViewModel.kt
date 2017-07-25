package br.com.ecostage.mobilecollect.ui.category.selection

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by cmaia on 7/23/17.
 */
class ClassificationViewModel(val id : String?, val name: String, val colorHexadecimal: String, val parent: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(colorHexadecimal)
        parcel.writeString(parent)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ClassificationViewModel> {
        override fun createFromParcel(parcel: Parcel): ClassificationViewModel {
            return ClassificationViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ClassificationViewModel?> {
            return arrayOfNulls(size)
        }
    }

}