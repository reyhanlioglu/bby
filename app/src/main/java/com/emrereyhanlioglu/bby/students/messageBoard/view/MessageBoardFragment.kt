package com.emrereyhanlioglu.bby.students.messageBoard.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.emrereyhanlioglu.bby.R
import com.emrereyhanlioglu.bby.SharedPreferencesHelper
import com.emrereyhanlioglu.bby.students.messageBoard.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_message_board.*
import kotlinx.android.synthetic.main.row_message.view.*


class MessageBoardFragment : Fragment() {

    private var auth: FirebaseAuth? = null
    private var messageList: ArrayList<Message>? = null
    private lateinit var prefHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        messageList = ArrayList()
        auth = FirebaseAuth.getInstance()
        context?.let {
            prefHelper = SharedPreferencesHelper(it)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchMessages()
    }


    private fun fetchMessages() {
        val className = prefHelper.getClassName()
        println("CLASS NAME IS "+className)
        val ref = FirebaseDatabase.getInstance().getReference("Users/Messages/"+className)
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()


                p0.children.forEach {
                    val result = it.getValue(Message::class.java)
                    result?.let {
                        adapter.add(MessageItem(result))
                    }
                }
                recyclerMessageList?.adapter = adapter
                recyclerMessageList?.scrollToPosition(adapter.itemCount-1)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }


    class MessageItem(var message: Message) : Item<ViewHolder>() {
        override fun getLayout(): Int {
            return R.layout.row_message
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            val item = viewHolder.itemView

            item.writerMessage.text = message.writer
            item.textMessage.text = message.message
            item.dateMessage.text = message.date


        }
    }

}
