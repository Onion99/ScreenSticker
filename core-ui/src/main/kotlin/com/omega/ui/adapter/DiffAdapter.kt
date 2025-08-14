package com.omega.ui.adapter

import com.omega.recyclerview.DiffUtil
import com.omega.recyclerview.ListAdapter
import com.omega.recyclerview.RecyclerView

abstract class DiffAdapter<T,VH:RecyclerView.ViewHolder> constructor(private val callBack:DiffUtil.ItemCallback<T>):ListAdapter<T,VH>(callBack)
