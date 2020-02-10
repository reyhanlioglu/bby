package com.emrereyhanlioglu.bby.volunteers.homepage


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.emrereyhanlioglu.bby.R
import com.emrereyhanlioglu.bby.SharedPreferencesHelper
import kotlinx.android.synthetic.main.fragment_homepage_vol.*


class HomepageVolFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homepage_vol, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            val prefHelper = SharedPreferencesHelper(it)
            val isAdmin = prefHelper.getAdminStatus()
            if(isAdmin.equals("true")) {
                managementPanel?.visibility = View.VISIBLE
            }
        }

    }



}
