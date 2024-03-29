package com.fundaec.ddcharactermanager.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fundaec.ddcharactermanager.R
import com.fundaec.ddcharactermanager.activities.CharacterActivity
import com.fundaec.ddcharactermanager.models.CharacterMainActivityDto
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.character_layout_item.view.*

class CharactersAdapter(var context: Context, var characterActivities: List<CharacterMainActivityDto>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<CharactersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.character_layout_item, parent, false)
        return ViewHolder(v, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(characterActivities[position])
    }

    override fun getItemCount(): Int {
        return characterActivities.size
    }

    class ViewHolder(
        itemView: View,
        var context: Context
    ) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bindItems(character: CharacterMainActivityDto) {
            itemView.setOnClickListener {
                val intent = Intent(context, CharacterActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("characterId", character.id)
                context.startActivity(intent)
            }

            Picasso.get()
                .load(character.image)
                .fit()
                .centerCrop()
                .into(itemView.characterImage)
            itemView.characterName.text = character.characterName
            itemView.characterInfo.text = "${character.race} ${character.characterClass} Level ${character.level}"
        }
    }
}