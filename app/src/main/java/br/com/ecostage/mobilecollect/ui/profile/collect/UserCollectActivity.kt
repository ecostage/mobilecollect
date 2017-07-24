package br.com.ecostage.mobilecollect.ui.profile.collect

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.ecostage.mobilecollect.R
import kotlinx.android.synthetic.main.activity_user_collect.*

class UserCollectActivity : AppCompatActivity() {

    private lateinit var adapter: CollectListAdapter
    private lateinit var userCollectInteractor: UserCollectInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_collect)

        adapter = CollectListAdapter(this)
        userCollectInteractor = UserCollectInteractorImpl(adapter)
        userCollectList.adapter = adapter
        userCollectInteractor.loadUserCollects()
    }
}
