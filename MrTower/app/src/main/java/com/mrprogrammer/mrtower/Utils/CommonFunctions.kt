package com.mrprogrammer.mrtower.Utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.mrprogrammer.mrtower.Model.NetWorkInfo
import com.mrprogrammer.mrtower.R
import java.net.NetworkInterface
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class CommonFunctions {
    companion object{
        fun FirebaseClearString(aString: String?): String? {
            var aString = aString
            aString = aString?.replace("@", "")
            aString = aString?.replace(".", "")
            aString = aString?.replace("#", "")
            aString = aString?.replace("$", "")
            aString = aString?.replace("[", "")
            aString = aString?.replace("]", "")
            return aString
        }

        fun getDate(): String? {
            val date = Date()
            val formatter = SimpleDateFormat("dd/MM/yy")
            return formatter.format(date)
        }

        fun getTime(): String? {
            val date = Date()
            val formatter = SimpleDateFormat("hh-mm-ss")
            return formatter.format(date)
        }
        fun activityAnimation(context: Context, fromFront: Boolean) {
            if (context is Activity && fromFront) {
                context.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
                return
            }
            (context as Activity).overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }

        fun createUniqueKey(): String? {
            val date = Date()
            val uuiValue = UUID.randomUUID().toString().replace("-", "")
            val formatter = SimpleDateFormat("yyyyMMddHHmmssS")
            return formatter.format(date) + uuiValue
        }

        fun removeFormatOfCurrency(currency: String):String{
            val currencySymbol = Currency.getInstance(Const.currency).symbol
            val numberFormat = DecimalFormat("#,##0.00")

            val unformattedText = currency
                .replace(currencySymbol, "")
                .replace(",", "")
                .replace(".", "")
                .replace(" ", "")

            return numberFormat.parse(unformattedText)?.toString() ?: currency
        }

        fun getYYYYMM(): String? {
            val date = Date()
            val formatter = SimpleDateFormat("yyyyMM")
            return formatter.format(date)
        }



        fun formatCurrency(amount: Double, currencyCode: String): String {
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
            currencyFormat.currency = Currency.getInstance(currencyCode)
            val formatter = currencyFormat as DecimalFormat
            val symbols = formatter.decimalFormatSymbols
            symbols.groupingSeparator = ','
            formatter.decimalFormatSymbols = symbols
            return formatter.format(amount)
        }


        fun isConnected(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                val info = connectivityManager.allNetworkInfo
                if (info != null) {
                    for (networkInfo in info) {
                        if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                            return true
                        }
                    }
                }
            }
            return false
        }

        fun logout(context: Context) {
            (context.getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData()
        }



        fun getNetworkDetails(context: Context): kotlin.collections.List<NetWorkInfo> {
            val netWorkInfo = mutableListOf<NetWorkInfo>()
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                netWorkInfo.add(NetWorkInfo("Type",activeNetworkInfo.type.toString()))
                netWorkInfo.add(NetWorkInfo("Type Name",activeNetworkInfo.typeName.toString()))
                netWorkInfo.add(NetWorkInfo("Extra Info",activeNetworkInfo.extraInfo.toString()))
                netWorkInfo.add(NetWorkInfo("Connected",activeNetworkInfo.isConnected.toString()))
                netWorkInfo.add(NetWorkInfo("Available",activeNetworkInfo.isAvailable.toString()))
                netWorkInfo.add(NetWorkInfo("Roaming",activeNetworkInfo.isRoaming.toString()))
                netWorkInfo.add(NetWorkInfo("State",activeNetworkInfo.state.toString()))
                netWorkInfo.add(NetWorkInfo("Fail Over",activeNetworkInfo.isFailover.toString()))
                netWorkInfo.add(NetWorkInfo("Detailed State",activeNetworkInfo.hashCode().toString()))
            }
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val networkOperatorName = telephonyManager.networkOperatorName
            netWorkInfo.add(NetWorkInfo("Network Operator Name",networkOperatorName,isBottomLine = true))


            val netWorkInfo1 = mutableListOf<NetWorkInfo>()
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (!address.isLinkLocalAddress) {
                        netWorkInfo1.add(NetWorkInfo("Host Address",address.hostAddress))
                        netWorkInfo1.add(NetWorkInfo("Site Local Address",address.isSiteLocalAddress.toString()))
                        netWorkInfo1.add(NetWorkInfo("MC Site Local",address.isMCSiteLocal.toString()))
                        netWorkInfo1.add(NetWorkInfo("MC Link Local",address.isMCLinkLocal.toString()))
                        netWorkInfo1.add(NetWorkInfo("MC Org Local",address.isMCOrgLocal.toString()))
                        netWorkInfo1.add(NetWorkInfo("Any Local Address",address.isAnyLocalAddress.toString()))
                        netWorkInfo1.add(NetWorkInfo("Link Local Address",address.isLinkLocalAddress.toString()))
                        netWorkInfo1.add(NetWorkInfo("MC Global",address.isMCGlobal.toString()))
                        netWorkInfo1.add(NetWorkInfo("Loop Back Address",address.isLoopbackAddress.toString(),true))
                    }
                }
            }
            netWorkInfo.addAll(netWorkInfo1)


            return netWorkInfo
        }
    }
}