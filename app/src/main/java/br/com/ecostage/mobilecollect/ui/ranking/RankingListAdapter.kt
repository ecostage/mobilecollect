package br.com.ecostage.mobilecollect.ui.ranking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import br.com.ecostage.mobilecollect.R
import br.com.ecostage.mobilecollect.ui.collect.TeamViewModel

/**
 * Created by cmaia on 8/1/17.
 */
class RankingListAdapter(val context: Context, val items: List<RankingViewModel>)
    : BaseAdapter() {

    private val inflator: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val vh : ListRowHolder

        if (convertView == null) {
            view = this.inflator.inflate(R.layout.ranking_row_item, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        if (items[position].team != null) {
            vh.userOrTeamName.text = items[position].team?.name
            vh.team = items[position].team
        }
        else {
            vh.userOrTeamName.text = items[position].user?.email
        }

        vh.place.text = items[position].position.toString()
        vh.points.text = items[position].points.toString()

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
        val place = row?.findViewById(R.id.rankingPlace) as TextView
        val userOrTeamName = row?.findViewById(R.id.rankingUserOrTeamName) as TextView
        val points = row?.findViewById(R.id.rankingPoints) as TextView
        var team: TeamViewModel? = null
    }
}