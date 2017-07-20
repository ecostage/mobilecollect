package br.com.ecostage.mobilecollect.collect

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.ecostage.mobilecollect.R
import kotlinx.android.synthetic.main.activity_collect.*
import org.jetbrains.anko.startActivity

class CollectActivity : AppCompatActivity() {

    companion object {
        val COLLECT_ID = "CollectActivity:collectId"
        val MARKER_LATITUDE = "CollectActivity:markerLatitude"
        val MARKER_LONGITUDE = "CollectActivity:markerLongitude"
        val MAP_SNAPSHOT = "CollectActivity:MapSnapshot"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect)

        val collectId = intent.getStringExtra(COLLECT_ID)
        val latitude = intent.getStringExtra(MARKER_LATITUDE)
        val longitude = intent.getStringExtra(MARKER_LONGITUDE)
//        val mapSnapshot : Bitmap? = intent.getParcelableArrayExtra(MAP_SNAPSHOT) as? Bitmap

        collectLatLng.setText(resources.getString(R.string.collect_lat_lng_text, latitude, longitude))
//        collectMapImg.setImageBitmap(mapSnapshot)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        collectClassification.setOnClickListener {
            startActivity<CategorySelectionActivity>()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
