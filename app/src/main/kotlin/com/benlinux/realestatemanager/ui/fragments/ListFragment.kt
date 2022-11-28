package com.benlinux.realestatemanager.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.ui.models.Realtor
import com.google.android.material.divider.MaterialDividerItemDecoration

class ListFragment: Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mProperties: MutableList<Property>
    private lateinit var adapter: com.benlinux.realestatemanager.ui.adapters.ListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //configRecyclerView()
    }

    /**
     * Init the recyclerView that contains properties list
     */
    private fun configRecyclerView() {

        val property = Property(id="1", name="Ma super villa", type="flat", price= 1200000, area="Quartier de dingue",
            realtor= Realtor("test", "***", "Ben", "Linux", ""), isAvailable = true )

        mProperties.add(property)

        mRecyclerView.layoutManager = LinearLayoutManager(context)
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            divider.dividerInsetEnd = 120
        }
        mRecyclerView.addItemDecoration(divider)
        mRecyclerView.adapter = adapter
        adapter.notifyItemRangeInserted(-1, mProperties.size)
    }
}