package com.example.edutrash.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.edutrash.R
import com.example.edutrash.view.activity.DetailActivity
import com.example.edutrash.view.adapter.CategoryAdapter
import com.example.edutrash.view.adapter.CategoryItem

class CategoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        val categories = listOf(
            CategoryItem(
                R.drawable.organic, "Organic",
                "Organic waste is biodegradable waste that comes from plants or animals.",
                "Organic waste refers to biodegradable materials that come from plant or animal sources. These materials can decompose naturally through biological processes, making them environmentally friendly when properly managed.\n\nExamples:\n- Kitchen waste: Vegetable peels, fruit scraps, eggshells, and coffee grounds.\n- Garden waste: Grass clippings, fallen leaves, and small branches.\n- Animal waste: Manure and food scraps from pets.\n\nDetailed Explanation:\nOrganic waste is commonly used for composting, a process that turns biodegradable materials into nutrient-rich soil enhancers. Proper segregation and disposal of organic waste can reduce landfill contributions and create sustainable resources for gardening and farming."
            ),
            CategoryItem(
                R.drawable.anorganic, "Anorganic",
                "Anorganic waste is waste that does not come from biological sources and is not biodegradable.",
                "Anorganic waste includes non-biodegradable materials that do not originate from biological sources. These items take a long time to decompose or may not decompose at all, often contributing to environmental pollution if not recycled.\n\nExamples:\n- Plastic products: Bottles, bags, and containers.\n- Metal objects: Cans, wires, and foil.\n- Glass materials: Bottles and jars (e.g., green, brown, white glass).\n\nDetailed Explanation:\nAnorganic waste can often be recycled to minimize its environmental impact. Recycling programs for materials like plastic, metal, and glass help reduce waste in landfills and conserve natural resources. Proper sorting and recycling practices are essential for effective waste management."
            ),
            CategoryItem(
                R.drawable.b3, "B3",
                "B3 waste (hazardous waste) is a type of waste that contains dangerous substances.",
                "B3 waste, or hazardous waste, contains substances that can be harmful to human health or the environment if improperly handled. These materials require special disposal methods to ensure safety and prevent contamination.\n\nExamples:\n- Batteries: Lithium-ion batteries, car batteries.\n- Chemicals: Paint, pesticides, and cleaning agents.\n- Electronic waste: Old mobile phones, computers, and electronic devices.\n\nDetailed Explanation:\nB3 waste must be disposed of according to government regulations to avoid harming the environment or public health. Specialized facilities handle hazardous waste to ensure it is neutralized, recycled, or stored safely. Public awareness and proper waste segregation play a key role in managing hazardous materials."
            )
        )

        val listViewCategories: ListView = view.findViewById(R.id.listViewCategories)
        val adapter = CategoryAdapter(requireContext(), categories)
        listViewCategories.adapter = adapter

        listViewCategories.setOnItemClickListener { _, _, position, _ ->
            val category = categories[position]
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("IMAGE_RES_ID", category.imageResId)
            intent.putExtra("TITLE", category.title)
            intent.putExtra("DETAIL", category.detail)
            startActivity(intent)
        }

        return view
    }
}
