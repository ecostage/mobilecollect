package br.com.ecostage.mobilecollect.profile.collect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import br.com.ecostage.mobilecollect.OnCollectLoadedListener
import br.com.ecostage.mobilecollect.R
import br.com.ecostage.mobilecollect.collect.Collect
import br.com.ecostage.mobilecollect.collect.CollectViewModel

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

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        // no-op
    }

    override fun onCollectLoaded(collect: Collect) {
        items.add(CollectViewModel(collect.id, collect.name, collect.latitude,
                collect.longitude, collect.classification, collect.userId, collect.date))
    }

    override fun onCollectLoadedError() {
        // no-op
    }

    private class ListRowHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.userCollectRow) as TextView
    }

}