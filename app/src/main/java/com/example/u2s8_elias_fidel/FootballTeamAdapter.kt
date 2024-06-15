package com.example.u2s8_elias_fidel

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.u2s8_elias_fidel.model.FootballTeam

class FootballTeamAdapter(private val context: Context, private val teamList: List<FootballTeam>) : RecyclerView.Adapter<FootballTeamAdapter.TeamViewHolder>() {

    class TeamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTeamName: TextView = itemView.findViewById(R.id.tvTeamName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_team, parent, false)
        return TeamViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val currentTeam = teamList[position]
        holder.tvTeamName.text = currentTeam.name

        holder.itemView.setOnClickListener {
            showDetailsDialog(currentTeam)
        }
    }

    override fun getItemCount() = teamList.size

    private fun showDetailsDialog(team: FootballTeam) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_team_details, null)

        val tvTeamNameDetails: TextView = view.findViewById(R.id.tvTeamNameDetails)
        val tvCityDetails: TextView = view.findViewById(R.id.tvCityDetails)
        val tvStadiumDetails: TextView = view.findViewById(R.id.tvStadiumDetails)
        val tvDetails: TextView = view.findViewById(R.id.tvDetails)

        tvTeamNameDetails.text = team.name
        tvCityDetails.text = team.city
        tvStadiumDetails.text = team.stadium
        tvDetails.text = team.details

        builder.setView(view)
        builder.setPositiveButton("Close") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}
