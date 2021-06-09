package uz.revolution.adiblar.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_tab.view.*
import uz.revolution.adiblar.R
import uz.revolution.adiblar.databinding.FragmentHome2Binding
import uz.revolution.adiblar.home.adapters.HomeAdapter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentHome2Binding

    lateinit var adapter: HomeAdapter
    lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHome2Binding.inflate(layoutInflater)

        firebaseFirestore = FirebaseFirestore.getInstance()

        adapter = HomeAdapter(childFragmentManager)
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        setTabs()

        searchClick()

        return binding.root
    }

    private fun searchClick() {
        binding.searchBtn.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
    }

    private fun setTabs() {

        for (i in 0 until binding.tabLayout.tabCount) {
            val tabBind =
                LayoutInflater.from(binding.root.context).inflate(R.layout.item_tab, null, false)
            val tab = binding.tabLayout.getTabAt(i)
            tab?.customView = tabBind

            when (i) {
                0 -> tabBind.title_tv.text = "Mumtoz"
                1 -> tabBind.title_tv.text = "O'zbek"
                2 -> tabBind.title_tv.text = "Jahon"
            }

            if (i == 0) {
                tabBind.title_tv.setBackgroundResource(R.drawable.tab_item_back_selected)
                tabBind.title_tv.setTextColor(resources.getColor(R.color.white))
            } else {
                tabBind.title_tv.setBackgroundResource(R.drawable.tab_item_back_unselected)
                tabBind.title_tv.setTextColor(resources.getColor(R.color.tab_unselected_text_color))
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val view = tab?.customView
                view?.title_tv?.setBackgroundResource(R.drawable.tab_item_back_selected)
                view?.title_tv?.setTextColor(resources.getColor(R.color.white))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val view = tab?.customView
                view?.title_tv?.setBackgroundResource(R.drawable.tab_item_back_unselected)
                view?.title_tv?.setTextColor(resources.getColor(R.color.tab_unselected_text_color))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


    }

}