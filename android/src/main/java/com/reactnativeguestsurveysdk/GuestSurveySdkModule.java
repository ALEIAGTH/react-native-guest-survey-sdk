package com.reactnativeguestsurveysdk;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.confirmit.mobilesdk.ConfirmitSDK;
import com.confirmit.mobilesdk.ConfirmitServer;
import com.confirmit.mobilesdk.ProgramDownloadResult;
import com.confirmit.mobilesdk.TriggerSDK;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.it4pme.locatme.Locatme;

@ReactModule(name = GuestSurveySdkModule.NAME)
public class GuestSurveySdkModule extends ReactContextBaseJavaModule {
    public static final String NAME = "GuestSurveySdk";
    private static ReactApplicationContext mContext;
    private static String serverId;
    private static ProgramDownloadResult result;
    private static String appKey = "";
    private static String appName = "";
    private static String vendorClientId = "";
    private static String vendorSecretKey = "";
    private static String userID = "";

    public GuestSurveySdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public static void configure(Callback callback) {
        Locatme.configure(mContext, new Locatme.configueCallback() {
          @Override
          public void onResponse(String s, JSONObject jsonObject) {
                new ConfirmitSDK.Setup((android.app.Application) mContext.getApplicationContext()).configure();
                System.out.println("LocatmeJson -> "+jsonObject);
                try {
                  vendorClientId = jsonObject.getString("vendorClientId");
                  vendorSecretKey = jsonObject.getString("vendorSecretKey");
                  appKey = jsonObject.getString("appKey");
                  appName = jsonObject.getString("appName");
                  SaveSharedPreference.setAppKey(mContext,appKey);
                  SaveSharedPreference.setAppName(mContext,appName);
                } catch (JSONException e) {
                  e.printStackTrace();
                }

                ConfirmitServer.INSTANCE.configureUS(vendorClientId, vendorSecretKey);
                serverId = ConfirmitServer.INSTANCE.getUS().getServerId();
                result = TriggerSDK.INSTANCE.download(serverId, mContext.getString(R.string.program_key));

                if(result.getResult()) {
                  // Your Digital Feedback program is started!
                  System.out.println("Your Digital Feedback program is started!");
                  callback.invoke("ok");
                } else {
                  // Digital Feedback program download failed
                  // Tip: a possible reason is that your program contains scripting errors
                  System.out.println("Digital Feedback program download failed");
                  callback.invoke("failed");
                }
          }
       });
    }

    @ReactMethod
    public static void startCollection(String userId) {
      checkPermission(mContext.getCurrentActivity());
      SaveSharedPreference.setUserID(mContext,userId);
      Locatme.startCollection(mContext,userId);
    }

  public static boolean checkPermission(Activity activity) {
    List<String> permissionsToRequest = new ArrayList<>();

    String[] requiredSystemPermissions = new String[]{
      Manifest.permission.ACCESS_COARSE_LOCATION,
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.ACCESS_WIFI_STATE,
      Manifest.permission.CHANGE_WIFI_STATE,
      Manifest.permission.BLUETOOTH
    };

    for (int i = 0; i <  requiredSystemPermissions.length; i++)
      if (ContextCompat.checkSelfPermission(activity, requiredSystemPermissions[i]) != PackageManager.PERMISSION_GRANTED){
        permissionsToRequest.add(requiredSystemPermissions[i]);
      }

    if (permissionsToRequest.size() > 0){
      ActivityCompat.requestPermissions(activity, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), 1024);
      return false;
    }else {
      return true;
    }
  }

    @ReactMethod
    public static void stopCollection() {
      Locatme.stopCollection(mContext);
      SaveSharedPreference.clearData(mContext);
    }

    @ReactMethod
    public static void getUserSurveys( Callback callback) {

      Locatme.configure(mContext, new Locatme.configueCallback() {
          @Override
          public void onResponse(String s, JSONObject jsonObject) {
            System.out.println("getUserSurveys : "+s);
            userID = SaveSharedPreference.getUserID(mContext);
            String key = SaveSharedPreference.getAppKey(mContext);
            String mAppName = SaveSharedPreference.getAppName(mContext);

            System.out.println("getUserSurveys userID: "+userID);
            System.out.println("getUserSurveys AppName: "+SaveSharedPreference.getAppName(mContext));
              if ( userID!= ""){
                Locatme.getUserSurveys(userID, key, mAppName, new Locatme.Callback() {
                  @Override
                  public void onResponse(String s, JSONArray jsonArray) {
                    System.out.println("getUserSurveys : "+jsonArray);
                    callback.invoke(jsonArray.toString());
                  }
                });
            }
        }
      });
    }

    @ReactMethod
    public static void sendLog() {
      Locatme.sendLog(mContext);
    }

    @ReactMethod
    public static void addPushNotificationToken(String token) {
      Locatme.addPushNotificationToken(mContext, token);
    }

    @ReactMethod
    public static void triggerSurvey(String programKey, String eventName){
      if(result.getResult()) {
        System.out.println("Your Digital Feedback program is started!");

        new Handler(Looper.getMainLooper()).post(new Runnable() {
          @Override
          public void run() {
            Intent intent = new Intent(mContext.getCurrentActivity(), SurveyFragment.class);
            intent.putExtra("programKey",programKey);
            intent.putExtra("eventName",eventName);
            intent.putExtra("email",SaveSharedPreference.getUserID(mContext));
            intent.putExtra("appName",SaveSharedPreference.getAppName(mContext));
            intent.putExtra("appKey",SaveSharedPreference.getAppKey(mContext));
            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.getCurrentActivity().startActivityForResult(intent,123);
          }
        });
      } else {
        System.out.println("Digital Feedback program download failed");
      }
    }
}
