package com.example.u2s8_elias_fidel

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.example.u2s8_elias_fidel.model.FootballTeam

class MainActivity : AppCompatActivity() {

    private lateinit var etTeamName: EditText
    private lateinit var etCity: EditText
    private lateinit var etStadium: EditText
    private lateinit var etDetails: EditText
    private lateinit var btnAddTeam: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var teamList: MutableList<FootballTeam>
    private lateinit var adapter: FootballTeamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etTeamName = findViewById(R.id.etTeamName)
        etCity = findViewById(R.id.etCity)
        etStadium = findViewById(R.id.etStadium)
        etDetails = findViewById(R.id.etDetails)
        btnAddTeam = findViewById(R.id.btnAddTeam)
        recyclerView = findViewById(R.id.recyclerView)

        database = FirebaseDatabase.getInstance().getReference("teams")
        teamList = mutableListOf()
        adapter = FootballTeamAdapter(this, teamList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnAddTeam.setOnClickListener {
            addTeam()
        }

        fetchTeams()
    }

    private fun addTeam() {
        val name = etTeamName.text.toString().trim()
        val city = etCity.text.toString().trim()
        val stadium = etStadium.text.toString().trim()
        val details = etDetails.text.toString().trim()

        if (name.isNotEmpty() && city.isNotEmpty() && stadium.isNotEmpty() && details.isNotEmpty()) {
            val id = database.push().key ?: return
            val team = FootballTeam(id, name, city, stadium, details)
            database.child(id).setValue(team).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Team added", Toast.LENGTH_SHORT).show()
                    etTeamName.text.clear()
                    etCity.text.clear()
                    etStadium.text.clear()
                    etDetails.text.clear()
                } else {
                    Toast.makeText(this, "Failed to add team", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchTeams() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                teamList.clear()
                for (dataSnapshot in snapshot.children) {
                    val team = dataSnapshot.getValue(FootballTeam::class.java)
                    team?.let { teamList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load teams", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
