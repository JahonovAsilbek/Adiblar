package uz.revolution.adiblar.home.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import uz.revolution.adiblar.classic.LiteratureFragment

class HomeAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        return LiteratureFragment.newInstance(position)
    }
}