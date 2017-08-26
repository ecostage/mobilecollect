package br.com.ecostage.mobilecollect.ui.collect

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
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

class CollectActivity : BaseActivity(), CollectView, SensorEventListener {

    companion object {
        val COLLECT_ID = "CollectActivity:collectId"
        val MARKER_LATITUDE = "CollectActivity:markerLatitude"
        val MARKER_LONGITUDE = "CollectActivity:markerLongitude"
        val COMPRESSED_MAP_SNAPSHOT = "CollectActivity:MapSnapshot"
        val CAMERA_REQUEST = 1888
        val CAMERA_PERMISSION_REQUEST_CODE = 2
        val LAST_COLLECT_PHOTO_FILE_NAME = "LAST_COLLECT"
        val CLASSIFICATION_REQUEST = 3
        val CLASSIFICATION_DATA_RESULT = "CollectActivity:classification"
    }

    private val collectPresenter: CollectPresenter = CollectPresenterImpl(this, this)

    private var collectLastImage: Uri? = null
    private var collectLastImagePath: String? = null
    private var collectId: String? = null

    private var viewModel = CollectViewModel()

    private var sensorManager: SensorManager? = null
    private var sensorAccelerometer: Sensor? = null
    private var sensorMagneticField: Sensor? = null

    private var valuesAccelerometer: FloatArray? = null
    private var valuesMagneticField: FloatArray? = null

    private lateinit var matrixR: FloatArray
    private lateinit var matrixI: FloatArray
    private lateinit var matrixValues: FloatArray

