package com.mcsoft.nightmode;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import com.mcsoft.mcnightmode.NightService;

public class MainActivity extends AppCompatActivity
{
   private Context context = this;
   private boolean isRunningNightMode = false;
   private Button btnNightMode;

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.layout_main_activity);

      if (Build.VERSION.SDK_INT >= 23)
      {
         if (!Settings.canDrawOverlays(this))
         {
            Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(myIntent);
         }
      }

      btnNightMode = (Button) findViewById(R.id.btnNightMode);

      btnNightMode.setOnClickListener(new View.OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            if(!isRunningNightMode)
            {
               startService(new Intent(context, NightService.class).putExtra("amount",5));
               isRunningNightMode = true;
            }
            else
            {
               stopService(new Intent(context,NightService.class));
               isRunningNightMode = false;
            }
         }
      });
   }

   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event)
   {
      if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5 && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
      {
         onBackPressed();
      }
      return super.onKeyDown(keyCode, event);
   }

   public void onBackPressed ()
   {
      moveTaskToBack(true);
   }
   @Override
   protected void onDestroy()
   {
      super.onDestroy();
      stopService(new Intent(context,NightService.class));
      android.os.Process.killProcess(android.os.Process.myPid());
   }
}
