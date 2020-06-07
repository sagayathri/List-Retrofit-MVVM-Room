package com.gayathriarumugam.spectrumtask.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gayathriarumugam.spectrumtask.Data.Database.CompanyEntity
import com.gayathriarumugam.spectrumtask.Data.Model.Company
import com.gayathriarumugam.spectrumtask.R
import com.gayathriarumugam.spectrumtask.Utils.PreferencesManager
import com.gayathriarumugam.spectrumtask.View.Adapter.CompanyListAdapter
import com.gayathriarumugam.spectrumtask.ViewModel.CompanyViewModel
import java.util.*


class CompanyFragment : Fragment() {
    private lateinit var companyViewModel: CompanyViewModel
    private lateinit var allCompanies: List<CompanyEntity>
    private lateinit var filterdCompanies: ArrayList<CompanyEntity>
    private lateinit var companyListAdapter: CompanyListAdapter
    private var isNameAsendend = false
    private var isSortedAsc = false
    private var isSortedDesc = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @SuppressLint("NewApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_company, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.frag_company_toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        companyViewModel = ViewModelProviders.of(requireActivity()).get(CompanyViewModel::class.java)

        return view
    }

    override fun onStart() {
        super.onStart()
        val recyclerView = view?.findViewById<RecyclerView>(R.id.company_recyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(requireView().context)
        companyListAdapter = CompanyListAdapter(requireContext(), companyViewModel)

        recyclerView.adapter = companyListAdapter

        //Checks if local store is empty or null
        val preferences = PreferencesManager.preferenceFileExist(requireContext())
        //If the database empty then makes network call
        if (!preferences) {
            companyViewModel.fetchCompany()?.observe(
                this,
                Observer(function = fun(companyList: List<Company>?) {
                    companyList?.let {
                        companyViewModel.updateDatabase(companyList)
                        PreferencesManager.with(requireContext())
                        PreferencesManager.saveURL()
                        loadDatadase(recyclerView)
                    }
                })
            )
        }
        else {
            loadDatadase(recyclerView)
        }
    }

    fun loadDatadase(recyclerView: RecyclerView) {
        companyViewModel.getAllCompanys()
        companyViewModel.allCompanies?.observe(
            this,
            Observer(function = fun(companies: List<CompanyEntity>?) {
                companies?.let {
                    allCompanies = companies
                    if (companyViewModel.sortCompanyList.isNullOrEmpty()) {
                        companyListAdapter.companyList = companies
                    }
                    companyListAdapter.notifyDataSetChanged()
                    setOnItemClick(companies)
                    companyViewModel.allCompanies?.removeObservers(this)
                }
            })
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.company_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        if(searchItem != null) {
            val searchView: SearchView = searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    filterdCompanies = arrayListOf()
                    if (newText!!.isNotEmpty()) {
                        val search = newText.toLowerCase()
                        for (it in allCompanies) {
                            if (it.companyName?.toLowerCase(Locale.getDefault())?.contains(search)!!) {
                                filterdCompanies.add(it)
                            }
                        }
                    } else {
                        filterdCompanies.addAll(allCompanies)
                    }
                    companyListAdapter.companyList = filterdCompanies
                    companyListAdapter.notifyDataSetChanged()
                    return true
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.getItemId()) {
            R.id.action_search -> {
                true
            }
            R.id.action_sort -> {
                if (isNameAsendend) {
                    isNameAsendend = false
                    isSortedAsc = true
                    isSortedDesc = false
                    companyViewModel.sortCompanyByNameDesend()
                } else {
                    isNameAsendend = true
                    isSortedAsc = false
                    isSortedDesc = false
                    companyViewModel.sortCompanyByNameAsend()
                }
                //Observe sorted listed and popultes UI
                companyViewModel.sortedCompanyList.observe(this,
                    Observer(function =  fun(companies: List<CompanyEntity>?) {
                        companies?.let {
                            companyViewModel.getAllCompanys()
                            companyListAdapter.companyList = companies
                            companyListAdapter.notifyDataSetChanged()
                            setOnItemClick(companies)
                        }
                    }))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Call when company clicked
    fun setOnItemClick(companies: List<CompanyEntity>) {
        companyListAdapter.setItemClickListener(object :
            CompanyListAdapter.ItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val newFragment = CompanyDetailsFragment(companies.get(position))
                fragmentManager!!.beginTransaction()
                    .replace(R.id.frag_company, newFragment)
                    .addToBackStack("attach")
                    .commit()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        isNameAsendend = false
        companyViewModel.sortCompanyList = listOf()
    }
}
