package uz.revolution.adiblar.item

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.athkalia.emphasis.EmphasisTextView
import com.squareup.picasso.Picasso
import uz.revolution.adiblar.R
import uz.revolution.adiblar.database.AppDatabase
import uz.revolution.adiblar.database.daos.LiteratureDao
import uz.revolution.adiblar.database.entities.Literature
import uz.revolution.adiblar.databinding.FragmentItemBinding


private const val ARG_PARAM1 = "literature"

class ItemFragment : Fragment() {

    lateinit var literature: Literature
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            literature = it.getSerializable(ARG_PARAM1) as Literature
        }
        setHasOptionsMenu(true)

    }

    lateinit var binding: FragmentItemBinding
    lateinit var getDao: LiteratureDao
    private var emphasisTextView: EmphasisTextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemBinding.inflate(layoutInflater)
        getDao = AppDatabase.get.getDatabase().getDao()

        emphasisTextView = EmphasisTextView(binding.root.context)



        setToolbar()
        loadDataToView()


        saveClick()

        searchText()


        return binding.root
    }

    private fun searchText() {
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
//                filter(s.toString())
                binding.text.setTextToHighlight(s.toString())
                binding.text.setTextHighlightColor(R.color.main_green)
                binding.text.highlight()
            }
        })
    }

    private fun saveClick() {

        binding.save.setOnClickListener {
            if (literature.isSaved == true) {
                literature.isSaved = false
                literature.id?.let { it1 -> getDao.deleteLiterature(it1) }

                binding.save.setBackgroundResource(R.drawable.search_btn_back)
                binding.save.setImageResource(R.drawable.ic_item_not_saved)
            } else {
                literature.isSaved = true
                getDao.insertLiterature(literature)
                literature = getDao.getLiteratureByName(literature.name!!)

                binding.save.setBackgroundResource(R.drawable.search_btn_back)
                binding.save.setImageResource(R.drawable.ic_item_saved)
            }
        }
    }

    private fun loadDataToView() {
        binding.text.text = literature.text
        Picasso.get().load(literature.image).into(binding.image)
        if (literature.isSaved == true) {
            binding.save.setBackgroundResource(R.drawable.search_btn_back)
            binding.save.setImageResource(R.drawable.ic_item_saved)
        }


    }

    private fun setToolbar() {
        binding.navigation.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.name.text = literature.name
        binding.years.text = literature.years

        binding.search.setOnClickListener {
            binding.navigation.visibility = View.INVISIBLE
            binding.save.visibility = View.INVISIBLE
            binding.search.visibility = View.INVISIBLE

            binding.searchEt.visibility = View.VISIBLE
            binding.cancelSearch.visibility = View.VISIBLE

            binding.searchEt.requestFocus()
            val imm: InputMethodManager? =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.showSoftInput(binding.searchEt, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.cancelSearch.setOnClickListener {
            if (binding.searchEt.text.toString().trim().isEmpty()) {
                binding.navigation.visibility = View.VISIBLE
                binding.save.visibility = View.VISIBLE
                binding.search.visibility = View.VISIBLE

                binding.searchEt.visibility = View.INVISIBLE
                binding.cancelSearch.visibility = View.INVISIBLE
            } else {
                binding.searchEt.text = null
                binding.text.setTextToHighlight("");
                binding.text.setTextHighlightColor("#FFFFFF")
                binding.text.highlight()
            }

        }
    }

}