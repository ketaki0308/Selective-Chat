package com.gotenna.selectivechat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created on 12/05/2019 Thu
 *
 * @author Chuliang
 */
@Parcelize
data class ChatGroup(var name:String?=null,var members:List<Member>?= mutableListOf()):Parcelable