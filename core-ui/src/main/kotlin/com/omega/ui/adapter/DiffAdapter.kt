package com.omega.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class DiffAdapter<T,VH: RecyclerView.ViewHolder> constructor(private val callBack:DiffUtil.ItemCallback<T>):ListAdapter<T,VH>(callBack)
