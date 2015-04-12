package com.thrive.westm10.thrive;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Matthew West on 12/04/2015.
 */
public class DateConversion {

    public Date julianToDate(double jd) {

        double z, f, a, b, c, d, e, m, aux;
        Date date = new Date();
        jd += 0.5;
        z = Math.floor(jd);
        f = jd - z;

        if (z >= 2299161.0) {
            a = Math.floor((z - 1867216.25) / 36524.25);
            a = z + 1 + a - Math.floor(a / 4);
        } else {
            a = z;
        }

        b = a + 1524;
        c = Math.floor((b - 122.1) / 365.25);
        d = Math.floor(365.25 * c);
        e = Math.floor((b - d) / 30.6001);
        aux = b - d - Math.floor(30.6001 * e) + f;

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, (int) aux);
        aux = ((aux - calendar.get(Calendar.DAY_OF_MONTH)) * 24);
        calendar.set(Calendar.HOUR_OF_DAY, (int) aux);
        calendar.set(Calendar.MINUTE, (int) ((aux - calendar.get(Calendar.HOUR_OF_DAY)) * 60));

        if (e < 13.5) {
            m = e - 1;
        } else {
            m = e - 13;
        }
        calendar.set(Calendar.MONTH, (int) m - 1);
        if (m > 2.5) {
            calendar.set(Calendar.YEAR, (int) (c - 4716));
        } else {
            calendar.set(Calendar.YEAR, (int) (c - 4715));
        }
        return calendar.getTime();
    }

    public double dateToJulian(Date date) {

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int year;
        int month;
        float day;
        int a;
        int b;
        double d;
        double frac;

        frac = (calendar.get(Calendar.HOUR_OF_DAY) / 0.000024 + calendar.get(Calendar.MINUTE) / 0.001440);

        b = 0;

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;

        DecimalFormat zeroPlaces = new DecimalFormat("0");
        day = calendar.get(Calendar.DAY_OF_MONTH);
        day = Float.parseFloat(zeroPlaces.format(day) + "." + zeroPlaces.format(Math.round(frac)));

        if (month < 3) {
            year--;
            month += 12;
        }
        if (compare(calendar.getTime(), calendar.getGregorianChange()) > 0) {
            a = year / 100;
            b = 2 - a + a / 4;
        }
        d = Math.floor(365.25 * year) + Math.floor(30.6001 * (month + 1)) + day + 1720994.5 + b;

        return (d);
    }

        public int compare(Date d1, Date d2) {

            Calendar c1 = new GregorianCalendar();
            c1.setTime(d1);
            Calendar c2 = new GregorianCalendar();
            c2.setTime(d2);

            if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
                if (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) {
                    return c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH);
                } else {
                    return c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
                }
            } else {
                return c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
            }
        }

}
