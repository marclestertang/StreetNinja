package com.streetninja.corp.android.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.HashMap;

/**
 * Manages scroll of the RecyclerView.
 *
 * @author marctang@google.com (Marc Tang)
 * @version 2015.03
 * @see android.support.v7.widget.RecyclerView.OnScrollListener
 */
public class ScrollManager extends RecyclerView.OnScrollListener {

  private static final int MIN_SCROLL_TO_HIDE = 10;
  private boolean hidden;
  private int accummulatedDy;
  private int totalDy;
  private int initialOffset;
  private HashMap<View, Direction> viewsToHide = new HashMap<>();

  public ScrollManager() {
  }

  public void attach(RecyclerView recyclerView) {
    recyclerView.setOnScrollListener(this);
  }

  public void addView(View view, Direction direction) {
    viewsToHide.put(view, direction);
  }

  public void setInitialOffset(int initialOffset) {
    this.initialOffset = initialOffset;
  }

  @Override
  public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    totalDy += dy;

    if (totalDy < initialOffset) {
      return;
    }

    if (dy > 0) {
      accummulatedDy = accummulatedDy > 0 ? accummulatedDy + dy : dy;
      if (accummulatedDy > MIN_SCROLL_TO_HIDE) {
        hideViews();
      }
    } else if (dy < 0) {
      accummulatedDy = accummulatedDy < 0 ? accummulatedDy + dy : dy;
      if (accummulatedDy < -MIN_SCROLL_TO_HIDE) {
        showViews();
      }
    }
  }

  public void hideViews() {
    if (!hidden) {
      hidden = true;
      for (View view : viewsToHide.keySet()) {
        hideView(view, viewsToHide.get(view));
      }
    }
  }

  private void showViews() {
    if (hidden) {
      hidden = false;
      for (View view : viewsToHide.keySet()) {
        showView(view);
      }
    }
  }

  private void hideView(View view, Direction direction) {
    int height = calculateTranslation(view);
    int translateY = direction == Direction.UP ? -height : height;
    runTranslateAnimation(view, translateY, new AccelerateInterpolator(3));
  }

  /**
   * Takes height and margin.
   *
   * @param view View to translate
   * @return translation in pixels
   */
  private int calculateTranslation(View view) {
    int height = view.getHeight();

    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
    int margins = params.topMargin + params.bottomMargin;

    return height + margins;
  }

  private void showView(View view) {
    runTranslateAnimation(view, 0, new DecelerateInterpolator(3));
  }

  private void runTranslateAnimation(View view, int translateY, Interpolator interpolator) {
    Animator slideInAnimation = ObjectAnimator.ofFloat(view, "translationY", translateY);
    slideInAnimation.setDuration(
        view.getContext().getResources().getInteger(android.R.integer.config_mediumAnimTime));
    slideInAnimation.setInterpolator(interpolator);
    slideInAnimation.start();
  }

  public static enum Direction {UP, DOWN}
}
