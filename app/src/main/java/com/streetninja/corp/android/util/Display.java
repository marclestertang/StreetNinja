package com.streetninja.corp.android.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;

import com.streetninja.corp.android.peoplemap.R;


/**
 * Display utilities to covert display resources to the required value.
 *
 * @author marctang@google.com (Marc Tang)
 * @version 2015.03
 */
public class Display {

  /**
   * This method converts dp unit to equivalent pixels, depending on device density.
   *
   * @param dp A value in dp (density independent pixels) unit. Which we need to convert into
   * pixels
   * @param context Context to get resources and device specific display metrics
   * @return A float value to represent px equivalent to dp depending on device density
   */
  public static float convertDpToPixel(float dp, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float px = dp * (metrics.densityDpi / 160f);
    return px;
  }

  /**
   * This method converts device specific pixels to density independent pixels.
   *
   * @param px A value in px (pixels) unit. Which we need to convert into db
   * @param context Context to get resources and device specific display metrics
   * @return A float value to represent dp equivalent to px value
   */
  public static float convertPixelsToDp(float px, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float dp = px / (metrics.densityDpi / 160f);
    return dp;
  }

  public static int getToolbarHeight(Context context) {
    final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
            new int[]{R.attr.actionBarSize});
    int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
    styledAttributes.recycle();

    return toolbarHeight;
  }
}
