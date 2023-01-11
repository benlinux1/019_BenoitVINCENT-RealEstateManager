package com.benlinux.realestatemanager.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.ui.activities.PropertyDetailsActivity
import com.benlinux.realestatemanager.ui.models.Property
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*


@SuppressLint("NotifyDataSetChanged")
class ListAdapter(properties: MutableList<Property?>, context: Context) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var mProperties: List<Property?>
    private var localContext: Context

    /**
     * Instantiates a new ListAdapter.
     */
    init {
        mProperties = properties
        localContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_property_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // bind property according to position in the list
        mProperties[position]?.let { holder.bind(it) }

        // Launch Property Details according to its Id
        holder.itemView.setOnClickListener { propertyItem ->
            val propertyDetailsActivityIntent = Intent(
                propertyItem.context,
                PropertyDetailsActivity::class.java
            )
            propertyDetailsActivityIntent.putExtra("PROPERTY_ID", holder.id.text)
            propertyDetailsActivityIntent.putExtra("PROPERTY_CREATOR_ID", holder.realtorId.text)
            propertyItem.context.startActivity(propertyDetailsActivityIntent)
        }
    }

    fun updateProperties(properties: List<Property?>) {
        mProperties = properties
        notifyDataSetChanged()
    }


    fun getItem(i: Int): Property? {
        return mProperties[i]
    }

    override fun getItemCount(): Int {
        return mProperties.size
    }

    /**
     *
     * ViewHolder for properties items in the properties list
     * @author BenLinux1
     */
    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        /**
         * The square picture of the property
         */
        private val picture: ImageView

        /**
         * The TextView displaying the id of the property (invisible)
         */
        val id: TextView

        /**
         * The TextView displaying the id of the property's creator (invisible)
         */
        val realtorId: TextView


        /**
         * The TextView displaying the title of the property
         */
        private val title: TextView

        /**
         * The TextView displaying the area in which the property is
         */
        private val area: TextView

        /**
         * The TextView displaying the price of the property
         */
        private val price: TextView


        /**
         * Instantiates a new Property ViewHolder.
         */
        init {
            picture = itemView.findViewById(R.id.item_property_picture)
            id = itemView.findViewById(R.id.item_property_id)
            realtorId = itemView.findViewById(R.id.item_property_realtor_id)
            title = itemView.findViewById(R.id.item_property_title)
            area = itemView.findViewById(R.id.item_property_area)
            price = itemView.findViewById(R.id.item_property_price)

        }

        /**
         * Binds data to the item view.
         * @param property the property to bind in the item view
         */
        fun bind(property: Property) {

            // Set title
            title.text = property.name

            // Set property id
            id.text = property.id.toString()

            // Set property's creator id
            realtorId.text = property.realtor.id

            // Set property picture

            if (property.pictures.isNotEmpty())
                Glide.with(localContext)
                    .load(property.pictures[0]?.url)
                    .apply(RequestOptions.centerCropTransform())
                    .into(picture)
            else
                Glide.with(localContext)
                    .load(R.mipmap.no_photo)
                    .apply(RequestOptions.centerCropTransform())
                    .into(picture)


            // Set property area
            area.text = property.area

            // Set property price with thousand separator
            val formattedPrice = String.format("%,d", property.price)
            price.text = buildString {
                append("$ ")
                append(formattedPrice)
            }

        }
    }
}