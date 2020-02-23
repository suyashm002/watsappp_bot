package com.autoai.readnotification.models

 import android.support.annotation.IntegerRes
 import io.realm.RealmObject

open class SaveCustomeMessage( var expectedMessage: String?= null,
                               var replyMessage: String?= null) : RealmObject(){}