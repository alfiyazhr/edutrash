package com.example.edutrash.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.edutrash.R
import com.example.edutrash.view.adapter.CategoryAdapter
import com.example.edutrash.view.adapter.CategoryItem

class CategoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        val categories = listOf(
            CategoryItem(R.drawable.organic, "Organic", "Organic waste is biodegradable waste that comes from plants or animals.", "Detailed explanation about organic waste and examples."),
            CategoryItem(R.drawable.anorganic, "Anorganic", "Anorganic waste is waste that does not come from biological sources and is not biodegradable.", "Detailed explanation about anorganic waste and examples."),
            CategoryItem(R.drawable.b3, "B3", "B3 waste (hazardous waste) is a type of waste that contains dangerous substances.", "Detailed explanation about hazardous waste and examples.")
        )

        val listViewCategories: ListView = view.findViewById(R.id.listViewCategories)
        val adapter = CategoryAdapter(requireContext(), categories)
        listViewCategories.adapter = adapter

        return view
    }
}
