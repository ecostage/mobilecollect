package br.com.ecostage.mobilecollect.ui.map.manage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import br.com.ecostage.mobilecollect.R

/**
 * Created by cmaia on 8/22/17.
 */
class OfflineStateMapListAdapter(val context: Context) : BaseAdapter(), AdapterView.OnItemClickListener {
    private val inflator: LayoutInflater = LayoutInflater.from(context)
    private var items: Array<OfflineStateViewModel> = arrayOf(
            OfflineStateViewModel("Acre", "AC", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Alagoas", "AL", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Amapá", "AP", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Amazonas", "AM", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Bahia", "BA", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Ceará", "CE", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Espírito Santo", "ES", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Goiás", "GO", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Maranhão", "MA", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Mato Grosso", "MT", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Mato Grosso do Sul", "MS", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Minas Gerais", "MG", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Pará", "PA", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Paraíba", "PB", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Pernambuco", "PE", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Piauí", "PI", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Rio de Janeiro", "RJ", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Rio Grande do Norte", "RN", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Rio Grande do Sul", "RS", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Rondônia", "RO", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Roraima", "RR", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Santa Catarina", "SC", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("São Paulo", "SP", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Sergipe", "SE", listOf(OfflineAreaViewModel(10.0, 10.0))),
            OfflineStateViewModel("Tocations", "TO", listOf(OfflineAreaViewModel(10.0, 10.0))))

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: OfflineStateMapListAdapter.ListRowHolder

        if (convertView == null) {
            view = this.inflator.inflate(R.layout.offline_state_row_item, parent, false)
            vh = OfflineStateMapListAdapter.ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as OfflineStateMapListAdapter.ListRowHolder
        }

        vh.label.text = items[position].name

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

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private class ListRowHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.offlineStateTextView) as TextView
    }
}