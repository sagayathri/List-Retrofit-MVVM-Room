package com.gayathriarumugam.spectrumtask.View.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gayathriarumugam.spectrumtask.Data.Database.CompanyEntity
import com.gayathriarumugam.spectrumtask.R
import com.gayathriarumugam.spectrumtask.Utils.PreferencesManager
import com.gayathriarumugam.spectrumtask.ViewModel.CompanyViewModel
import kotlinx.coroutines.awaitAll
import java.io.FilterReader

class CompanyListAdapter(private val context: Context, private val companyViewModel: CompanyViewModel) :
    RecyclerView.Adapter<CompanyListAdapter.ViewHolder>() {

    private var onItemClickListener: ItemClickListener? = null
    var companyList: List<CompanyEntity> = listOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view =  LayoutInflater.from(viewGroup?.context).inflate(R.layout.company_row_item, viewGroup, false)
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return companyList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Glide.with(viewHolder.itemView.context)
            .load(companyList[position].logo)
            .placeholder(R.drawable.image_placeholder)
            .into(viewHolder.logoImageView as ImageView)
        viewHolder.name?.text = companyList[position].companyName
        viewHolder.website?.text = companyList[position].website

        populateFavButtons(companyList = companyList[position], viewHolder = viewHolder)
        populateFollowButtons(companyList = companyList[position], viewHolder = viewHolder)

        viewHolder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(viewHolder.itemView, position)
        }

        viewHolder.favButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (companyList[position].isFav == 0) {
                    companyList[position].isFav = 1
                }else {
                    companyList[position].isFav = 0
                }
                populateFavButtons(companyList = companyList[position], viewHolder = viewHolder)

                updateDatabase(companyList[position])
            }})

        viewHolder.followButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (companyList[position].isFollowing == 0) {
                    companyList[position].isFollowing = 1
                }else {
                    companyList[position].isFollowing = 0
                }
                populateFollowButtons(companyList = companyList[position], viewHolder = viewHolder)

                updateDatabase(companyList[position])
            }})
    }

    private fun populateFavButtons(companyList: CompanyEntity, viewHolder: CompanyListAdapter.ViewHolder){
        if (companyList.isFav != 0) {
            viewHolder.favButton?.setBackgroundResource(R.drawable.favorite)
        }
        else {
            viewHolder.favButton?.setBackgroundResource(R.drawable.not_favorite)
        }
    }

    private fun updateDatabase(company: CompanyEntity) {
        if (companyViewModel != null){
            companyViewModel.updateCompany(company)
        }
    }

    private fun populateFollowButtons(companyList: CompanyEntity, viewHolder: CompanyListAdapter.ViewHolder){
        if (companyList.isFollowing != 0) {
            viewHolder.followButton?.setBackgroundResource(R.drawable.follow)
        }
        else {
            viewHolder.followButton?.setBackgroundResource(R.drawable.not_following)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logoImageView = itemView.findViewById<ImageView>(R.id.imgCompanyLogo)
        val name = itemView.findViewById<TextView>(R.id.tvName)
        val website = itemView.findViewById<TextView>(R.id.tvWebsite)
        val favButton = itemView.findViewById<Button>(R.id.btnFavorite)
        val followButton = itemView.findViewById<Button>(R.id.btnFollow)
    }

    fun setItemClickListener(clickListener: ItemClickListener) {
        onItemClickListener = clickListener
    }

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}