package com.example.edutrash.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.edutrash.R
import com.example.edutrash.view.activity.DetailActivity

data class CategoryItem(val imageResId: Int, val title: String, val description: String, val detail: String)

class CategoryAdapter(private val context: Context, private val categories: List<CategoryItem>) : BaseAdapter() {

    override fun getCount(): Int {
        return categories.size
    }

    override fun getItem(position: Int): Any {
        return categories[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)

        val category = categories[position]

        val imgCategory: ImageView = view.findViewById(R.id.imgCategory)
        val tvCategoryTitle: TextView = view.findViewById(R.id.tvCategoryTitle)
        val tvCategoryDescription: TextView = view.findViewById(R.id.tvCategoryDescription)
        val btnMoreDetails: Button = view.findViewById(R.id.btnMoreDetails)

        imgCategory.setImageResource(category.imageResId)
        tvCategoryTitle.text = category.title
        tvCategoryDescription.text = category.description

        btnMoreDetails.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("DETAIL", category.detail)
            context.startActivity(intent)
        }

        return view
    }
}
