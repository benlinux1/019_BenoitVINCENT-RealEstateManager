package com.benlinux.realestatemanager.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.ui.models.Picture
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


@SuppressLint("NotifyDataSetChanged")
class PictureAdapter(pictures: MutableList<Picture?>, context: Context, userIsRealtor: Boolean) : RecyclerView.Adapter<PictureAdapter.ViewHolder>() {

    private var mPictures: MutableList<Picture?>
    private var localContext: Context
    private var realtorStatus: Boolean

    /**
     * Instantiates a new PictureAdapter.
     */
    init {
        mPictures = pictures
        localContext = context
        realtorStatus = userIsRealtor
    }

    // Layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_property_picture, parent, false)
        return ViewHolder(view)
    }

    // Position for click listener
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // bind picture according to position in the list
        mPictures[position]?.let { holder.bind(it) }

        // TODO : Call metrics for tab or smartphone to define onClick action
        // Launch Picture Details according to its Id
        holder.itemView.setOnClickListener {
            // TODO : extend picture view ?
            zoomOnPicture(it.context, it, holder.imageView.drawable, holder.title.text.toString(), position)
        }

    }

    // Launch popup window with image zoom
    private fun zoomOnPicture(context: Context?, view: View, picture: Drawable, roomName: String, position: Int) {
        // Builder & custom view
        val builder = AlertDialog.Builder(context!!, R.style.CustomAlertDialog)
        val customView: View = LayoutInflater.from(view.rootView.context).inflate(R.layout.custom_zoom_picture, null)
        builder.setView(customView)
        builder.setCancelable(true)
        val dialogWindow = builder.create()

        // Custom view picture
        val imageView: ImageView = customView.findViewById(R.id.zoom_room_picture)
        // Custom view room name
        val room: TextView = customView.findViewById(R.id.zoom_room_name)
        // Custom view icon
        val windowIcon: ImageView = customView.findViewById(R.id.zoom_close_picture)

        // Picture
        Glide.with(context)
            .load(picture)
            .apply(RequestOptions.centerInsideTransform())
            .into(imageView)

        // Room name
        room.text = roomName

        // Popup icon action
        if(realtorStatus) {
            // If realtor is updating or adding, set delete icon with delete feature
            if (context.javaClass.simpleName == "UpdatePropertyActivity" || context.javaClass.simpleName == "AddPropertyActivity") {
                windowIcon.setImageResource(R.drawable.ic_baseline_delete_24)
                windowIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent))
                windowIcon.setOnClickListener {
                    mPictures.removeAt(position)
                    notifyDataSetChanged()
                    dialogWindow.dismiss()
                }
            // if realtor is just looking, let close icon with close option
            } else {
                windowIcon.setOnClickListener {
                    dialogWindow.dismiss()
                }
            }
        // if simple user, let close icon with close option
        } else {
            windowIcon.setOnClickListener {
                dialogWindow.dismiss()
            }
        }

        // Display dialog
        dialogWindow.show()
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
        val imageView: ImageView


        /**
         * The TextView displaying the title of the picture
         */
        val title: TextView


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