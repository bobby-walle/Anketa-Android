package com.example.anketa.screen.responses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.anketa.databinding.ResponseItemBinding
import com.example.anketa.model.BaseModel

class ResponsesAdapter(
    models: ArrayList<BaseModel>,
    private val onItemClicked: (BaseModel) -> Unit,
    private val onBtnTelegramClicked: () -> Unit,
    private val onBtnWhatsappClicked: () -> Unit,
) :
    RecyclerView.Adapter<ResponseViewHolder>() {

    var listOfModels = models
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponseViewHolder {
        val binding =
            ResponseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResponseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResponseViewHolder, position: Int) {
        val model = listOfModels[position]
        with(holder.binding) {
            reviewName.text = model.name
            reviewPosition.text = model.position
            rating.text = model.rating.toString()

            imgAvatar.setImageDrawable(
                ResourcesCompat.getDrawable(
                    holder.itemView.resources,
                    model.arrayOfResources[0].first,
                    null
                )
            )

            card.setOnClickListener {
                onItemClicked(model)
            }
            btnTelegram.setOnClickListener {
                onBtnTelegramClicked.invoke()
            }

            btnWhatsapp.setOnClickListener {
                onBtnWhatsappClicked.invoke()
            }
        }
    }

    override fun getItemCount(): Int {
        return listOfModels.size
    }
}

class ResponseViewHolder(val binding: ResponseItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
}
