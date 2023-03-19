package com.mrprogrammer.mrtower.Realm

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Tower(
    @PrimaryKey
    var id: Int = 0,
    var radio: String? = "",
    var mcc: Int? = 0,
    var net: Int? = 0,
    var area: Int? = 0,
    var cell: Int?= 0,
    var lon: Double = 0.0,
    var lat: Double = 0.0
) : RealmObject()


