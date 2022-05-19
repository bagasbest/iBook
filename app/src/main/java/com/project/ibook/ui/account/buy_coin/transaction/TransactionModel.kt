package com.project.ibook.ui.account.buy_coin.transaction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionModel(
    var uid: String? = null,
    var userId: String? = null,
    var username: String? = null,
    var date: String? = null,
    var status: String? = null,
    var coin: Long? = 0L,
    var price: String? = null,
    var paymentProof: String? = null,
    var paymentMethod: String? = null,
) : Parcelable