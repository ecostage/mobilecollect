package br.com.ecostage.mobilecollect.ui.collect

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by cmaia on 7/22/17.
 */
class CollectViewModel() : Parcelable {
    var id: String? = null
    var name: String? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var classification: String? = null
    var userId: String? = null
    var date: Date? = null
    var photo: ByteArray? = null
    var team: TeamViewModel? = null
    var photoAzimuth: Double? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
        latitude = parcel.readValue(Double::class.java.classLoader) as? Double
        longitude = parcel.readValue(Double::class.java.classLoader) as? Double
        classification = parcel.readString()
        userId = parcel.readString()
        photo = parcel.createByteArray()
        team = parcel.readParcelable(TeamViewModel::class.java.classLoader)
        photoAzimuth = parcel.readValue(Double::class.java.classLoader) as? Double
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        parcel.writeString(classification)
        parcel.writeString(userId)
        parcel.writeByteArray(photo)
        parcel.writeParcelable(team, flags)
        parcel.writeValue(photoAzimuth)
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