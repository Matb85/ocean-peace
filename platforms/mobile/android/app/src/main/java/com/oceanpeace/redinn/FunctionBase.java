package com.oceanpeace.redinn;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;
import java.util.Objects;

public class FunctionBase {

    //region JSONArrayHasElement

    /**
     * Checking if <i>JSONArray</i>  contains certain <b>int</b>
     *
     * @param array     array which you want to be searched through
     * @param element   element you're looking for
     * @return true if @array has element
     * @throws JSONException
     */
    public static boolean JSONArrayHasElement(JSONArray array, int element) throws JSONException {
        for (int i=0; i < array.length(); i++) {
            if (array.getInt(i) == element)
                return true;
        }
        return false;
    }

    /**
     * Checking if <i>JSONArray</i>  contains certain <b>String</b>
     *
     * @param array     array which you want to be searched through
     * @param element   element you're looking for
     * @return true if @array has element
     * @throws JSONException
     */
    public static boolean JSONArrayHasElement(JSONArray array, String element) throws JSONException {
        for (int i=0; i < array.length(); i++) {
            if (Objects.equals(array.getString(i), element))
                return true;
        }
        return false;
    }

    /**
     * Checking if <i>JSONArray</i> contains certain <b>long</b>
     *
     * @param array     array which you want to be searched through
     * @param element   element you're looking for
     * @return true if @array has element
     * @throws JSONException
     */
    public static boolean JSONArrayHasElement(JSONArray array, long element) throws JSONException {
        for (int i=0; i < array.length(); i++) {
            if (array.getLong(i) == element)
                return true;
        }
        return false;
    }
    //endregion

    //region DayOfTheWeek

    /**
     *
     * Returns shorter version of day of the week <i>("Mon", etc.)</i>
     *
     * @return <b>String</b> with day of the week
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

}
