package uz.revolution.adiblar.settings.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import uz.revolution.adiblar.R
import uz.revolution.adiblar.databinding.FragmentInfoDialogBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class InfoDialogFragment : DialogFragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentInfoDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoDialogBinding.inflate(layoutInflater)



        return binding.root
    }

    override fun getTheme() = R.style.RoundedCornersDialog

}