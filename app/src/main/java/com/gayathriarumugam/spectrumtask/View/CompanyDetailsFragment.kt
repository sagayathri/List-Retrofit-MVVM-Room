package com.gayathriarumugam.spectrumtask.View

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gayathriarumugam.spectrumtask.Data.Database.CompanyEntity
import com.gayathriarumugam.spectrumtask.Data.Database.MemberEntity
import com.gayathriarumugam.spectrumtask.R
import com.gayathriarumugam.spectrumtask.View.Adapter.CompanyDetailsAdapter
import com.gayathriarumugam.spectrumtask.ViewModel.CompanyViewModel
import java.util.*


class CompanyDetailsFragment(private val company: CompanyEntity) : Fragment() {
    private lateinit var allMembers: List<MemberEntity>
    private lateinit var filteredMembers: ArrayList<MemberEntity>
    private lateinit var companyViewModel: CompanyViewModel
    private lateinit var companyDetailsAdapter: CompanyDetailsAdapter
    private var isNameAsendend = false
    private var isAgeAsendend = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_company_details, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.frag_member_toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        toolbar.title = company.companyName

        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                fragmentManager?.popBackStack("attach", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = view.findViewById<TextView>(R.id.tvDetailsName)
        val about = view.findViewById<TextView>(R.id.tvDetailsAbout)

        name.text = company.companyName
        about?.text = company.about

        companyViewModel = ViewModelProviders.of(requireActivity()).get(CompanyViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.membersRecyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(requireView().context)
        companyDetailsAdapter = CompanyDetailsAdapter(requireContext(), companyViewModel)
        recyclerView.adapter = companyDetailsAdapter

        companyViewModel!!.getAllMembers(company.id)?.observe(viewLifecycleOwner, Observer(function = fun(memberList: List<MemberEntity>?) {
                memberList?.let {
                    if(companyViewModel.isMemberSorted.not()) {
                        allMembers = memberList
                        companyDetailsAdapter.memberList = memberList
                        companyDetailsAdapter.notifyDataSetChanged()
                    }
                }
            }))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.member_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val searchItem = menu.findItem(R.id.action_member_search)
        if(searchItem != null) {
            val searchView: SearchView = searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    filteredMembers = arrayListOf()
                    if (newText!!.isNotEmpty()) {
                        val search = newText.toLowerCase()
                        for (it in allMembers) {
                            val name = it.firstName + it.lastName
                            if (name.toLowerCase(Locale.getDefault())?.contains(search)!!) {
                                filteredMembers.add(it)
                            }
                        }
                    } else {
                        filteredMembers.addAll(allMembers)
                    }
                    companyDetailsAdapter.memberList = filteredMembers
                    companyDetailsAdapter.notifyDataSetChanged()
                    return true
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        companyViewModel.allMembers?.observe(this,
            Observer(function =  fun(members: List<MemberEntity>?) {
                members?.let {
                    companyDetailsAdapter.memberList = members
                    companyDetailsAdapter.notifyDataSetChanged()
                }
            }))
        // Handle item selection
        return when (item.getItemId()) {
            R.id.action_member_search -> {
                true
            }
            R.id.action_sort_age -> {
                if (isAgeAsendend) {
                    companyViewModel.sortMemberByAgeDesend(companyID = company.id)
                    isAgeAsendend = false
                }
                else {
                    companyViewModel.sortMemberByAgeAsend(companyID = company.id)
                    isAgeAsendend = true
                    isNameAsendend = false
                }
                true
            }
            R.id.action_sort_name -> {
                if (isNameAsendend) {
                    isNameAsendend = false
                    companyViewModel.sortMemberByNameDesend(companyID = company.id)

                }
                else {
                    isNameAsendend = true
                    isAgeAsendend = false
                    companyViewModel.sortMemberByNameAsend(companyID = company.id)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        companyViewModel.isMemberSorted = false
    }
}
