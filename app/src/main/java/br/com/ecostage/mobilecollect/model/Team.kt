package br.com.ecostage.mobilecollect.model

/**
 * Created by andremaia on 7/27/17.
 */
class Team {
    var id: String? = null
    var name: String? = null

    override fun toString(): String {
        return name ?: ""
    }
}