package com.emrereyhanlioglu.bby.volunteers.examResultShare


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.emrereyhanlioglu.bby.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_select_class.*
import kotlinx.android.synthetic.main.row_class_list.view.*


class SelectClassFragment : Fragment() {
    private lateinit var destinationPage: String

    private var classNameList: MutableList<String?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            destinationPage = SelectClassFragmentArgs.fromBundle(it).destinationPage
        }

        classNameList = mutableListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_class, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchClassNames()
    }


    private fun fetchClassNames() {
        val ref = FirebaseDatabase.getInstance().getReference("/Classnames")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    val result = it.getValue(String::class.java)
                    result?.let {
                        adapter.add(
                            ClassNameItem(
                                result, destinationPage
                            )
                        )
                    }
                }
                classList?.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    class ClassNameItem(var className: String, var destinationPage: String) : Item<ViewHolder>() {
        override fun getLayout(): Int {
            return R.layout.row_class_list
        }
        override fun bind(viewHolder: ViewHolder, position: Int) {
            val item = viewHolder.itemView
            item.rowClassName.text = className
            item.rowClassName.setOnClickListener {

                val action = SelectClassFragmentDirections.actionSelectStudent()
                action.selectedClass = className
                action.destinationPage = destinationPage
                Navigation.findNavController(it).navigate(action)

            }
        }
    }
}
