package com.gayathriarumugam.spectrumtask.View.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gayathriarumugam.spectrumtask.Data.Database.MemberEntity
import com.gayathriarumugam.spectrumtask.R
import com.gayathriarumugam.spectrumtask.Utils.PreferencesManager
import com.gayathriarumugam.spectrumtask.ViewModel.CompanyViewModel

class CompanyDetailsAdapter(private val context: Context, private val companyViewModel: CompanyViewModel) :
    RecyclerView.Adapter<CompanyDetailsAdapter.ViewHolder>() {

    private var onClickListener: View.OnClickListener? = null
    var memberList: List<MemberEntity> = listOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view =  LayoutInflater.from(viewGroup?.context).inflate(R.layout.member_row_item, viewGroup, false)
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name?.text = "${memberList[position].firstName} ${memberList[position].lastName}"
        viewHolder.age?.text = memberList[position].age.toString()
        viewHolder.email?.text = memberList[position].email
        viewHolder.phone?.text = memberList[position].phone
        populateFavButton(memberList = memberList[position], viewHolder = viewHolder)

        viewHolder.isFav.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if (memberList[position].isFav == 0) {
                    memberList[position].isFav = 1
                }else {
                    memberList[position].isFav = 0
                }
                populateFavButton(memberList = memberList[position], viewHolder = viewHolder)

                updateDatabaseMembers(memberList[position])
            }})
    }

    private fun updateDatabaseMembers(member: MemberEntity) {
        if (companyViewModel != null){
            companyViewModel.updateMember(member)
        }
    }

    private fun populateFavButton(memberList: MemberEntity, viewHolder: ViewHolder){
        if (memberList.isFav != 0) {
            viewHolder.isFav?.setBackgroundResource(R.drawable.favorite)
        }
        else {
            viewHolder.isFav?.setBackgroundResource(R.drawable.not_favorite)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.tvMemberName)
        val age = itemView.findViewById<TextView>(R.id.tvMemberAge)
        val email = itemView.findViewById<TextView>(R.id.tvMemberEmail)
        val phone = itemView.findViewById<TextView>(R.id.tvMemberPhone)
        val isFav = itemView.findViewById<Button>(R.id.btnMemberFavorite)
    }
}