package br.com.ecostage.mobilecollect.ui.category.selection

/**
 * Created by andremaia on 7/24/17.
 */
class ClassificationColorSearch {

    val colorMapping = mapOf<String, String>(
            "1 Floresta" to "#129912",
            "1.1 Formações Florestais Naturais" to "#1F4423",
            "1.1.1 Floresta Densa" to "#006400",
            "1.1.2 Floresta Aberta" to "#77A605",
            "1.1.3 Mangue" to "#737300",
            "1.1.4 Floresta Alagada" to "#76A5AF",
            "1.1.5 Floresta Degradada" to "#29EEE4",
            "1.1.6 Floresta Secundária" to "#00FF00",
            "1.2 Silvicultura" to "#935132",
            "2 Formações Naturais não Florestais" to "#9DFCAC",
            "2.1 Áreas Úmidas Naturais não Florestais" to "#45C2A5",
            "2.2 Vegetação Campestre (Campos)" to "#B8AF4F",
            "2.3 Outras formações não Florestais" to "#F1C232",
            "3 Uso Agropecuário" to "#FFC278",
            "3.1 Pastagem" to "#F0D991",
            "3.1.1 Pastagem em Campos Naturais (integração)" to "#CF7F3B",
            "3.1.2 Outras Pastagens" to "#A0D0DE",
            "3.2 Agricultura" to "#E974ED",
            "3.2.1 Culturas Anuais" to "#D8A6BD",
            "3.2.2 Culturas Semi-Perene (Cana de Açucar)" to "#C27BA0",
            "3.2.3 Mosaico de Cultivos" to "#CE3D3D",
            "3.3 Agricultura ou Pastagem (biomas)" to "#FFEFC3",
            "4 Áreas não vegetadas" to "#EA9999",
            "4.1 Praias e dunas" to "#FFFF9E",
            "4.2 Infraestrutura Urbana" to "#E60000",
            "4.3 Outras áreas não vegetadas" to "#686868",
            "5 Corpos Dágua" to "#004DFF",
            "6 Não observado" to "#E1E1E1"
    )


    fun classificationColor(name: String?): String? {
        return colorMapping.get(name)
    }
}