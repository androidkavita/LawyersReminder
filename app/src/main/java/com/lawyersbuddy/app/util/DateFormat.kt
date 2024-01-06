package com.lawyersbuddy.app.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.util.*
import java.util.concurrent.TimeUnit

class DateFormat {
    companion object {






        fun date(date: String?): String? {
            var result = ""
            val sourceFormat = SimpleDateFormat("dd-MM-yyyy")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("dd-MMM-yyyy")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }
         fun dateHearing(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("dd-MMM-yyyy")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun addDealsDate(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun addServiceDealsDate(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("dd-MM-yyyy")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun exobeAllScreenDateFormat(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("MMM dd, yyyy")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun isBlank(s: String?): Boolean {
            return s == null || s.trim { it <= ' ' }.length == 0
        }
        fun getMonth(dates: String?): String? {
            val inputFormat = SimpleDateFormat("MM")
            val outputFormat = SimpleDateFormat("MMM")
            var date: Date? = null
            try {
                date = inputFormat.parse(dates)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val formattedDate = outputFormat.format(date)
            println(formattedDate) // prints 10-04-2018
            return formattedDate
        }
        @SuppressLint("SuspiciousIndentation")
        fun firstdateofMonth(): String {
            var   sdfn = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            var _calendar = Calendar.getInstance(Locale.getDefault())
            _calendar.add(Calendar.MONTH, 0)
            _calendar.set(Calendar.DATE, _calendar.getActualMinimum(Calendar.DAY_OF_MONTH))
            val monthFirstDay = _calendar.getTime()
            var   stringStartDate = sdfn.format(monthFirstDay)
            return stringStartDate
        }
        fun getMonthName(dates: String?): String? {
            val inputFormat = SimpleDateFormat("MM")
            val outputFormat = SimpleDateFormat("MMMM")
            var date: Date? = null
            try {
                date = inputFormat.parse(dates)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val formattedDate = outputFormat.format(date)
            println(formattedDate) // prints 10-04-2018
            return formattedDate
        }

        fun getFullMonthName(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("MMMM dd, yyyy")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun getComplianceFullMonthName(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("MMMM dd, yyyy")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun getCurrentDate():String{
            val sdf = SimpleDateFormat("MMMM dd, yyyy")
            return sdf.format(Date())
        }
        fun getCurrentDateformat():String{
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            return sdf.format(Date())
        }

        fun dealsdate(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("yyyy-MM-dd")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun proxyDateFormat(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("dd-MM-yyyy")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("yyyy-MM-dd")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun lastDayOfMonth(Y: Int, M: Int): Int {
            return LocalDate.of(Y, M, 1).month.length(Year.of(Y).isLeap)
        }

        fun periodFormat(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("dd-MMM-yyyy")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("dd-MMM-yyyy")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun overDue(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("dd-MMM-yyyy")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun NotificationDate(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("dd-MMM-yyyy")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }
        fun settlementDate(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("MMM dd,yyyy | HH:mm aa")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }



        fun dealstime(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("hh:mm:ss aa")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }

        fun dealstimeforedit(date: String?): String? {
            var result = ""

            val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            var parsed: Date? = null // => Date is in UTC now
            parsed = try {
                sourceFormat.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                return ""
            }
            val destFormat = SimpleDateFormat("hh:mm")
            destFormat.timeZone = TimeZone.getDefault()
            result = destFormat.format(parsed)
            return result
        }


        fun getDaysBetweenDates(start: String?, end: String?): Long {

            //String dateStr = "04/09/2019";
            var start = start
            var end = end
            val serverDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
            val endDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
            var dateObj: Date? = null
            var dateend: Date? = null
            try {
                dateObj = serverDate.parse(start)
                dateend = endDate.parse(end)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val postFormater = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
            val newDateStr = postFormater.format(dateObj)
            start = newDateStr
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
            var endStr: String? = ""
            try {
                endStr = postFormater.format(dateend)
            } catch (e: Exception) {
            }
            end = endStr
            val startDate: Date
            val endDate1: Date
            var numberOfDays: Long = 0
            try {
                startDate = dateFormat.parse(start)
                endDate1 = dateFormat.parse(end)
                numberOfDays = getUnitBetweenDates(startDate, endDate1, TimeUnit.DAYS)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return numberOfDays
        }

        private fun getUnitBetweenDates(
            startDate: Date,
            endDate: Date,
            unit: TimeUnit
        ): Long {
//              return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
            return endDate.time - startDate.time
        }

    }

}