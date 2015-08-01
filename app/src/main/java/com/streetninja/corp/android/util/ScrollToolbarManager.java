package com.streetninja.corp.android.util;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;

import java.util.HashMap;

/**
 * Created by marctang on 3/4/15.
 */
public class ScrollToolbarManager extends RecyclerView.OnScrollListener {

  private static final int MIN_SCROLL_TO_HIDE = 10;
  private boolean hidden;
  private int accummulatedDy;
  private int totalDy;
  private int initialOffset;
  private HashMap<ActionBar, Direction> viewsToHide = new HashMap<>();

  public ScrollToolbarManager() {
  }

  public void attach(RecyclerView recyclerView) {
    recyclerView.setOnScrollListener(this);
  }

  public void addToolbar(ActionBar view, Direction direction) {
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
      for (ActionBar view : viewsToHide.keySet()) {
        hideView(view);
      }
    }
  }

  private void showViews() {
    if (hidden) {
      hidden = false;
      for (ActionBar view : viewsToHide.keySet()) {
        showView(view);
      }
    }
  }

  private void hideView(ActionBar view) {
    view.hide();
  }

  private void showView(ActionBar view) {
    view.show();
  }


  public static enum Direction {UP, DOWN}


}
