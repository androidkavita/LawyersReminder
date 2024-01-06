package com.lawyersbuddy.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lawyersbuddy.app.R


import com.lawyersbuddy.app.adapter.OffersAdapter
import com.lawyersbuddy.app.baseClasses.BaseFragment
import com.lawyersbuddy.app.databinding.FragmentTabOffersBinding
import com.lawyersbuddy.app.model.OffersData
import com.lawyersbuddy.app.viewmodel.OffersViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TabOffersFragment : BaseFragment() {

    private lateinit var binding: FragmentTabOffersBinding
    private val offersViewModel: OffersViewModel by viewModels()
    private var list: ArrayList<OffersData> = ArrayList()
    lateinit var adapter: OffersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab_offers, container, false)

        offersViewModel.coupon_listApi("Bearer " + userPref.getToken().toString())

        offersViewModel.progressBarStatus.observe(requireActivity()) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        offersViewModel.offersResponse.observe(requireActivity()) {
            if (it?.status == 1) {
                list.clear()
                list.addAll(it.data)

                binding.rvOffers.layoutManager = LinearLayoutManager(requireContext())
                adapter = OffersAdapter(requireContext(), list)
                binding.rvOffers.adapter = adapter


            } else {
                // toa(it?.message!!)
                toast(requireContext(), it?.message!!)
            }
        }

        return binding.root
    }

}