    private var azimuth: Double? = null
    private var photoAzimuth: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_collect)

        setupView()
        setupKeyboardUI(activity_collect)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorAccelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorMagneticField = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        valuesAccelerometer = FloatArray(3)
        valuesMagneticField = FloatArray(3)

        matrixR = FloatArray(9)
        matrixI = FloatArray(9)
        matrixValues = FloatArray(9)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // no-op
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> for (i in 0..2) {
                valuesAccelerometer?.set(i, event.values[i])
            }
            Sensor.TYPE_MAGNETIC_FIELD -> for (i in 0..2) {
                valuesMagneticField?.set(i, event.values[i])
            }
        }

        val success = SensorManager.getRotationMatrix(
                matrixR,
                matrixI,
                valuesAccelerometer,
                valuesMagneticField)

        if (success) {
            SensorManager.getOrientation(matrixR, matrixValues)

            azimuth = Math.toDegrees(matrixValues[0].toDouble())
        }
    }

    override fun onResume() {
        sensorManager?.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager?.registerListener(this, sensorMagneticField, SensorManager.SENSOR_DELAY_NORMAL)
        super.onResume()
    }

    override fun onPause() {
        sensorManager?.unregisterListener(this, sensorAccelerometer)
        sensorManager?.unregisterListener(this, sensorMagneticField)
        super.onPause()
    }

    private fun setupView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        collectId = intent.getStringExtra(COLLECT_ID)

        if (collectId != null) {
            collectPresenter.setupCollectMode(CollectView.CollectMode.VISUALIZING)

            collectId?.let { collectId ->
                collectPresenter.loadCollect(collectId)
            }
        } else {
            collectPresenter.setupCollectMode(CollectView.CollectMode.COLLECTING)

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
        collectTakePhotoBtn.setOnClickListener {
            collectPresenter.takePhoto()
        }
    }

    private fun setupTeamControllers() {
        collectTeamTextView.setOnClickListener {
            if (isNetworkAvailable()) {
                collectPresenter.selectTeam(viewModel)
            } else {
                longToast(R.string.team_selection_internet_unavailable_message)
            }
        }
        collectTeamRemoveButton.setOnClickListener { collectPresenter.removeTeamSelected(viewModel) }
    }

    private fun isNetworkAvailable() : Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun dateFormatted(now: Date?): String = SimpleDateFormat(dateFormat()).format(now)

    private fun dateFormat() = "dd/MM/yyyy HH:mm:ss"

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_collect, menu)
        return true
    }

    private fun validateForm(): Boolean {
        val classificationText = collectClassification.text.toString().trim()
        if (classificationText.isNullOrEmpty()) {
            hideProgress()
            longToast(resources.getString(R.string.collect_classification_validation_error))
            return false
        }

        if (collectLastImage == null) {
            hideProgress()
            longToast(getString(R.string.collect_photo_validation_error))
            return false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_save_collect -> {
                if (!this.validateForm())
                    return false

                viewModel.name = collectName.text.toString()
                viewModel.latitude = intent.getStringExtra(MARKER_LATITUDE).toDouble()
                viewModel.longitude = intent.getStringExtra(MARKER_LONGITUDE).toDouble()
                viewModel.date = SimpleDateFormat(dateFormat()).parse(collectDate.text.toString())

                collectLastImagePath.let { path ->
                    if (path != null) {
                        viewModel.photo = collectPresenter.compressCollectPhoto(path,
                                Bitmap.CompressFormat.JPEG, 30)
                    }
                }

                collectPresenter.save(viewModel)

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
            CAMERA_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    collectImage.setImageURI(collectLastImage)

                    if (azimuth != null) {
                        photoAzimuth = azimuth
                    }
                } else {
                    collectImage.setImageURI(null)
                    collectLastImage = null
                    collectLastImagePath = null
                }
            }
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
        viewModel.classification = classificationText
        collectClassification.text = viewModel.classification
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

            val file = File.createTempFile(LAST_COLLECT_PHOTO_FILE_NAME, ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES))

            if (Build.VERSION.SDK_INT >= 24) {
                collectLastImage = FileProvider.getUriForFile(this,
                        this.applicationContext.packageName + ".fileprovider",
                        file)
            } else {
                collectLastImage = Uri.fromFile(file)
            }
            collectLastImagePath = file.absolutePath

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
        this.showMessageAsLongToast(getString(R.string.collect_save_error_no_user_auth))
    }

    override fun returnToMap(collectViewModel: CollectViewModel?) {
        if (collectViewModel != null)
            setResult(Activity.RESULT_OK, intentFor<MapActivity>(MapActivity.COLLECT_DATA_RESULT to collectViewModel))

        finishAfterTransition()
    }

    override fun populateFields(collectViewModel: CollectViewModel) {

        collectClassification.text = collectViewModel.classification
        collectClassification.typeface = Typeface.DEFAULT
        applyCategoryColorSelected(ClassificationColorSearch().classificationColor(collectViewModel.classification))

        collectName.isFocusable = false
        collectName.isEnabled = false
        collectName.setText(collectViewModel.name, TextView.BufferType.NORMAL)

        collectDate.text = dateFormatted(collectViewModel.date)

        collectLatLng.text = resources.getString(R.string.collect_lat_lng_text,
                doubleFormatted(collectViewModel.latitude), doubleFormatted(collectViewModel.longitude))

        collectTeamTextView.isFocusable = true
        if (collectViewModel.team == null) {
            collectTeamTextView.text = getString(R.string.message_time_no_informed)
            collectTeamTextView.typeface = Typeface.defaultFromStyle(Typeface.ITALIC)
            collectTeamTextView.isEnabled = false
        } else {
            collectTeamTextView.text = collectViewModel.team?.name
            collectTeamTextView.typeface = Typeface.DEFAULT
        }

        collectViewModel.photo.let { img ->
            if (img != null) {
                collectImage.setImageBitmap(collectPresenter.convertCollectPhoto(img))
            }
        }
    }

    override fun hideImageContainers() {
        collectMapSnapshotImageContainer.visibility = View.GONE
        collectTakePhotoBtn.visibility = View.GONE
    }

    override fun showImageContainers() {
        collectMapSnapshotImageContainer.visibility = View.VISIBLE
        collectTakePhotoBtn.visibility = View.VISIBLE
    }

    override fun showTeamList(teamsList: ArrayList<TeamViewModel>) {

        val builder = android.app.AlertDialog.Builder(this)

        val arrayAdapter = ArrayAdapter<TeamViewModel>(this, android.R.layout.select_dialog_singlechoice, teamsList)

        val dialog = builder.setTitle(getString(R.string.title_select_a_team))
                .setSingleChoiceItems(arrayAdapter, -1) { dialog, i ->
                    setTeamTextView(teamsList, i)
                    dialog.dismiss()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ -> }
                .create()

        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    private fun setTeamTextView(teamsList: ArrayList<TeamViewModel>, i: Int) {
        val teamSelected = teamsList[i]
        viewModel.team = teamSelected
        collectTeamTextView.text = teamSelected.name
        collectTeamTextView.typeface = Typeface.DEFAULT
        collectTeamRemoveButton.visibility = View.VISIBLE
    }

    override fun removeTeamSelected() {
        collectTeamTextView.text = ""
        collectTeamTextView.typeface = Typeface.defaultFromStyle(Typeface.ITALIC)
        collectTeamRemoveButton.visibility = View.GONE
    }

    override fun showUserHasNoTeamsMessage() {
        Snackbar.make(linearLayoutViewContainer, R.string.message_snackbar_no_teams_available, Snackbar.LENGTH_SHORT)
                .show()
    }
}
