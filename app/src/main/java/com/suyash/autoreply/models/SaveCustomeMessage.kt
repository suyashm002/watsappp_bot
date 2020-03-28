package com.suyash.autoreply.models

 import io.realm.RealmObject

open class SaveCustomeMessage( var expectedMessage: String?= null,
                               var replyMessage: String?= null) : RealmObject(){}