package br.com.ecostage.mobilecollect.category.selection

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import br.com.ecostage.mobilecollect.R

/**
 * Created by cmaia on 7/23/17.
 */
class ClassificationListAdapter(val context: Context) : BaseAdapter()  {

    private val inflator: LayoutInflater = LayoutInflater.from(context)
    private var items: Array<ClassificationViewModel> = arrayOf(
            ClassificationViewModel("1", "1 Floresta", "#129912", null),
            ClassificationViewModel("2", "1.1 Formações Florestais Naturais", "#1F4423", "1"),
            ClassificationViewModel("3", "1.1.1 Floresta Densa", "#006400", "2"),
            ClassificationViewModel("4", "1.1.2 Floresta Aberta", "#77A605", "2"),
            ClassificationViewModel("5", "1.1.3 Mangue", "#737300", "2"),
            ClassificationViewModel("6", "1.1.4 Floresta Alagada", "#76A5AF", "2"),
            ClassificationViewModel("7", "1.1.5 Floresta Degradada", "#29EEE4", "2"),
            ClassificationViewModel("8", "1.1.6 Floresta Secundária", "#00FF00", "2"),
            ClassificationViewModel("9", "1.2 Silvicultura", "#935132", "1"),
            ClassificationViewModel("10", "2 Formações Naturais não Florestais", "#9DFCAC", null),
            ClassificationViewModel("11", "2.1 Áreas Úmidas Naturais não Florestais", "#45C2A5", "9"),
            ClassificationViewModel("12", "2.2 Vegetação Campestre (Campos)", "#B8AF4F", "9"),
            ClassificationViewModel("13", "2.3 Outras formações não Florestais", "#F1C232", "9"),
            ClassificationViewModel("14", "3 Uso Agropecuário", "#FFC278", null),
            ClassificationViewModel("15", "3.1 Pastagem", "#F0D991", "14"),
            ClassificationViewModel("16", "3.1.1 Pastagem em Campos Naturais (integração)", "#CF7F3B", "15"),
            ClassificationViewModel("17", "3.1.2 Outras Pastagens", "#A0D0DE", "15"),
            ClassificationViewModel("18", "3.2 Agricultura", "#E974ED", "14"),
            ClassificationViewModel("19", "3.2.1 Culturas Anuais", "#D8A6BD", "18"),
            ClassificationViewModel("20", "3.2.2 Culturas Semi-Perene (Cana de Açucar)", "#C27BA0", "18"),
            ClassificationViewModel("21", "3.2.3 Mosaico de Cultivos", "#CE3D3D", "18"),
            ClassificationViewModel("22", "3.3 Agricultura ou Pastagem (biomas)", "#FFEFC3", "14"),
            ClassificationViewModel("23", "4 Áreas não vegetadas", "#EA9999", null),
            ClassificationViewModel("24", "4.1 Praias e dunas", "#FFFF9E", "23"),
            ClassificationViewModel("25", "4.2 Infraestrutura Urbana", "#E60000", "23"),
            ClassificationViewModel("26", "4.3 Outras áreas não vegetadas", "#686868", "23"),
            ClassificationViewModel("27", "5 Corpos Dágua", "#004DFF", null),
            ClassificationViewModel("28", "6 Não observado", "#E1E1E1", null)
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ListRowHolder

        if (convertView == null) {
            view = this.inflator.inflate(R.layout.classification_row_item, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        vh.label.text = items[position].name

        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_circle_24dp)
        drawable.colorFilter = PorterDuffColorFilter(Color.parseColor(items[position].colorHexadecimal), PorterDuff.Mode.SRC_IN)

        vh.label.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

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
        val label: TextView = row?.findViewById(R.id.collectRowClassification) as TextView
    }
}