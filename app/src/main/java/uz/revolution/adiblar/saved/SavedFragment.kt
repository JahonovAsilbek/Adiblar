package uz.revolution.adiblar.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import uz.revolution.adiblar.classic.adapters.LiteratureAdapter
import uz.revolution.adiblar.database.AppDatabase
import uz.revolution.adiblar.database.daos.LiteratureDao
import uz.revolution.adiblar.database.entities.Literature
import uz.revolution.adiblar.databinding.FragmentSavedBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SavedFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentSavedBinding
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var data: ArrayList<Literature>
    lateinit var adapter: LiteratureAdapter
    lateinit var getDao: LiteratureDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedBinding.inflate(layoutInflater)
        getDao = AppDatabase.get.getDatabase().getDao()

        firebaseFirestore = FirebaseFirestore.getInstance()
        loadData()
        saveClick()



        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadData()
        saveClick()
    }

    private fun saveClick() {
        adapter.setOnSaveClick(object : LiteratureAdapter.OnSaveClick {
            override fun onClick(
                literature: Literature,
                position: Int,
                appCompatImageButton: AppCompatImageButton
            ) {
                literature.id?.let { getDao.deleteLiterature(it) }
                data.removeAt(position)
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, data.size)
            }

        })
    }

    private fun loadData() {
        data = ArrayList()
        data = getDao.getAllLiterature() as ArrayList
        adapter = LiteratureAdapter(binding.root.context, data)
        binding.rv.adapter = adapter
    }
}