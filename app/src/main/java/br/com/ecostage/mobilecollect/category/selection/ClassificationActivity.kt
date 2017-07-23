package br.com.ecostage.mobilecollect.category.selection

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import br.com.ecostage.mobilecollect.R
import kotlinx.android.synthetic.main.activity_classification.*


class ClassificationActivity : AppCompatActivity(), ClassificationView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classification)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val values = arrayOf("1 Floresta",
                "1.1 Formações Florestais Naturais",
                "1.1.1 Floresta Densa",
                "1.1.2 Floresta Aberta",
                "1.1.3 Mangue",
                "1.1.4 Floresta Alagada",
                "1.1.5 Floresta Degradada",
                "1.1.6 Floresta Secundária",
                "1.2 Silvicultura",
                "2 Formações Naturais não Florestais",
                "2.1 Áreas Úmidas Naturais não Florestais",
                "2.2 Vegetação Campestre (Campos)",
                "2.3 Outras formações não Florestais",
                "3 Uso Agropecuário",
                "3.1 Pastagem",
                "3.1.1 Pastagem em Campos Naturais (integração)",
                "3.1.2 Outras Pastagens",
                "3.2 Agricultura",
                "3.2.1 Culturas Anuais",
                "3.2.2 Culturas Semi-Perene (Cana de Açucar)",
                "3.2.3 Mosaico de Cultivos",
                "3.3 Agricultura ou Pastagem (biomas)",
                "4 Áreas não vegetadas",
                "4.1 Praias e dunas",
                "4.2 Infraestrutura Urbana",
                "4.3 Outras áreas não vegetadas",
                "5 Corpos Dágua",
                "6 Não observado"
                )
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, values)

        collectClassificationList.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
