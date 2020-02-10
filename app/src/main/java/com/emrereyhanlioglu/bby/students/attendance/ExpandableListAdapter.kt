package com.emrereyhanlioglu.bby.students.attendance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView
import com.emrereyhanlioglu.bby.R
import com.emrereyhanlioglu.bby.students.attendance.model.AttendanceDetail

class ExpandableListAdapter(var context: Context?, var expandableListView: ExpandableListView, var header: MutableList<String>, var attendanceAll:MutableList<MutableList<AttendanceDetail?>?>?): BaseExpandableListAdapter() {
    override fun getGroup(groupPosition: Int): String {
       return header[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
       return false
    }

    override fun hasStableIds(): Boolean {
       return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        if(view == null) {
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.layout_group, null)
        }
        val title = view?.findViewById<TextView>(R.id.tv_title)
        title?.text = getGroup(groupPosition)




        title?.setOnClickListener{
            if(expandableListView.isGroupExpanded(groupPosition)) {
                expandableListView.collapseGroup(groupPosition)
            } else {
                expandableListView.expandGroup(groupPosition)
            }
        }



        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        var value = attendanceAll?.get(groupPosition)?.size
        value?.let {
            return it
        }
        return 0
    }

    override fun getChild(groupPosition: Int, childPosition: Int): AttendanceDetail {
        var value = attendanceAll?.get(groupPosition)?.get(childPosition)
        value?.let {
            return it
        }
        return AttendanceDetail()
    }

    override fun getGroupId(groupPosition: Int): Long {
         return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var view = convertView
        if(view == null) {
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.layout_child, null)
        }
        val type = view?.findViewById<TextView>(R.id.tv_attendanceType)
        val date = view?.findViewById<TextView>(R.id.tv_attendanceDate)
        val hours = view?.findViewById<TextView>(R.id.tv_attendanceHours)
        //SET VALUES HERE
        type?.text = getChild(groupPosition, childPosition).type
        date?.text = getChild(groupPosition, childPosition).date
        hours?.text = getChild(groupPosition, childPosition).hours.toString() + " saat"

        if(getChildrenCount(groupPosition) == 0)
            view?.visibility = View.GONE

        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return attendanceAll!!.size
    }
}