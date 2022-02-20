package edu.uoc.android.PEC3App

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.uoc.android.rest.models.Element

/**
 * Reycler view adapter for Quizzes
 */
class AdapterQuizzes(private val myDataset: List<Question>) :
    RecyclerView.Adapter<AdapterQuizzes.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.title)
        val image: ImageView = itemView.findViewById(R.id.image)
        val choice1: TextView = itemView.findViewById(R.id.choice1)
        val choice2: TextView = itemView.findViewById(R.id.choice2)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): AdapterQuizzes.MyViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.text = myDataset[position].title
        Picasso.get().load(myDataset[position].imagen).into(holder.image)
        holder.choice1.text = myDataset[position].choice1
        holder.choice2.text = myDataset[position].choice2
        //Sets in bold the answer
        if(myDataset[position].rightChoice.toInt() == 0)
        {
            holder.choice1.setTypeface(Typeface.DEFAULT_BOLD);
        }
        else{
            holder.choice2.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}