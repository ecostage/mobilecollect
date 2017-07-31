package br.com.ecostage.mobilecollect.model

import br.com.ecostage.mobilecollect.ui.model.Team
import java.util.*

// This should be a data class (check the firebase deserialization to change this)
class Collect {
    var id: String? = null
    var name: String? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var classification: String? = null
    var userId: String? = null
    var photo: ByteArray? = null
    var date: Date? = null
    var team: Team? = null
}