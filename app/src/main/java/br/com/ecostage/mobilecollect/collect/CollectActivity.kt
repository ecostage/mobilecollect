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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.com.ecostage.mobilecollect.R
import br.com.ecostage.mobilecollect.category.selection.ClassificationActivity
import br.com.ecostage.mobilecollect.category.selection.ClassificationViewModel
import br.com.ecostage.mobilecollect.map.MapActivity
import kotlinx.android.synthetic.main.activity_collect.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast
import java.io.File
import java.text.DecimalFormat


class CollectActivity : AppCompatActivity(), CollectView {
    companion object {
        val COLLECT_ID = "CollectActivity:collectId"
        val MARKER_LATITUDE = "CollectActivity:markerLatitude"
        val MARKER_LONGITUDE = "CollectActivity:markerLongitude"
        val COMPRESSED_MAP_SNAPSHOT = "CollectActivity:MapSnapshot"
        val CAMERA_REQUEST = 1888
        val CAMERA_PERMISSION_REQUEST_CODE = 2
        val LAST_COLLECT_PHOTO_FILE_NAME = "LAST_COLLECT.jpg"
        val CLASSIFICATION_REQUEST = 3
    }

    private val collectPresenter: CollectPresenter = CollectPresenterImpl(this)

    private var collectLastImage: Uri? = null
    private var collectSelectedClassification : ClassificationViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_collect)

        setupView()
    }

    private fun setupView() {
        val collectId = intent.getStringExtra(COLLECT_ID)
        val compressedMapSnapshot = intent.getByteArrayExtra(COMPRESSED_MAP_SNAPSHOT)

        collectLatLng.text = resources.getString(R.string.collect_lat_lng_text, latitude(), longitude())
        collectMapImg.setImageBitmap(collectPresenter.decompressMapSnapshot(compressedMapSnapshot))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        collectClassification.setOnClickListener {
            startActivityForResult(intentFor<ClassificationActivity>(), CLASSIFICATION_REQUEST)
        }

        collectTakePhotoBtn.setOnClickListener {
            collectPresenter.takePhoto()
        }

        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_collect, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {


        when(item?.itemId) {
            R.id.action_save_collect -> {
            collectPresenter.save(name = collectName.text.toString(),
                    latitude = latitude().toDouble(),
                    longitude = longitude().toDouble())

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun longitude(): String {
        return doubleFormatted(MARKER_LONGITUDE)
    }


    private fun latitude(): String {
        return doubleFormatted(MARKER_LATITUDE)
    }

    private fun doubleFormatted(id: String): String {
        val value = intent.getStringExtra(id)
        return DecimalFormat("#.0000000").format(value.toDouble())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CAMERA_REQUEST -> if (resultCode == Activity.RESULT_OK) collectImage.setImageURI(collectLastImage)
            CLASSIFICATION_REQUEST ->  {
                if (resultCode == Activity.RESULT_OK)
                    collectSelectedClassification = data?.getParcelableExtra<ClassificationViewModel>("")
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

    override fun canAccessCamera(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    override fun showMessageAsLongToast(message: String) {
        longToast(message)
    }

    override fun showProgress() {
        collectProgress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        collectProgress.visibility = View.GONE
    }

    override fun showCollectRequestSuccess() {
        longToast(R.string.collect_save_success)
    }

    override fun showNoUserError() {
        this.showMessageAsLongToast(R.string.collect_save_error_no_user_auth.toString())
    }

    override fun returnToMap(collectViewModel: CollectViewModel?) {
        if (collectViewModel != null) // This should be parcelable
            setResult(Activity.RESULT_OK, intentFor<MapActivity>(MapActivity.COLLECT_DATA_RESULT to collectViewModel))

        finishAfterTransition()
    }
}
