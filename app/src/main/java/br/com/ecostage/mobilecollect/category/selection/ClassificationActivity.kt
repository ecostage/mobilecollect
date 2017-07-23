package br.com.ecostage.mobilecollect.category.selection

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.ecostage.mobilecollect.R
import kotlinx.android.synthetic.main.activity_classification.*


class ClassificationActivity : AppCompatActivity(), ClassificationView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classification)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        collectClassificationList.adapter = ClassificationListAdapter(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
