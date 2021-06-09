package uz.revolution.adiblar.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import uz.revolution.adiblar.R
import uz.revolution.adiblar.databinding.FragmentMainBinding
import uz.revolution.adiblar.main.adapters.MainAdapter

class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    lateinit var adapter: MainAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)

        adapter = MainAdapter(childFragmentManager)
        binding.viewPager2.adapter = adapter

        binding.bottomBar.onItemSelected = { position ->
            when (position) {
                0 -> {
                    binding.viewPager2.currentItem = 0
                }
                1 -> {
                    binding.viewPager2.currentItem = 1
                }
                else -> {
                    binding.viewPager2.currentItem = 2
                }
            }
        }

        binding.viewPager2.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.bottomBar.itemActiveIndex = 0
                    }
                    1 -> {
                        binding.bottomBar.itemActiveIndex = 1
                    }
                    else -> {
                        binding.bottomBar.itemActiveIndex = 2
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        return binding.root
    }

}