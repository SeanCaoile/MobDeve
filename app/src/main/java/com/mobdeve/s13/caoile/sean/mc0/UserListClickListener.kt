package com.mobdeve.s13.caoile.sean.mc0

import android.view.View

interface UserListClickListener {

    fun onUserListItemClick(view: View, user: UserModel, position: Int)
}