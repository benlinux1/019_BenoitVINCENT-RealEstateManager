package com.benlinux.realestatemanager.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.ui.models.Picture
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@SuppressLint("NotifyDataSetChanged")
class PictureAdapter(pictures: MutableList<Picture?>, context: Context) : RecyclerView.Adapter<PictureAdapter.ViewHolder>() {

    private var mPictures: MutableList<Picture?>
    private var localContext: Context

    /**
     * Instantiates a new PictureAdapter.
     */
    init {
        mPictures = pictures
        localContext = context
    }

    // Layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_property_picture, parent, false)
        return ViewHolder(view)
    }

    // Position for click listener
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // bind property according to position in the list
        mPictures[position]?.let { holder.bind(it) }

        // TODO : Call metrics for tab or smartphone to define onClick action
        // Launch Picture Details according to its Id
        holder.itemView.setOnClickListener {
            // TODO : extend picture view ?
        }
    }

    fun updatePictures(pictures: MutableList<Picture?>) {
        mPictures = pictures
        notifyDataSetChanged()
    }


    fun getItem(i: Int): Picture? {
        return mPictures[i]
    }

    override fun getItemCount(): Int {
        return mPictures.size
    }

    /**
     *
     * ViewHolder for pictures items in the pictures list
     * @author BenLinux1
     */
    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        /**
         * The square picture
         */
        private val imageView: ImageView


        /**
         * The TextView displaying the title of the picture
         */
        private val title: TextView


        /**
         * Instantiates a new Picture ViewHolder.
         */
        init {
            imageView = itemView.findViewById(R.id.property_picture_item)
            title = itemView.findViewById(R.id.property_picture_description)
        }

        /**
         * Binds data to the item view.
         * @param picture the property to bind in the item view
         */
        fun bind(picture: Picture) {

            // Set title
            title.text = picture.room

            // Set property picture
            if (picture.url.isNotEmpty()) {
                Glide.with(localContext)
                    .load(picture.url)
                    .apply(RequestOptions.centerCropTransform())
                    .into(imageView)
            } else {
                Glide.with(localContext)
                    .load(R.mipmap.no_photo)
                    .apply(RequestOptions.centerCropTransform())
                    .into(imageView)
            }
        }
    }
}