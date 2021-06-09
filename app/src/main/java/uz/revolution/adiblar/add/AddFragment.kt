package uz.revolution.adiblar.add

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import uz.revolution.adiblar.add.adapters.SpinnerAdapter
import uz.revolution.adiblar.database.entities.Literature
import uz.revolution.adiblar.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    lateinit var binding: FragmentAddBinding
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var reference: StorageReference
    lateinit var firebaseFireStore: FirebaseFirestore
    private var imgUrl: String? = null
    private lateinit var typeList: ArrayList<String>
    lateinit var list: ArrayList<Literature>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(layoutInflater)

        loadFireStore()
        loadData()
        loadSpinner()
        imageBtnClick()
        saveClick()

        return binding.root
    }

    private fun checkName(name: String): Boolean {
        var check = true

        for (i in 0 until list.size) {
            if (name.equals(list[i].name, true)) {
                check = false
            }
        }
        return check
    }

    private fun loadData() {
        list = ArrayList()

        firebaseFireStore.collection("literature")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result

                    result?.forEach { queryDocumentSnapshot ->
                        val literature = queryDocumentSnapshot.toObject(Literature::class.java)
                        list.add(literature)
                    }
                }
            }.addOnFailureListener {

            }

    }

    private fun loadSpinner() {
        typeList = ArrayList()
        typeList.add("Turi")
        typeList.add("Mumtoz adabiyoti")
        typeList.add("O'zbek adabiyoti")
        typeList.add("Jahon adabiyoti")

        val adapter = SpinnerAdapter(typeList)
        binding.spinner.adapter = adapter
    }

    private fun saveClick() {
        binding.save.setOnClickListener {
            val name = binding.nameEt.text.toString().trim()
            val yearOfBirth = binding.bornYearEt.text.toString().trim()
            val yearOfDeath = binding.diedYearEt.text.toString().trim()
            val type = typeList[binding.spinner.selectedItemPosition]
            val about = binding.textEt.text.toString().trim()

            if (name.isNotEmpty() && yearOfBirth.isNotEmpty() && yearOfDeath.isNotEmpty() && type.isNotEmpty() && !type.equals(
                    "Turi",
                    true
                ) && about.isNotEmpty() && imgUrl != null
            ) {

                if (checkName(name)) {

                    val literature =
                        Literature(
                            name,
                            "($yearOfBirth-$yearOfDeath)",
                            type,
                            about,
                            imgUrl!!,
                            false
                        )

                    firebaseFireStore.collection("literature").document(name)
                        .set(literature)
                        .addOnSuccessListener {
                            findNavController().popBackStack()
                            Snackbar.make(
                                binding.root,
                                "Muvaffaqiyatli qo'shildi",
                                Snackbar.LENGTH_LONG
                            )
                                .show()
                        }.addOnFailureListener {
                            Snackbar.make(binding.root, "Xatolik!", Snackbar.LENGTH_LONG).show()
                        }
                } else {
                    Snackbar.make(binding.root, "$name oldin kiritilgan!", Snackbar.LENGTH_LONG)
                        .show()
                }


            } else {
                Snackbar.make(binding.root, "Bo'sh o'rinlarni to'ldiring!", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun loadFireStore() {
        firebaseStorage = FirebaseStorage.getInstance()
        reference = firebaseStorage.getReference("images/")
        firebaseFireStore = FirebaseFirestore.getInstance()
    }

    private fun imageBtnClick() {
        binding.imageBtn.setOnClickListener {

            askPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
                //all permissions already granted or just granted

                getImageContent.launch("images/*")


            }.onDeclined { e ->
                if (e.hasDenied()) {

                    androidx.appcompat.app.AlertDialog.Builder(binding.root.context)
                        .setMessage("Qurilma xotirasiga ruxsat zarur")
                        .setPositiveButton("OK") { dialog, which ->
                            e.askAgain();
                        } //ask again
                        .setNegativeButton("Bekor qilish") { dialog, which ->
                            dialog.dismiss();
                        }
                        .show()
                }

                if (e.hasForeverDenied()) {

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }
            }
        }
    }

    private var getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            binding.image.setImageURI(uri)
            val m = System.currentTimeMillis()
            val uploadTask = reference.child(m.toString()).putFile(uri)

            uploadTask.addOnSuccessListener {
                if (it.task.isSuccessful) {
                    val downloadUrl = it.metadata?.reference?.downloadUrl
                    downloadUrl?.addOnSuccessListener { imgUri ->
                        imgUrl = imgUri.toString()
                    }
                }
            }.addOnFailureListener {

            }
        }

}