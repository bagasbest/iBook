package com.project.ibook.books.my_book

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyBookBabModel(
    var uid: String? = null,
    var title: String? = null,
    var description: String? = null,
) : Parcelable