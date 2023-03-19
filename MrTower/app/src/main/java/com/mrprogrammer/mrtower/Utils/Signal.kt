package com.mrprogrammer.mrtower.Utils

import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import androidx.core.content.ContextCompat.getSystemService
import com.mrprogrammer.mrtower.Model.NetWorkInfo


class Signal : PhoneStateListener() {
    var signalLevel: String = ""
    var signalEcie: String = ""
    var signalDbm: String = ""
    override fun onSignalStrengthsChanged(signalStrength: android.telephony.SignalStrength?) {
        super.onSignalStrengthsChanged(signalStrength)

        if (signalStrength != null) {
            signalLevel = signalStrength.level.toString()
            signalDbm = signalStrength.cdmaDbm.toString()
            signalEcie = signalStrength.cdmaEcio.toString()
        }
    }

    fun getSignalStrength(): MutableList<NetWorkInfo> {
        val signalData = mutableListOf<NetWorkInfo>()
        signalData.add(NetWorkInfo("Signal Level",signalLevel))
        signalData.add(NetWorkInfo("Signal CdmaDbm",signalDbm))
        signalData.add(NetWorkInfo("Signal CdmaEcie",signalEcie))
        return signalData
    }
}
