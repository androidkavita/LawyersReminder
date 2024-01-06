package com.lawyersbuddy.app.util

import android.view.View

interface OnItemClickListener<T> {

    fun onItemClick(view: View, `object`: T)
}