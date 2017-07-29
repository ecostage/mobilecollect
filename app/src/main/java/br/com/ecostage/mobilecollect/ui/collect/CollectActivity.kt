package br.com.ecostage.mobilecollect.ui.collect

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import br.com.ecostage.mobilecollect.BaseActivity
import br.com.ecostage.mobilecollect.R
import br.com.ecostage.mobilecollect.ui.category.selection.ClassificationActivity
import br.com.ecostage.mobilecollect.ui.category.selection.ClassificationColorSearch
import br.com.ecostage.mobilecollect.ui.category.selection.ClassificationViewModel
import br.com.ecostage.mobilecollect.ui.helper.ProgressBarHandler
import br.com.ecostage.mobilecollect.ui.map.MapActivity
import kotlinx.android.synthetic.main.activity_collect.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class CollectActivity : BaseActivity(), CollectView {

    companion object {
        val COLLECT_ID = "CollectActivity:collectId"
        val MARKER_LATITUDE = "CollectActivity:markerLatitude"
        val MARKER_LONGITUDE = "CollectActivity:markerLongitude"
        val COMPRESSED_MAP_SNAPSHOT = "CollectActivity:MapSnapshot"
        val CAMERA_REQUEST = 1888
        val CAMERA_PERMISSION_REQUEST_CODE = 2
        val LAST_COLLECT_PHOTO_FILE_NAME = "LAST_COLLECT.jpg"
        val CLASSIFICATION_REQUEST = 3
        val CLASSIFICATION_DATA_RESULT = "CollectActivity:classification"
    }

    private val collectPresenter: CollectPresenter = CollectPresenterImpl(this)

    private var collectLastImage: Uri? = null
    private var collectId: String? = null

    private var model = Collect()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_collect)

        setupView()
        setupKeyboardUI(activity_collect)
    }

    private fun setupView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        collectId = intent.getStringExtra(COLLECT_ID)

        if (collectId != null) {
            collectId?.let { collectId ->
                collectMapSnapshotImageContainer.visibility = View.GONE
                collectPhotoContainer.visibility = View.GONE
                collectPresenter.loadCollect(collectId)
            }
        } else {
            val compressedMapSnapshot = intent.getByteArrayExtra(COMPRESSED_MAP_SNAPSHOT)

            collectLatLng.text = resources.getString(R.string.collect_lat_lng_text, latitude(), longitude())
            val now = Calendar.getInstance().time
            collectDate.text = dateFormatted(now)
            collectMapSnapshotImage.setImageBitmap(collectPresenter.decompressMapSnapshot(compressedMapSnapshot))

            setupClassificationControllers()
            setupTeamControllers()
            setupPhotoControllers()
        }
    }

    private fun setupClassificationControllers() {
        collectClassification.setOnClickListener {
            startActivityForResult(intentFor<ClassificationActivity>(), CLASSIFICATION_REQUEST)
        }
    }

    private fun setupPhotoControllers() {
        collectTakePhotoBtn.setOnClickListener { collectPresenter.takePhoto() }
    }

    private fun setupTeamControllers() {
        collectTeamTextView.setOnClickListener { collectPresenter.selectTeam(model) }
        collectTeamRemoveButton.setOnClickListener { collectPresenter.removeTeamSelected(model) }
    }

    private fun dateFormatted(now: Date?): String = SimpleDateFormat(dateFormat()).format(now)

    private fun dateFormat() = "dd/MM/yyyy HH:mm:ss"

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_collect, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_save_collect -> {
                val classificationText = collectClassification.text.toString()
                if (classificationText.isNullOrEmpty()) {
                    longToast(resources.getString(R.string.collect_classification_validation_error))
                    return false
                }

                val collectDate = SimpleDateFormat(dateFormat()).parse(collectDate.text.toString())

                model.name = collectName.text.toString()
                model.latitude = intent.getStringExtra(MARKER_LATITUDE).toDouble()
                model.longitude = intent.getStringExtra(MARKER_LONGITUDE).toDouble()
                model.date = collectDate

                collectPresenter.save(model)

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun longitude(): String {
        return doubleFormatted(intent.getStringExtra(MARKER_LONGITUDE).toDouble())
    }


    private fun latitude(): String {
        return doubleFormatted(intent.getStringExtra(MARKER_LATITUDE).toDouble())
    }

    private fun doubleFormatted(value: Double?): String {
        if (value != null)
            return DecimalFormat("#.0000000").format(value)

        return ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CAMERA_REQUEST -> if (resultCode == Activity.RESULT_OK) collectImage.setImageURI(collectLastImage)
            CLASSIFICATION_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    val selectedClassification = data?.getParcelableExtra<ClassificationViewModel>(CLASSIFICATION_DATA_RESULT)
                    val classificationText = selectedClassification?.name
                    val classificationColor = selectedClassification?.colorHexadecimal

                    applyCategorySelected(classificationText, classificationColor)


                }
            }
        }
    }

    private fun applyCategorySelected(classificationText: String?, classificationColor: String?) {
        applyColorTextSelected(classificationText)
        applyCategoryColorSelected(classificationColor)
    }

    private fun applyColorTextSelected(classificationText: String?) {
        model.classification = classificationText
        collectClassification.text = model.classification
    }

    private fun applyCategoryColorSelected(classificationColor: String?) {
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_circle_24dp)
        drawable.colorFilter = PorterDuffColorFilter(Color.parseColor(classificationColor), PorterDuff.Mode.SRC_IN)
        collectClassification.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
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

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (collectId != null) {
            menu.getItem(0).isEnabled = false
        }
        return true
    }

    override fun showCamera() {
        if (canAccessCamera()) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (Build.VERSION.SDK_INT >= 24) {
                collectLastImage = FileProvider.getUriForFile(this,
                        this.applicationContext.packageName + ".br.com.ecostage.mobilecollect.provider",
                        File(getExternalStorageDirectory(), LAST_COLLECT_PHOTO_FILE_NAME))
            } else {
                collectLastImage = Uri.fromFile(File(getExternalStorageDirectory(), LAST_COLLECT_PHOTO_FILE_NAME))
            }

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
        ProgressBarHandler().showProgress(true, scrollViewActivity, collectProgress)
    }

    override fun showProgressBarForTeams() {
        collectTeamProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        ProgressBarHandler().showProgress(false, scrollViewActivity, collectProgress)
    }

    override fun hideProgressBarForTeams() {
        collectTeamProgressBar.visibility = View.GONE
    }

    override fun showCollectRequestSuccess() {
        longToast(R.string.collect_save_success)
    }

    override fun showNoUserError() {
        this.showMessageAsLongToast(R.string.collect_save_error_no_user_auth.toString())
    }

    override fun returnToMap(collectViewModel: CollectViewModel?) {
        if (collectViewModel != null)
            setResult(Activity.RESULT_OK, intentFor<MapActivity>(MapActivity.COLLECT_DATA_RESULT to collectViewModel))

        finishAfterTransition()
    }

    override fun populateFields(collectViewModel: CollectViewModel) {

        collectClassification.text = collectViewModel.classification
        applyCategoryColorSelected(ClassificationColorSearch().classificationColor(collectViewModel.classification))

        collectName.isFocusable = false
        collectName.isEnabled = false
        collectName.setText(collectViewModel.name, TextView.BufferType.EDITABLE)

        collectDate.text = dateFormatted(collectViewModel.date)

        collectLatLng.text = resources.getString(R.string.collect_lat_lng_text,
                doubleFormatted(collectViewModel.latitude), doubleFormatted(collectViewModel.longitude))

        collectTeamTextView.isFocusable = false
        collectTeamTextView.isEnabled = false
    }


    override fun showTeamList(teamsList: Array<CharSequence>) {

        val builder = android.app.AlertDialog.Builder(this)

        builder.setTitle("Selecione um time")
                .setSingleChoiceItems(teamsList, 3) { dialog, i ->
                    setTeamTextView(teamsList, i)
                    dialog.dismiss()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ -> }
                .create()
                .show()
    }

    private fun setTeamTextView(teamsList: Array<CharSequence>, i: Int) {
        collectTeamTextView.text = teamsList[i]
        collectTeamTextView.typeface = Typeface.DEFAULT
        collectTeamRemoveButton.visibility = View.VISIBLE
    }

    override fun removeTeamSelected() {
        collectTeamTextView.text = ""
        collectTeamTextView.typeface = Typeface.defaultFromStyle(Typeface.ITALIC)
        collectTeamRemoveButton.visibility = View.GONE
    }
}
