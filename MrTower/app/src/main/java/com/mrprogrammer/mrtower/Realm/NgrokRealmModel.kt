package com.mrprogrammer.mrtower.Realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class NgrokRealmModel(
    @PrimaryKey
    var id:String = "" ,
    var url: String = "",
) : RealmObject()