package br.com.ecostage.mobilecollect.collect

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import br.com.ecostage.mobilecollect.R
import br.com.ecostage.mobilecollect.category.selection.CategorySelectionActivity
import kotlinx.android.synthetic.main.activity_collect.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import java.io.File

class CollectActivity : AppCompatActivity(), CollectView {

    companion object {
        val COLLECT_ID = "CollectActivity:collectId"
        val MARKER_LATITUDE = "CollectActivity:markerLatitude"
        val MARKER_LONGITUDE = "CollectActivity:markerLongitude"
        val MAP_SNAPSHOT = "CollectActivity:MapSnapshot"
        val CAMERA_REQUEST = 1888
        val CAMERA_PERMISSION_REQUEST_CODE = 2
        val LAST_COLLECT_PHOTO_FILE_NAME = "LAST_COLLECT.jpg"
    }

    private val collectPresenter: CollectPresenter = CollectPresenterImpl(this)

    var collectLastImage : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect)

        setupView()
    }

    private fun setupView() {
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

        collectTakePhotoBtn.setOnClickListener {
            collectPresenter.takePhoto()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (collectLastImage != null) {
                collectImage.setImageURI(collectLastImage)
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE ->
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    collectPresenter.onPermissionDenied(resources.getString(R.string.collect_permission_needed))
                } else {
                    collectPresenter.takePhoto()
                }
        }
    }

    override fun showCamera() {
        if (canAccessCamera()) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            collectLastImage = Uri.fromFile(File(getExternalStorageDirectory(), LAST_COLLECT_PHOTO_FILE_NAME))
            intent.putExtra(MediaStore.EXTRA_OUTPUT, collectLastImage)

            startActivityForResult(intent, CAMERA_REQUEST)
        } else {
            collectPresenter.onPermissionsNeeded()
        }
    }

    override fun showRequestPermissionsDialog() {
        ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                CAMERA_PERMISSION_REQUEST_CODE)
    }

    override fun canAccessCamera(): Boolean  {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    override fun showMessageAsLongToast(message: String) {
        longToast(message)
    }
}
