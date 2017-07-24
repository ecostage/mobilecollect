package br.com.ecostage.mobilecollect.ui.profile.collect

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.ecostage.mobilecollect.R
import kotlinx.android.synthetic.main.activity_user_collect.*

class UserCollectActivity : AppCompatActivity(), UserCollectView {

    private lateinit var adapter: CollectListAdapter
    private lateinit var userCollectPresenter: UserCollectPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_collect)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        adapter = CollectListAdapter(this)
        userCollectPresenter = UserCollectPresenterImpl(this, adapter, this)

        userCollectList.adapter = adapter
        userCollectPresenter.loadCollects()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
