package br.com.ecostage.mobilecollect.ui.map

import android.os.Parcel
import android.os.Parcelable
import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions

/**
 * Created by cmaia on 8/21/17.
 */
class MapboxCustomMarkerOptions() : BaseMarkerOptions<MapboxCustomMarker, MapboxCustomMarkerOptions>() {
    var tag: String? = null

    constructor(parcel: Parcel) : this() {
        tag = parcel.readString()
    }

    override fun getThis(): MapboxCustomMarkerOptions {
        return this
    }

    override fun getMarker(): MapboxCustomMarker {
        return MapboxCustomMarker(this, tag.orEmpty())
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tag)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MapboxCustomMarkerOptions> {
        override fun createFromParcel(parcel: Parcel): MapboxCustomMarkerOptions {
            return MapboxCustomMarkerOptions(parcel)
        }

        override fun newArray(size: Int): Array<MapboxCustomMarkerOptions?> {
            return arrayOfNulls(size)
        }
    }
}