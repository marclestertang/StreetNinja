package com.streetninja.corp.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * Created by marctang on 3/20/15.
 */
public class ObservableScrollView extends ScrollView {

  private ArrayList<Callbacks> mCallbacks = new ArrayList<Callbacks>();

  public ObservableScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    super.onScrollChanged(l, t, oldl, oldt);
    for (Callbacks c : mCallbacks) {
      c.onScrollChanged(l, t);
    }
  }

  @Override
  public int computeVerticalScrollRange() {
    return super.computeVerticalScrollRange();
  }

  public void addCallbacks(Callbacks listener) {
    if (!mCallbacks.contains(listener)) {
      mCallbacks.add(listener);
    }
  }

  public static interface Callbacks {

    public void onScrollChanged(int deltaX, int deltaY);
  }
}
