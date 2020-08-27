package com.example.manageapp.view.task.details.dtab2

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.manageapp.BR
import com.example.manageapp.R
import com.example.manageapp.databinding.ItemListDetailTaskOutBinding
import com.example.manageapp.model.task.PurchaseArr

class OutAdapter1(context: Context, resource:Int, objects:List<PurchaseArr>): ArrayAdapter<PurchaseArr>() {
    private var data = objects



    override fun getItem(position: Int): PurchaseArr? {
        return super.getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getContext(): Context {
        return super.getContext()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var binding:ItemListDetailTaskOutBinding
        var cView = convertView
        if (convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_list_detail_task_out,parent,false);
            cView = binding.root
            cView.tag = binding
        } else {
            binding = cView!!.tag as ItemListDetailTaskOutBinding
        }
        binding.setVariable(BR.pa, data[position])
        var content = data[position]
        return cView
    }

}