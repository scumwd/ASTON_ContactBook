package com.example.aston_contactbook.adapters

interface ContactItemClickListener {
    fun onContactItemClick(position: Int)
    fun onCheckboxClick(position: Int, isChecked: Boolean)
}