package com.mrprogrammer.mrtower.Utils

class Const {
    companion object {
        const val LocalRealmDb = "RealmDatabase"
        const val SplashScreenTimeOut = 3000L
        const val appTourSP = "appTour"
        const val googleLogo = "https://cdn-icons-png.flaticon.com/512/281/281764.png";
        const val terms =
            "By continuing, I agree to the <span style=\"color: red;\">terms &amp; conditions</span> and <span style=\"color: red;\">privacy policy</span>"
        const val LocalDbName = "UserLocalDb"
        const val LocalDbNameValueName = "name"
        const val LocalDbNameValueEmail = "email"
        const val LocalDbNameValueImage = "imageUrl"


        //Firebase DB ref
        const val SPM = "SPM"

        //Firebase Category
        const val Category = "Category"
        const val Product = "Product"
        const val ShippingEmail = "ShippingEmail"
        const val Orders = "Orders"
        const val OrderShippingDetails = "OrderShippingDetails"
        const val UserOrder = "UserOrder"
        const val UserOrderMapper = "UserOrderMapper"
        const val Accounts = "Accounts"
        const val AppCustomize = "AppCustomize"

        val unitArray = arrayOf<String>("NOS", "KG", "g", "L", "Ml", "m", "Cm", "In")

        var states = arrayOf(
            "Tamil Nadu",
            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chhattisgarh",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Telangana",
            "Tripura",
            "Uttarakhand",
            "Uttar Pradesh",
            "West Bengal"
        )

        //Error
        const val inputRequired = "Input Required for the fields."
        const val chooseImages = "Please choose a image."
        const val Successfully = "Saved Successfully."
        const val deleted = "Deleted Successfully."
        const val selectCategory = "Please selected category."
        const val productCategory = "Please add a category."

        //ResultCode
        const val imageCode = 7077


        //FirebaseImage
        const val IMAGE = "images"

        //IntentCode
        const val INTENT_CODE = "data"

        const val RecycleViewSwipeBackgroundColor = "#0FA37F"
        const val databaseFetchTime = 3000L

        const val currency = "INR"
        const val AmountText = "Total Payable Amount Is"
    }
}