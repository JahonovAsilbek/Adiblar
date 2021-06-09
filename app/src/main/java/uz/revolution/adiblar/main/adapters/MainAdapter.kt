package uz.revolution.adiblar.main.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import uz.revolution.adiblar.home.HomeFragment
import uz.revolution.adiblar.saved.SavedFragment
import uz.revolution.adiblar.settings.SettingsFragment

class MainAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        var fragment = Fragment()
        when (position) {
            0 -> fragment = HomeFragment()
            1 -> fragment = SavedFragment()
            2 -> fragment = SettingsFragment()
        }
        return fragment
    }


}