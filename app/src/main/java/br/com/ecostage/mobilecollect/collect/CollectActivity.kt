package br.com.ecostage.mobilecollect.collect

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.ecostage.mobilecollect.R
import kotlinx.android.synthetic.main.activity_collect.*

fun Context.CollectIntent(collectId : Int) : Intent {
    return Intent(this, CollectActivity::class.java).apply {
        putExtra(COLLECT_ID, collectId)
    }
}

fun Context.CollectIntent(latitude: Double, longitude: Double) : Intent {
    return Intent(this, CollectActivity::class.java).apply {
        putExtra(MARKER_LATITUDE, latitude)
        putExtra(MARKER_LONGITUDE, longitude)
    }
}

private const val COLLECT_ID = "collectId"
private const val MARKER_LATITUDE = "markerLatitude"
private const val MARKER_LONGITUDE = "markerLongitude"

class CollectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect)
    }
}
