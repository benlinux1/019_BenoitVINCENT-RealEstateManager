package com.benlinux.realestatemanager.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.R.string.no_network
import com.benlinux.realestatemanager.ui.models.Picture
import com.benlinux.realestatemanager.utils.isInternetAvailable
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter

// Slider adapter class that contains list of property pictures to slide
class SliderAdapter(pictures: MutableList<Picture?>, context: Context) :
    SliderViewAdapter<SliderAdapter.SliderViewHolder>() {

    private var picturesList: MutableList<Picture?>
    private var localContext: Context

    // Initialize adapter variables
    init {
        picturesList = pictures
        localContext = context
    }

    // Return slider item
    fun getItem(i: Int): Picture? {
        return picturesList[i]
    }

    // Return pictures list size
    override fun getCount(): Int {
        return picturesList.size
    }


    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup?): SliderViewHolder {
        // Inflate the layout that contains a picture with its description
        val inflate: View = LayoutInflater.from(parent!!.context).inflate(R.layout.item_slider_picture, null)
        return SliderViewHolder(inflate)
    }

    // Set data into view holder
    override fun onBindViewHolder(viewHolder: SliderViewHolder?, position: Int) {

        if (viewHolder != null) {
            // Load picture into dedicated image view
            Glide.with(viewHolder.itemView)
                .load(picturesList[position]!!.url)
                .centerCrop()
                .into(viewHolder.imageView)

            // Set picture's description into dedicated textview
            viewHolder.imageDescription.text = picturesList[position]!!.room

            // Set slider view when no network
            if (!isInternetAvailable(localContext)) {
                Glide.with(viewHolder.itemView)
                    .load(R.mipmap.no_photo)
                    .fitCenter()
                    .into(viewHolder.imageView)

                viewHolder.imageDescription.text = localContext.getString(no_network)
            }
        }
    }

    // Slider view holder
    class SliderViewHolder(itemView: View?) : ViewHolder(itemView) {

        // Initialize views (picture & description)
        var imageView: ImageView = itemView!!.findViewById(R.id.slider_picture_item)
        var imageDescription: TextView = itemView!!.findViewById(R.id.slider_picture_description)
    }
}


