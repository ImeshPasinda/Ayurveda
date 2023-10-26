import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ayurveda.R
import com.example.ayurveda.Remedy
import java.util.Locale

class RemedyAdapter(private val remediesList: List<Remedy>) :
    RecyclerView.Adapter<RemedyAdapter.ViewHolder>() {

    private val filteredList = ArrayList(remediesList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_remedies, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val remedy = filteredList[position]

        // Bind data to the views
        holder.remedyTitle.text = remedy.title
        holder.remedyDocName.text = remedy.doctorName
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val remedyTitle: TextView = itemView.findViewById(R.id.remedyTitle)
        val remedyDocName: TextView = itemView.findViewById(R.id.remedyDocName)
    }

    fun filter(charText: String) {
        val text = charText.toLowerCase(Locale.getDefault())
        filteredList.clear()

        if (text.isEmpty()) {
            filteredList.addAll(remediesList)
        } else {
            for (remedy in remediesList) {
                if (remedy.category.toLowerCase(Locale.getDefault()).contains(text)) {
                    filteredList.add(remedy)
                }
            }
        }
        notifyDataSetChanged()
    }
}