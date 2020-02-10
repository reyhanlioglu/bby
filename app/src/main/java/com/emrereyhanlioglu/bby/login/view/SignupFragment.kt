package com.emrereyhanlioglu.bby.login.view


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.emrereyhanlioglu.bby.R
import kotlinx.android.synthetic.main.fragment_signup.*


class SignupFragment : Fragment() {
    private var selectedUserType: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        val callback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                NavHostFragment.findNavController(this@SignupFragment).navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUserTypeSpinner()
        setupUserClassSpinner()
    }

    override fun onResume() {
        super.onResume()
        val actionBar = (activity as LoginActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                println("PRESSED")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }




    private fun setupUserTypeSpinner() {
        val userType = arrayListOf("Öğrenci", "Gönüllü")
        val spinnerArrayAdapter = object : ArrayAdapter<String>(
            context!!, R.layout.support_simple_spinner_dropdown_item, userType
        ) {
            override fun getDropDownView(
                position: Int, convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView

                tv.setTextColor(resources.getColor(R.color.colorAccent))

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
                }
                return view
            }
        }
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinnerUserType.adapter = spinnerArrayAdapter
    }



    private fun setupUserClassSpinner() {
        // TODO : EDIT HERE
        val userClasses = arrayListOf("Sınıfı Yok","SAY-1", "SAY-2", "SAY-3", "EA-1", "EA-2")
        val spinnerArrayAdapter = object : ArrayAdapter<String>(
            context!!, R.layout.support_simple_spinner_dropdown_item, userClasses
        ) {
            override fun getDropDownView(
                position: Int, convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView

                tv.setTextColor(resources.getColor(R.color.colorAccent))

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
                }
                return view
            }
        }
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinnerUserClass.adapter = spinnerArrayAdapter
    }


}
