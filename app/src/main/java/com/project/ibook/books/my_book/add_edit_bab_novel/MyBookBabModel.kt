package com.project.ibook.books.my_book.add_edit_bab_novel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyBookBabModel(
    var uid: String? = null,
    var title: String? = null,
    var description: String? = null,
    var status: String? = null,
) : Parcelable