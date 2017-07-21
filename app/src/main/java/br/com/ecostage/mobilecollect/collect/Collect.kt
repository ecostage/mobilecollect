package br.com.ecostage.mobilecollect.collect

data class Collect(var id: String? = null, val name: String, val latitude: Double, val longitude: Double, val classification: String) {
    constructor(name: String, latitude: Double, longitude: Double, classification: String) : this(null, name, latitude, longitude, classification)
}