package com.emrereyhanlioglu.bby.volunteers.announcementShare


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.emrereyhanlioglu.bby.R
import kotlinx.android.synthetic.main.fragment_announcement_share.*


class AnnouncementShareFragment : Fragment() {
    private var fullname: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            fullname = AnnouncementShareFragmentArgs.fromBundle(it).fullname
        }

   /*     // This callback will only be called when fragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val action = AnnouncementShareFragmentDirections.actionHomepageVolFromAnnouncement()
            Navigation.findNavController(view!!).navigate(action)
        }
        callback.isEnabled = true  */
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_announcement_share, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        announcementShareWriter.text = fullname
    }

}
