package com.streetninja.corp.android.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;

/**
 * Created by marctang on 2/23/15.
 */
public class DownloadBackgroundImageTask extends AsyncTask<String, Void, Bitmap> {

  View bmImage;

  public DownloadBackgroundImageTask(View bmImage) {
    this.bmImage = bmImage;
  }

  private static InputStream fetch(String address) throws MalformedURLException, IOException {
    HttpGet httpRequest = new HttpGet(URI.create(address));
    HttpClient httpclient = new DefaultHttpClient();
    HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
    HttpEntity entity = response.getEntity();
    BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
    InputStream instream = bufHttpEntity.getContent();
    return instream;
  }

  protected Bitmap doInBackground(String... urls) {
    String urldisplay = urls[0];
    Bitmap mIcon11 = null;
    try {
      InputStream in = fetch(urldisplay);
      mIcon11 = BitmapFactory.decodeStream(in);
    } catch (Exception e) {
      Log.e("Error", e.getMessage());
      e.printStackTrace();
    }
    return mIcon11;
  }

  protected void onPostExecute(Bitmap result) {
    if (result != null) {
      BitmapDrawable background = new BitmapDrawable(result);
      bmImage.setBackground(background);
    }
  }
}
