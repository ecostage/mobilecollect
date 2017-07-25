package br.com.ecostage.mobilecollect.ui.category.selection

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

        val adapter = ClassificationListAdapter(this)
        collectClassificationList.adapter = adapter
        collectClassificationList.onItemClickListener = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
