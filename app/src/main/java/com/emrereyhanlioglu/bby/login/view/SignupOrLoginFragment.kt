package com.emrereyhanlioglu.bby.login.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class SignupOrLoginFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.emrereyhanlioglu.bby.R.layout.fragment_signup_or_login, container, false)
    }


    override fun onResume() {
        super.onResume()
        val actionBar = (activity as LoginActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setHomeButtonEnabled(false)
    }



}
