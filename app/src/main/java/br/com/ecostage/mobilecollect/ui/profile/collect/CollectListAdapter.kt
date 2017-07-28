package br.com.ecostage.mobilecollect.ui.profile.collect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import br.com.ecostage.mobilecollect.OnCollectLoadedListener
import br.com.ecostage.mobilecollect.R
import br.com.ecostage.mobilecollect.ui.collect.Collect
import br.com.ecostage.mobilecollect.ui.collect.CollectActivity
import br.com.ecostage.mobilecollect.ui.collect.CollectViewModel
import org.jetbrains.anko.startActivity

/**
 * Created by cmaia on 7/23/17.
 */
class CollectListAdapter(val context: Context)
    : BaseAdapter(), AdapterView.OnItemClickListener, OnCollectLoadedListener {

    private val items: ArrayList<CollectViewModel> = ArrayList()
    private val inflator: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ListRowHolder

        if (convertView == null) {
            view = this.inflator.inflate(R.layout.user_collect_row_item, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        vh.label.text = items.get(position).name

        return view
    }

    override fun getItem(position: Int): Any {
        return items.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong() // Check this, the obj id is string for now
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val collectId = items[position].id
        if (collectId != null)
            context.startActivity<CollectActivity>(CollectActivity.COLLECT_ID to collectId)
    }

    override fun onCollectLoaded(collect: Collect) {
        val collectViewModel = CollectViewModel()

        collectViewModel.id = collect.id
        collectViewModel.name = collect.name
        collectViewModel.latitude = collect.latitude
        collectViewModel.longitude = collect.longitude
        collectViewModel.classification = collect.classification
        collectViewModel.userId = collect.userId
        collectViewModel.date = collect.date
        collectViewModel.photo = collect.photo

        items.add(collectViewModel)

        this.notifyDataSetChanged()
    }

    override fun onCollectLoadedError() {
        // no-op
    }

    private class ListRowHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.userCollectRow) as TextView
    }

}