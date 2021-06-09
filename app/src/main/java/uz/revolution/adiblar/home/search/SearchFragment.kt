package uz.revolution.adiblar.home.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import uz.revolution.adiblar.R
import uz.revolution.adiblar.classic.adapters.LiteratureAdapter
import uz.revolution.adiblar.database.AppDatabase
import uz.revolution.adiblar.database.daos.LiteratureDao
import uz.revolution.adiblar.database.entities.Literature
import uz.revolution.adiblar.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var data: ArrayList<Literature>
    lateinit var firebaseData: ArrayList<Literature>
    lateinit var adapter: LiteratureAdapter
    lateinit var getDao: LiteratureDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        getDao = AppDatabase.get.getDatabase().getDao()

        firebaseFirestore = FirebaseFirestore.getInstance()

        loadData()
        saveClick()
        itemClick()
        searchText()
        cancelClick()

        binding.searchEt.requestFocus()
        val imm: InputMethodManager? =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(binding.searchEt, InputMethodManager.SHOW_IMPLICIT)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadData()
        saveClick()
        itemClick()
        searchText()
        cancelClick()
    }

    private fun loadData() {
        data = ArrayList()
        firebaseData = ArrayList()
        adapter = LiteratureAdapter(binding.root.context, data)
        binding.rv.adapter = adapter

        firebaseFirestore.collection("literature")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result

                    data.clear()
                    result?.forEach { queryDocumentSnapshot ->
                        val literature = queryDocumentSnapshot.toObject(Literature::class.java)
                        data.add(literature)
                        firebaseData.add(literature)
                    }

                    data = mergeData(data, getDao.getAllLiterature() as ArrayList)
                    Log.d("AAAA", "firebaseData: ${firebaseData.size}")
                    Log.d("AAAA", "data: ${data.size}")
                    adapter.notifyDataSetChanged()

                }
            }.addOnFailureListener {

            }
    }

    private fun itemClick() {
        adapter.setOnItemClick(object : LiteratureAdapter.OnItemClick {
            override fun onClick(literature: Literature, position: Int) {
                // on item clicked
                val navOptions = NavOptions.Builder()
                navOptions.setEnterAnim(R.anim.exit_anim)
                navOptions.setExitAnim(R.anim.pop_enter_anim)
                navOptions.setPopEnterAnim(R.anim.enter_anim)
                navOptions.setPopExitAnim(R.anim.pop_exit_anim)

                val bundle = Bundle()
                bundle.putSerializable("literature", literature)
                findNavController().navigate(
                    R.id.itemFragment,
                    bundle,
                    navOptions.build()
                )
            }
        })
    }

    private fun saveClick() {

        adapter.setOnSaveClick(object : LiteratureAdapter.OnSaveClick {
            override fun onClick(
                literature: Literature,
                position: Int,
                appCompatImageButton: AppCompatImageButton
            ) {
                if (literature.isSaved == true) {
                    data[position] = firebaseData[position]
                    data[position].isSaved = false
                    literature.id?.let { getDao.deleteLiterature(it) }
                    adapter.notifyItemChanged(position)
                } else {
                    literature.isSaved = true
                    getDao.insertLiterature(literature)
                    data[position] = getDao.getAllLiterature().last()
                    adapter.notifyItemChanged(position)
                }
            }

        })
    }

    private fun mergeData(
        firebaseData: ArrayList<Literature>,
        roomData: ArrayList<Literature>
    ): ArrayList<Literature> {

        for (i in 0 until firebaseData.size) {
            for (j in 0 until roomData.size) {
                if (firebaseData[i].name.equals(roomData[j].name, true)) {
                    roomData[j].isSaved = true
                    firebaseData[i] = roomData[j]
                }
            }
        }
        Log.d("AAAA", "mergeData: ${firebaseData.size}")
        return firebaseData
    }

    private fun cancelClick() {
        binding.cancelSearch.setOnClickListener {
            if (binding.searchEt.text.isNotEmpty()) {
                binding.searchEt.setText(null)
            } else {
                findNavController().popBackStack()
            }
        }
    }

    private fun searchText() {
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })
    }


    private fun filter(text: String) {
        val filteredList = ArrayList<Literature>()
        for (item in data) {
            if (item.name!!.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }
        adapter.filterList(filteredList)
    }
}