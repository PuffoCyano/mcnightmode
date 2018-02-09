package com.mcsoft.mcnightmode;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class NightService extends Service
{
   private static final String TAG = "NightService";
   boolean mViewAdded = false;
   private int amountNight = 5; // Default = 5

   WindowManager.LayoutParams mLayoutParams;
   View mOverlayView;
   WindowManager mWindowManager;


   @Override
   public void onCreate()
   {
      Log.d(TAG, "onCreate");
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId)
   {
      if (intent !=null && intent.getExtras()!=null)
      {
         this.amountNight = intent.getExtras().getInt("amount");
      }
      setNightViewOverlay(false);
      return super.onStartCommand(intent, flags, startId);
   }

   private void setNightViewOverlay(boolean landscapeOrientation)
   {
      mWindowManager = ((WindowManager)getSystemService(Context.WINDOW_SERVICE));
      mOverlayView = new View(this);
      mLayoutParams = new WindowManager.LayoutParams();
      WindowManager.LayoutParams params = null;
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
      {
         /*params = new WindowManager.LayoutParams(
                 WindowManager.LayoutParams.MATCH_PARENT,
                 WindowManager.LayoutParams.MATCH_PARENT,
                 WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                 0 | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                 | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                 PixelFormat.TRANSLUCENT);*/
         if (landscapeOrientation)
         {
            // Landscape
            params = new WindowManager.LayoutParams((int)(mWindowManager.getDefaultDisplay().getWidth() * 1.2D), (int)(mWindowManager.getDefaultDisplay().getHeight() * 1.2D) , WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, 536, -3);
         }
         else
         {
            // Portrait
            params = new WindowManager.LayoutParams(-1, (int)(mWindowManager.getDefaultDisplay().getHeight() * 1.2D), WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, 536, -3);
         }
      }
      else
      {
         /*params = new WindowManager.LayoutParams(
                 WindowManager.LayoutParams.MATCH_PARENT,
                 WindowManager.LayoutParams.MATCH_PARENT,
                 WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                 0 | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                         | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                 PixelFormat.TRANSLUCENT);*/
         if (landscapeOrientation)
         {
            // Landscape
            params = new WindowManager.LayoutParams((int)(mWindowManager.getDefaultDisplay().getWidth() * 1.2D), (int)(mWindowManager.getDefaultDisplay().getHeight() * 1.2D) , WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, 536, -3);
         }
         else
         {
            // Portrait
            params = new WindowManager.LayoutParams(-1, (int)(mWindowManager.getDefaultDisplay().getHeight() * 1.2D), WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, 536, -3);
         }
      }

      int color = getResources().getColor(R.color.yellowTrasparent);
      int halfTransparentColor;
      if(this.amountNight<10)
      {
         halfTransparentColor = adjustAlpha(color, Float.parseFloat("0."+this.amountNight+"f"));
      }
      else
      {
         halfTransparentColor = adjustAlpha(color, 1f);
      }
      mOverlayView.setBackgroundColor(halfTransparentColor);
      //mLayoutParams.alpha = 0.2f;
      if (mViewAdded)
      {
         mWindowManager.updateViewLayout(mOverlayView, params);
      }
      else
      {
         mWindowManager.addView(mOverlayView, params);
         mViewAdded = true;
      }
   }

   @ColorInt
   public static int adjustAlpha(@ColorInt int color, float factor)
   {
      int alpha = Math.round(Color.alpha(color) * factor);
      int red = Color.red(color);
      int green = Color.green(color);
      int blue = Color.blue(color);
      return Color.argb(alpha, red, green, blue);
   }

   @Override
   public void onConfigurationChanged(Configuration newConfig)
   {
      Log.d("tag", "config changed");
      super.onConfigurationChanged(newConfig);

      if (mViewAdded)
      {
         mWindowManager.removeView(mOverlayView);
         mViewAdded = false;
      }
      int orientation = newConfig.orientation;
      if (orientation == Configuration.ORIENTATION_PORTRAIT)
         setNightViewOverlay(false);
      else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
         setNightViewOverlay(true);
      else
         Log.w("tag", "other: " + orientation);
   }

   @Override
   public void onDestroy()
   {
      super.onDestroy();

      if (mViewAdded)
      {
         mWindowManager.removeView(mOverlayView);
         mViewAdded = false;
      }
   }

   @Override
   public IBinder onBind(Intent intent)
   {
      return null;
   }
}
