package uz.revolution.adiblar.classic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import uz.revolution.adiblar.databinding.FragmentLiteratureBinding

private const val ARG_PARAM2 = "param2"

class LiteratureFragment : Fragment() {

    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentLiteratureBinding
    lateinit var firebaseFirestore: FirebaseFirestore
    private var adapter: LiteratureAdapter? = null

    lateinit var data: ArrayList<Literature>
    lateinit var firebaseData: ArrayList<Literature>

    lateinit var getDao: LiteratureDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLiteratureBinding.inflate(layoutInflater)
        firebaseFirestore = FirebaseFirestore.getInstance()
        getDao = AppDatabase.get.getDatabase().getDao()

        loadData()
        saveClick()
        itemClick()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadData()
        saveClick()
        itemClick()
    }

    private fun itemClick() {
        adapter?.setOnItemClick(object : LiteratureAdapter.OnItemClick {
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

        adapter?.setOnSaveClick(object : LiteratureAdapter.OnSaveClick {
            override fun onClick(
                literature: Literature,
                position: Int,
                appCompatImageButton: AppCompatImageButton
            ) {
                if (literature.isSaved == true) {
                    data[position] = firebaseData[position]
                    data[position].isSaved = false
                    literature.id?.let { getDao.deleteLiterature(it) }

                    adapter?.notifyItemChanged(position)
                } else {
                    literature.isSaved = true
                    getDao.insertLiterature(literature)
                    data[position] = getDao.getAllLiterature().last()

                    adapter?.notifyItemChanged(position)
                }
            }

        })
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
                    firebaseData.clear()

                    result?.forEach { queryDocumentSnapshot ->
                        val literature = queryDocumentSnapshot.toObject(Literature::class.java)

                        when (position) {
                            0 -> {
                                if (literature.type.equals("Mumtoz adabiyoti", true)) {
                                    data.add(literature)
                                    firebaseData.add(literature)
                                }
                            }
                            1 -> {
                                if (literature.type.equals("O'zbek adabiyoti", true)) {
                                    data.add(literature)
                                    firebaseData.add(literature)
                                }
                            }
                            2 -> {
                                if (literature.type.equals("Jahon adabiyoti", true)) {
                                    data.add(literature)
                                    firebaseData.add(literature)
                                }
                            }
                        }
                    }

                    data = mergeData(data, getDao.getAllLiterature() as ArrayList)
                    adapter?.notifyDataSetChanged()

                }
            }.addOnFailureListener {

            }
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
        return firebaseData
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) =
            LiteratureFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM2, position)
                }
            }
    }
}