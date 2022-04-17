package com.warzone.team08.VM.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class provides utility for <code>Date</code> and <code>Time</code>.
 *
 * @author Brijesh Lakkad
 * @version 1.0
 */
public class TimeUtil {
    /**
     * Gets the current <code>VM</code> date and time.
     *
     * @return Value of readable time string.
     */
    public static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        return format.format(new Date());
    }
}
