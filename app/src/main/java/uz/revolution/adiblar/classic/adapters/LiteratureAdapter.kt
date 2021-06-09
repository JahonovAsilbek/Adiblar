package uz.revolution.adiblar.classic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.revolution.adiblar.R
import uz.revolution.adiblar.database.entities.Literature
import uz.revolution.adiblar.databinding.ItemLiteratureBinding

class LiteratureAdapter(
    var context: Context,
    var list: ArrayList<Literature>,
) : RecyclerView.Adapter<LiteratureAdapter.VH>() {


    private var onItemClick: OnItemClick? = null
    private var onSaveClick: OnSaveClick? = null

    inner class VH(var binding: ItemLiteratureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(literature: Literature, position: Int) {

            binding.name.text = literature.name
            binding.years.text = literature.years

            Picasso.get().load(literature.image).into(binding.image)

            if (literature.isSaved == true) {
                binding.save.setBackgroundResource(R.drawable.saved_btn_back)
                binding.save.setImageResource(R.drawable.ic_save_white_btn)
            } else {
                binding.save.setBackgroundResource(R.drawable.unsave_btn_back)
                binding.save.setImageResource(R.drawable.ic_save_black_btn)
            }

            itemView.setOnClickListener {
                if (onItemClick != null) {
                    onItemClick?.onClick(literature, position)
                }
            }

            binding.save.setOnClickListener {
                if (onSaveClick != null) {
                    onSaveClick?.onClick(literature, position, binding.save)
                }
            }

            itemView.animation =
                AnimationUtils.loadAnimation(binding.root.context, R.anim.recycler_anim_2)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemLiteratureBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface OnSaveClick {
        fun onClick(
            literature: Literature,
            position: Int,
            appCompatImageButton: AppCompatImageButton
        )
    }

    interface OnItemClick {
        fun onClick(literature: Literature, position: Int)
    }

    fun setOnItemClick(onItemClick: OnItemClick){
        this.onItemClick = onItemClick
    }

    fun setOnSaveClick(onSaveClick:OnSaveClick){
        this.onSaveClick = onSaveClick
    }

    fun filterList(filteredList: ArrayList<Literature>) {
        list = filteredList
        notifyDataSetChanged()
    }
}