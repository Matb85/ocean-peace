package com.redinn.oceanpeace

import android.content.Context
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.Calendar

object FunctionBase {
    //region JSONArrayHasElement
    /**
     * Checking if [JSONArray][JSONArray.JSONArray] contains certain **int**
     *
     * @param array   [JSONArray][JSONArray.JSONArray] which you want to be searched through
     * @param element **int** you're looking for
     * @return returns [Integer][Integer.Integer] if array has element else **null**
     * @throws JSONException throws [JSONException]
     * @author Raccoon
     * @since 09.09.2022
     */
    @Throws(JSONException::class)
    fun JSONArrayOptElement(array: JSONArray, element: Int): Int? {
        for (i in 0 until array.length()) {
            if (array.getInt(i) == element) return array.getInt(i)
        }
        return null
    }

    /**
     * Checking if [JSONArray][JSONArray.JSONArray] contains certain [String][String.String]
     *
     * @param array   [JSONArray][JSONArray.JSONArray] which you want to be searched through
     * @param element [String][String.String] you're looking for
     * @return returns [String][String.String] if array has element else **null**
     * @throws JSONException throws [JSONException]
     * @author Raccoon
     * @since 09.09.2022
     */
    @Throws(JSONException::class)
    fun JSONArrayOptElement(array: JSONArray, element: String): String? {
        for (i in 0 until array.length()) {
            if (array.getString(i) == element) return array.getString(i)
        }
        return null
    }

    /**
     * Checking if [JSONArray][JSONArray.JSONArray] contains certain **long**
     *
     * @param array   [JSONArray][JSONArray.JSONArray] which you want to be searched through
     * @param element **long** you're looking for
     * @return return [Long][Long.Long] if *array* has element else **null**
     * @throws JSONException throws [JSONException]
     * @author Raccoon
     * @since 09.09.2022
     */
    @Throws(JSONException::class)
    fun JSONArrayOptElement(array: JSONArray, element: Long): Long? {
        for (i in 0 until array.length()) {
            if (array.getLong(i) == element) return array.getLong(i)
        }
        return null
    }

    /**
     * Checks if the *array* contains JSONObject with a [String][String.String] in the specified *field*
     *
     * @param array     [JSONArray][JSONArray.JSONArray] which you want to be searched through
     * @param fieldName Name of the field of [JSONObject][JSONObject.JSONObject] that you want to search in
     * @param element   [String][String.String] you looking for
     * @return returns [JSONObject][JSONObject.JSONObject] if array contains element in specified field else returns *null*
     * @throws JSONException throws [JSONException]
     * @author Raccoon
     * @since 09.09.2022
     */
    @Throws(JSONException::class)
    fun JSONArrayOptElement(array: JSONArray, fieldName: String, element: String): JSONObject? {
        for (i in 0 until array.length()) {
            if (array.getJSONObject(i)
                    .getString(fieldName) == element
            ) return array.getJSONObject(i)
        }
        return null
    }
    //endregion
    // region JSONArrayGetIndex
    /**
     * Get the index of the **element** int the **array**
     *
     * @param array   the searched [JSONArray][JSONArray.JSONArray]
     * @param element wanted [String][String.String]
     * @return *int* with index of the element
     *
     * <br></br>
     * **NOTE: **  if the function returned **-1**, it means that the array doesn't contain specified element
     * @author Raccon
     * @since 09.09.2022
     */
    fun JSONArrayGetIndexOf(array: JSONArray, element: String): Int {
        try {
            for (i in 0 until array.length()) {
                if (array.getString(i) == element) return i
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return -1
    }

    /**
     * Get the index of the **element** int the **array**
     *
     * @param array   the searched [JSONArray][JSONArray.JSONArray]
     * @param element wanted *int*
     * @return *int* with index of the element
     *
     * <br></br>
     * **NOTE: **  if the function returned **-1**, it means that the array doesn't contain specified element
     * @author Raccon
     * @since 09.09.2022
     */
    fun JSONArrayGetIndexOf(array: JSONArray, element: Int): Int {
        try {
            for (i in 0 until array.length()) {
                if (array.getInt(i) == element) return i
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return -1
    }

    /**
     * Get the index of the **element** int the **array**
     *
     * @param array   the searched [JSONArray][JSONArray.JSONArray]
     * @param element wanted *long*
     * @return *int* with index of the element
     *
     * <br></br>
     * **NOTE: **  if the function returned **-1**, it means that the array doesn't contain specified element
     * @author Raccon
     * @since 09.09.2022
     */
    fun JSONArrayGetIndexOf(array: JSONArray, element: Long): Int {
        try {
            for (i in 0 until array.length()) {
                if (array.getLong(i) == element) return i
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return -1
    }

    /**
     * Get the index of the [JSONObject][JSONObject.JSONObject] with the **element** at the specified **fieldName**
     *
     * @param array     the searched [JSONArray][JSONArray.JSONArray]
     * @param fieldName [String][String.String] containing name of the field which may contain **element**
     * @param element   wanted [String][String.String]
     * @return *int* with index of the element
     *
     * <br></br>
     * **NOTE: **  if the function returned **-1**, it means that the array doesn't contain specified element
     * @author Raccon
     * @since 09.09.2022
     */
    fun JSONArrayGetIndexOf(array: JSONArray, fieldName: String, element: String): Int {
        try {
            for (i in 0 until array.length()) {
                if (array.getJSONObject(i).getString(fieldName) == element) return i
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return -1
    }

    //endregion
    //region DayOfTheWeek
    val dayOfWeekStringShort: String?
        /**
         * Returns shorter version of day of the week *("Mon", etc.)*
         *
         * @return [String][String.String] with day of the week
         * @author Raccoon
         * @since 09.09.2022
         */
        get() {
            val dayOfWeek = Calendar.getInstance()[Calendar.DAY_OF_WEEK]
            var day: String? = null
            when (dayOfWeek) {
                Calendar.MONDAY -> day = "Mon"
                Calendar.TUESDAY -> day = "Tue"
                Calendar.WEDNESDAY -> day = "Wed"
                Calendar.THURSDAY -> day = "Thu"
                Calendar.FRIDAY -> day = "Fri"
                Calendar.SATURDAY -> day = "Sat"
                Calendar.SUNDAY -> day = "Sun"
            }
            return day
        }
    //endregion
    //region Files Directory
    /**
     * Function providing [String][String.String] containing absolute path to context's files folder
     *
     * @param context application [Context][Context.Context]
     * @return returns context's files directory
     * @author Raccoon
     * @since 09.09.2022
     */
    @JvmStatic
    fun getFilesDir(context: Context): String {
        return context.filesDir.absolutePath
    } //endregion
}
