package com.fundaec.ddcharactermanager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.aioria.insta.GsonRequest
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.fundaec.ddcharactermanager.MainActivity
import com.fundaec.ddcharactermanager.R
import com.fundaec.ddcharactermanager.adapters.SkillsAdapter
import com.fundaec.ddcharactermanager.models.CharacterClass
import com.fundaec.ddcharactermanager.models.NewCharacterPostDto
import com.fundaec.ddcharactermanager.models.Skill
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_skills_choices.*

class SkillsChoicesActivity : AppCompatActivity() {

    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skills_choices)
        queue = Volley.newRequestQueue(baseContext)

        fetchClass()
        buttonNextSkillChoices.setOnClickListener {
            val adapter = recyclerSkills.adapter as SkillsAdapter
            if (adapter.selected > adapter.choose) {
                Toast.makeText(baseContext, "You should remove skills", Toast.LENGTH_LONG).show()
            } else {
                saveCharacter()
            }
        }
    }

    private fun saveCharacter() {
        val skillsAdapter = recyclerSkills.adapter as SkillsAdapter
        val character = NewCharacterPostDto(
            "Personagem",
            intent.getStringExtra("class"),
            intent.getStringExtra("race"),
            intent.getParcelableArrayListExtra("attributes"),
            skillsAdapter.skills
        )
        val request = GsonRequest(
            Request.Method.POST,
            "http://192.168.56.1:8080/v1/character",
            String::class.java,
            Gson().toJson(character),
            Response.Listener {
                startActivity(Intent(baseContext, MainActivity::class.java))
                finishAffinity()
            },
            Response.ErrorListener {
                Toast.makeText(baseContext, it.toString(), Toast.LENGTH_LONG).show()
            }
        )
        queue?.add(request)
    }

    private fun fetchClass() {
        val request = GsonRequest(intent.getStringExtra("class"), CharacterClass::class.java,
            Response.Listener { characterClass ->
                createRecyclerSkills(characterClass)
            },
            Response.ErrorListener {
                Toast.makeText(baseContext, it.toString(), Toast.LENGTH_LONG).show()
            }
        )
        queue?.add(request)
    }

    private fun createRecyclerSkills(characterClass: CharacterClass) {
        val choose = characterClass.proficiencyChoices[0].choose
        choices.text = "Choose $choose"

        val skillsChoices = characterClass.proficiencyChoices[0].from
        recyclerSkills.adapter = SkillsAdapter(
            baseContext,
            skillsChoices.map { Skill(it.name) },
            choose
        )
        recyclerSkills.layoutManager = LinearLayoutManager(baseContext, RecyclerView.VERTICAL, false)
    }
}
