package com.emrereyhanlioglu.bby.students.announcements.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.emrereyhanlioglu.bby.R
import com.emrereyhanlioglu.bby.students.announcements.model.Announcement
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_announcements.*
import kotlinx.android.synthetic.main.row_announcement.view.*


class AnnouncementsFragment : Fragment() {

    private var auth: FirebaseAuth? = null
    private var announcementList: ArrayList<Announcement>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        announcementList = ArrayList()
        auth = FirebaseAuth.getInstance()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_announcements, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchAnnouncements()
    }



    private fun fetchAnnouncements() {
        val ref = FirebaseDatabase.getInstance().getReference("Users/Announcements")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()


                p0.children.forEach {
                    val result = it.getValue(Announcement::class.java)
                    result?.let {
                        adapter.add(AnnouncementItem(result))
                    }
                }

                recyclerAnnouncementList?.adapter = adapter
                recyclerAnnouncementList?.scrollToPosition(adapter.itemCount-1)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }



    class AnnouncementItem(var announcement: Announcement) : Item<ViewHolder>() {
        override fun getLayout(): Int {
            return R.layout.row_announcement
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            val item = viewHolder.itemView

            item.textAnnouncement.text = announcement.message
            item.headerAnnouncement.text = announcement.header
            item.infoAnnouncement.text = announcement.info


        }
    }

}
