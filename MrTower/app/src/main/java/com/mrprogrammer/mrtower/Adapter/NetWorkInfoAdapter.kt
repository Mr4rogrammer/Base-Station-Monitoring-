package com.mrprogrammer.mrtower.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.mrprogrammer.mrtower.Model.NetWorkInfo
import com.mrprogrammer.mrtower.R

class NetWorkInfoAdapter(private val items: List<NetWorkInfo>) : BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.list_item, parent, false)

        val title = convertView?.findViewById<TextView>(R.id.title);
        val message = convertView?.findViewById<TextView>(R.id.message);
        val line = convertView?.findViewById<View>(R.id.line);

        title?.text = items[position].title
        message?.text = items[position].message.ifEmpty { "-" }

        if(items[position].isNeedBelowLine) line?.visibility = View.VISIBLE else line?.visibility = View.GONE

        return view
    }
}
