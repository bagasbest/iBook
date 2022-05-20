package com.project.ibook.books.other.terlaris

import android.os.Parcelable
import com.project.ibook.books.my_book.add_edit_bab_novel.MyBookBabModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class NovelModel1(
    var title: String? = null,
    var titleTemp: String? = null,
    var homepageCategory: String? = null,
    var uid: String? = null,
    var synopsis: String? = null,
    var genre: ArrayList<String>? = null,
    var writerName: String? = null,
    var writerUid: String? = null,
    var image: String? = null,
    var viewTime: Long? = 0L,
    var coins: Long? = 0L,
    var status: String? = null,
    var babList: ArrayList<MyBookBabModel>? = null,
) : Parcelable