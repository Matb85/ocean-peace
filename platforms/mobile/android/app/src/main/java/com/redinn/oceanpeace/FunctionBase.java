package com.redinn.oceanpeace;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Objects;

public class FunctionBase {

    //region JSONArrayHasElement

    /**
     * Checking if {@link JSONArray#JSONArray()  JSONArray} contains certain <b>int</b>
     *
     * @param array   {@link JSONArray#JSONArray()  JSONArray} which you want to be searched through
     * @param element <b>int</b> you're looking for
     * @return returns {@link Integer#Integer(int) Integer} if array has element else <b>null</b>
     * @throws JSONException throws {@link JSONException JSONException}
     * @author Raccoon
     * @since 09.09.2022
     */
    @Nullable
    public static Integer JSONArrayOptElement(@NonNull JSONArray array, int element) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            if (array.getInt(i) == element)
                return array.getInt(i);
        }
        return null;
    }

    /**
     * Checking if {@link JSONArray#JSONArray()  JSONArray} contains certain {@link String#String() String}
     *
     * @param array   {@link JSONArray#JSONArray()  JSONArray} which you want to be searched through
     * @param element {@link String#String() String} you're looking for
     * @return returns {@link String#String() String} if array has element else <b>null</b>
     * @throws JSONException throws {@link JSONException JSONException}
     * @author Raccoon
     * @since 09.09.2022
     */
    @Nullable
    public static String JSONArrayOptElement(@NonNull JSONArray array, @NonNull String element) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            if (Objects.equals(array.getString(i), element))
                return array.getString(i);
        }
        return null;
    }

    /**
     * Checking if {@link JSONArray#JSONArray()  JSONArray} contains certain <b>long</b>
     *
     * @param array   {@link JSONArray#JSONArray()  JSONArray} which you want to be searched through
     * @param element <b>long</b> you're looking for
     * @return return {@link Long#Long(long) Long} if <i>array</i> has element else <b>null</b>
     * @throws JSONException throws {@link JSONException JSONException}
     * @author Raccoon
     * @since 09.09.2022
     */
    @Nullable
    public static Long JSONArrayOptElement(@NonNull JSONArray array, long element) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            if (array.getLong(i) == element)
                return array.getLong(i);
        }
        return null;
    }

    /**
     * Checks if the <i>array</i> contains JSONObject with a {@link String#String() String} in the specified <i>field</i>
     *
     * @param array     {@link JSONArray#JSONArray()  JSONArray} which you want to be searched through
     * @param fieldName Name of the field of {@link JSONObject#JSONObject() JSONObject} that you want to search in
     * @param element   {@link String#String() String} you looking for
     * @return returns {@link JSONObject#JSONObject() JSONObject} if array contains element in specified field else returns <i>null</i>
     * @throws JSONException throws {@link JSONException JSONException}
     * @author Raccoon
     * @since 09.09.2022
     */
    @Nullable
    public static JSONObject JSONArrayOptElement(@NonNull JSONArray array, @NonNull String fieldName, @NonNull String element) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            if (array.getJSONObject(i).getString(fieldName).equals(element))
                return array.getJSONObject(i);
        }
        return null;
    }
    //endregion


    // region JSONArrayGetIndex


    /**
     * Get the index of the <b>element</b> int the <b>array</b>
     *
     * @param array   the searched {@link JSONArray#JSONArray() JSONArray}
     * @param element wanted {@link String#String() String}
     * @return <i>int</i> with index of the element
     *
     * <br>
     * <b>NOTE: </b> &emsp; if the function returned <b>-1</b>, it means that the array doesn't contain specified element
     * @author Raccon
     * @since 09.09.2022
     */
    public static int JSONArrayGetIndexOf(@NonNull JSONArray array, @NonNull String element) {
        try {
            for (int i = 0; i < array.length(); i++) {
                if (array.getString(i).equals(element))
                    return i;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Get the index of the <b>element</b> int the <b>array</b>
     *
     * @param array   the searched {@link JSONArray#JSONArray() JSONArray}
     * @param element wanted <i>int</i>
     * @return <i>int</i> with index of the element
     *
     * <br>
     * <b>NOTE: </b> &emsp; if the function returned <b>-1</b>, it means that the array doesn't contain specified element
     * @author Raccon
     * @since 09.09.2022
     */
    public static int JSONArrayGetIndexOf(@NonNull JSONArray array, int element) {
        try {
            for (int i = 0; i < array.length(); i++) {
                if (array.getInt(i) == element)
                    return i;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Get the index of the <b>element</b> int the <b>array</b>
     *
     * @param array   the searched {@link JSONArray#JSONArray() JSONArray}
     * @param element wanted <i>long</i>
     * @return <i>int</i> with index of the element
     *
     * <br>
     * <b>NOTE: </b> &emsp; if the function returned <b>-1</b>, it means that the array doesn't contain specified element
     * @author Raccon
     * @since 09.09.2022
     */
    public static int JSONArrayGetIndexOf(@NonNull JSONArray array, long element) {
        try {
            for (int i = 0; i < array.length(); i++) {
                if (array.getLong(i) == element)
                    return i;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Get the index of the {@link JSONObject#JSONObject() JSONObject} with the <b>element</b> at the specified <b>fieldName</b>
     *
     * @param array     the searched {@link JSONArray#JSONArray() JSONArray}
     * @param fieldName {@link String#String() String} containing name of the field which may contain <b>element</b>
     * @param element   wanted {@link String#String() String}
     * @return <i>int</i> with index of the element
     *
     * <br>
     * <b>NOTE: </b> &emsp; if the function returned <b>-1</b>, it means that the array doesn't contain specified element
     * @author Raccon
     * @since 09.09.2022
     */
    public static int JSONArrayGetIndexOf(@NonNull JSONArray array, @NonNull String fieldName, @NonNull String element) {
        try {
            for (int i = 0; i < array.length(); i++) {
                if (array.getJSONObject(i).getString(fieldName).equals(element))
                    return i;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }


    //endregion


    //region DayOfTheWeek

    /**
     * Returns shorter version of day of the week <i>("Mon", etc.)</i>
     *
     * @return {@link String#String() String} with day of the week
     * @author Raccoon
     * @since 09.09.2022
     */
    public static String getDayOfWeekStringShort() {
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        String day = null;
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                day = "Mon";
                break;
            case Calendar.TUESDAY:
                day = "Tue";
                break;
            case Calendar.WEDNESDAY:
                day = "Wed";
                break;
            case Calendar.THURSDAY:
                day = "Thu";
                break;
            case Calendar.FRIDAY:
                day = "Fri";
                break;
            case Calendar.SATURDAY:
                day = "Sat";
                break;
            case Calendar.SUNDAY:
                day = "Sun";
                break;
        }
        return day;
    }

    //endregion


    //region Files Directory

    /**
     * Function providing {@link String#String() String} containing absolute path to context's files folder
     *
     * @param context application {@link Context#Context() Context}
     * @return returns context's files directory
     * @author Raccoon
     * @since 09.09.2022
     */
    @NonNull
    public static String getFilesDir(@NonNull Context context) {
        return context.getFilesDir().getAbsolutePath();
    }
    //endregion
}
