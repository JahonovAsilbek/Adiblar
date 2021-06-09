package uz.revolution.adiblar.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import uz.revolution.adiblar.R
import uz.revolution.adiblar.databinding.FragmentSettingsBinding
import uz.revolution.adiblar.settings.dialogs.InfoDialogFragment


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingsFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)

        shareClick()
        infoClick()
        addClick()

        return binding.root
    }

    private fun addClick() {
        binding.addLayout.setOnClickListener {

            val navOptions = NavOptions.Builder()
            navOptions.setEnterAnim(R.anim.exit_anim)
            navOptions.setExitAnim(R.anim.pop_enter_anim)
            navOptions.setPopEnterAnim(R.anim.enter_anim)
            navOptions.setPopExitAnim(R.anim.pop_exit_anim)

            val bundle = Bundle()
            findNavController().navigate(R.id.addFragment, bundle, navOptions.build())
        }
    }

    private fun infoClick() {
        binding.infoLayout.setOnClickListener {
            val dialog = InfoDialogFragment()
            dialog.show(childFragmentManager, "info")
        }
    }

    private fun shareClick() {
        binding.shareLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val shareBody = "Adiblar hayoti va ijodi\n" +
                    "https://play.google.com/store/apps/details?id=uz.mobiler.adiblar"
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(intent, "Ulashish"))
        }
    }
}