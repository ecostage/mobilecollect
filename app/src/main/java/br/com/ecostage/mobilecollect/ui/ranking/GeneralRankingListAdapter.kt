package br.com.ecostage.mobilecollect.ui.ranking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import br.com.ecostage.mobilecollect.R

/**
 * Created by cmaia on 8/1/17.
 */
class GeneralRankingListAdapter(val context: Context, val items: List<UserRankingDetailsViewModel>)
    : BaseAdapter() {

    private val inflator: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val vh : ListRowHolder

        if (convertView == null) {
            view = this.inflator.inflate(R.layout.general_ranking_row_item, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        vh.userEmail.text = items[position].userEmail //Change tho email
        vh.userPlace.text = "1"
        vh.userPoints.text = items[position].userRankingPoints.toString()

        return view
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

    private class ListRowHolder(row: View?) {
        val userPlace = row?.findViewById(R.id.generalRankingUserPlace) as TextView
        val userEmail = row?.findViewById(R.id.generalRankingUserEmail) as TextView
        val userPoints = row?.findViewById(R.id.generalRankingUserPoints) as TextView
    }
}