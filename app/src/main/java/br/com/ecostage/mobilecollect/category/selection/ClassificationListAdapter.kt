package br.com.ecostage.mobilecollect.category.selection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import br.com.ecostage.mobilecollect.R

/**
 * Created by cmaia on 7/23/17.
 */
class ClassificationListAdapter(context: Context) : BaseAdapter()  {

    private val inflator: LayoutInflater = LayoutInflater.from(context)
    private var items: Array<ClassificationViewModel> = arrayOf(
            ClassificationViewModel("1", "1 Floresta", "129912", null),
            ClassificationViewModel("1", "1.1 Formações Florestais Naturais", "1F4423", null),
            ClassificationViewModel("1", "1.1.1 Floresta Densa", "006400", null),
            ClassificationViewModel("1", "1.1.2 Floresta Aberta", "77A605", null),
            ClassificationViewModel("1", "1.1.3 Mangue", "737300", null),
            ClassificationViewModel("1", "1.1.4 Floresta Alagada", "76A5AF", null),
            ClassificationViewModel("1", "1.1.5 Floresta Degradada", "29EEE4", null),
            ClassificationViewModel("1", "1.1.6 Floresta Secundária", "00FF00", null),
            ClassificationViewModel("1", "1.2 Silvicultura", "935132", null),
            ClassificationViewModel("1", "2 Formações Naturais não Florestais", "9DFCAC", null),
            ClassificationViewModel("1", "2.1 Áreas Úmidas Naturais não Florestais", "45C2A5", null),
            ClassificationViewModel("1", "2.2 Vegetação Campestre (Campos)", "B8AF4F", null),
            ClassificationViewModel("1", "2.3 Outras formações não Florestais", "F1C232", null),
            ClassificationViewModel("1", "3 Uso Agropecuário", "FFC278", null),
            ClassificationViewModel("1", "3.1 Pastagem", "F0D991", null),
            ClassificationViewModel("1", "3.1.1 Pastagem em Campos Naturais (integração)", "CF7F3B", null),
            ClassificationViewModel("1", "3.1.2 Outras Pastagens", "A0D0DE", null),
            ClassificationViewModel("1", "3.2 Agricultura", "E974ED", null),
            ClassificationViewModel("1", "3.2.1 Culturas Anuais", "D8A6BD", null),
            ClassificationViewModel("1", "3.2.2 Culturas Semi-Perene (Cana de Açucar)", "C27BA0", null),
            ClassificationViewModel("1", "3.2.3 Mosaico de Cultivos", "CE3D3D", null),
            ClassificationViewModel("1", "3.3 Agricultura ou Pastagem (biomas)", "FFEFC3", null),
            ClassificationViewModel("1", "4 Áreas não vegetadas", "EA9999", null),
            ClassificationViewModel("1", "4.1 Praias e dunas", "FFFF9E", null),
            ClassificationViewModel("1", "4.2 Infraestrutura Urbana", "E60000", null),
            ClassificationViewModel("1", "4.3 Outras áreas não vegetadas", "686868", null),
            ClassificationViewModel("1", "5 Corpos Dágua", "004DFF", null),
            ClassificationViewModel("1", "6 Não observado", "E1E1E1", null)
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ListRowHolder

        if (convertView == null) {
            view = this.inflator.inflate(R.layout.classification_row_item, parent, false)
            vh = ListRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        vh.label.text = items[position].name

        return view
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong() // Check this, the obj id is string for now
    }

    override fun getCount(): Int {
        return items.size
    }

    private class ListRowHolder(row: View?) {
        public val label: TextView

        init {
            this.label = row?.findViewById(R.id.collectRowClassification) as TextView
        }
    }
}