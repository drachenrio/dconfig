/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * DConfig - Free Dynamic Configuration Toolkit
 * Copyright (C) 2006, 2007 Jonathan Luo
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 */

package org.moonwave.dconfig.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
    
    public static String DEFAULT_DISPLAY_FORMAT = "MM/dd/yyyy HH:mm:ss";
    public static String STORED_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Converts a <code>java.util.Date</code> to MM/dd/yyyy string.
     *
     * @param date date to convert.
     * @return converted string on success; empty string otherwise.
     */
    public static String toMMDDYYYY(Date date) {
        if(date != null) {
            return new SimpleDateFormat("MM/dd/yyyy").format(date);
        }
        return "";
    }
        
    /**
     * Converts a <code>java.util.Date</code> to yyyy-MM-dd string.
     *
     * @param date date to convert.
     * @return converted string on success; empty string otherwise.
     */
    public static String toYYYYMMDD(Date date) {
        if(date != null) {
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
        return "";
    }
        
    /**
     * Converts a <code>java.util.Date</code> to stored format.
     *
     * @param date date  to convert.
     * @return date string in stored format on success; empty string otherwise.
     */
    public static String toStoredFormat(Date date) {
        if(date != null) {
            return new SimpleDateFormat(STORED_FORMAT).format(date);
        }
        return "";
    }

    /**
     * Converts a date string to stored format.
     *
     * @param dateString date string to convert.
     * @return date string in stored format on success; empty string otherwise.
     */
    public static String toStoredFormat(String dateString) {
        if(dateString != null) {
            Date date = DataConverter.toDate(dateString);
            return toStoredFormat(date);
        }
        return "";
    }

    /**
     * Converts a <code>java.util.Date</code> to display format.
     *
     * @param date date  to convert.
     * @return date string in display format on success; empty string otherwise.
     */
    public static String toDisplayFormat(Date date) {
        if(date != null) {
            return new SimpleDateFormat(DEFAULT_DISPLAY_FORMAT).format(date);
        }
        return "";
    }

    /**
     * Converts a date string to display format.
     *
     * @param dateString date string to convert.
     * @return date string in display format on success; empty string otherwise.
     */
    public static String toDispalyFormat(String dateString) {
        if(dateString != null) {
            Date date = DataConverter.toDate(dateString);
            return toDisplayFormat(date);
        }
        return "";
    }
    
    /**
     * Converts a date string in stored format to a date.
     *
     * @param dateString date string in stored format: yyyy-MM-dd HH:mm:ss.
     * @return Converted date on success; null otherwise.
     */
    public static Date getDate(String dateString) {
        SimpleDateFormat fmt = null;
        if (dateString.indexOf("/") >=0 )
            fmt = new SimpleDateFormat(DateUtil.DEFAULT_DISPLAY_FORMAT);
        else
            fmt = new SimpleDateFormat(DateUtil.STORED_FORMAT);
        Date date = null;
        try {
            date = fmt.parse(dateString);
        } catch (Exception e) {
        }
        return date;
    }
    
    /**
     * Converts a <code>java.util.Date</code> object to <code>Calendar</code>.
     *
     * @param date date object to convert.
     * @return Converted <code>Calendar</code> object.
     */
    public static Calendar toCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static int getCurrentYear(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getCurrentMonth(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    /**
     * Clears hours, minutes, seconds, milliseconds fileds for a <code>Calendar</code>
     * object.
     */
    public static Calendar clearHHMMSSmm(Calendar calendar) {
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        calendar.clear(Calendar.AM_PM);
        calendar.clear(Calendar.HOUR_OF_DAY);
        return calendar;
    }

    /**
     * Clears hours, minutes, seconds, milliseconds fileds for a <code>java.util.Date</code>
     * object.
     */
    public static Date clearHHMMSSmm(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        clearHHMMSSmm(calendar);

        return calendar.getTime();
    }

    /**
     * Converts a date string to <code>java.util.Date</code> in DateFormat.SHORT
     * format.
     *
     * @param dateString date string to convert.
     * @return Converted date on success; null otherwise.
     */
    public static Date toDate(String dateString) {
        return getDateFromString(dateString, DateFormat.SHORT);
    }
    
    /**
     * Converts a date string to <code>java.util.Date</code> for specified format.
     *
     * @param dateString date string in stored format: yyyy-MM-dd HH:mm:ss.
     * @param style the given formatting style. For example,
     * SHORT for "M/d/yy" in the US locale.
     * @return Converted date on success; null otherwise.
     */
    public static Date getDateFromString(String dateString, int style) {
        DateFormat df = DateFormat.getDateInstance(style);
        Date dt = null;
        try {
            dt = df.parse(dateString);
        } catch (ParseException e) {
        }
        return dt;
    }
}
