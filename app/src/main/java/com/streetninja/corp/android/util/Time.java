package com.streetninja.corp.android.util;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.Date;

/**
 * Created by marctang on 2/10/15.
 */
public class Time {

  public static String getFormattedTime(Date startDate, Date endDate) {
    String displayMessage = new String();

    DateTime startTime = new DateTime(startDate), endTime = new DateTime(endDate);
    Period p = new Period(startTime, endTime);

    if (p.getYears() > 1) {
      displayMessage = p.getYears() + " yrs ago";
    } else if (p.getYears() == 1) {
      displayMessage = "A yr ago";
    } else if (p.getMonths() > 1) {
      displayMessage = p.getMonths() + " months ago";
    } else if (p.getMonths() == 1) {
      displayMessage = "A month ago";
    } else if (p.getWeeks() > 1) {
      displayMessage = p.getWeeks() + " weeks ago";
    } else if (p.getWeeks() == 1) {
      displayMessage = "A week ago";
    } else if (p.getDays() > 1) {
      displayMessage = p.getDays() + " days ago";
    } else if (p.getDays() == 1) {
      displayMessage = "Yesterday";
    } else if (p.getHours() > 1) {
      displayMessage = p.getHours() + " hrs ago";
    } else {
      displayMessage = "An hour ago";
    }

    return displayMessage;
  }
}
