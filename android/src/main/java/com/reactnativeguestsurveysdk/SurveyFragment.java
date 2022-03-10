package com.reactnativeguestsurveysdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;

import com.confirmit.mobilesdk.ConfirmitServer;
import com.confirmit.mobilesdk.ProgramDownloadResult;
import com.confirmit.mobilesdk.TriggerSDK;
import com.confirmit.mobilesdk.trigger.ProgramCallback;
import com.confirmit.mobilesdk.trigger.TriggerInfo;
import com.confirmit.mobilesdk.ui.SurveyFrameConfig;
import com.confirmit.mobilesdk.web.SurveyWebViewFragment;
import com.confirmit.mobilesdk.web.SurveyWebViewFragmentCallback;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import fr.it4pme.locatme.Locatme;

public class SurveyFragment extends FragmentActivity implements ProgramCallback  {

    String programKey = "";
    String eventName = "";
    String appName = "";
    String appKey = "";
    String email = "";
    String language = "";

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        Bundle extras = getIntent().getExtras();
        String serverId = ConfirmitServer.INSTANCE.getUS().getServerId();

        programKey = extras.getString("programKey");
        eventName = extras.getString("eventName");
        appName = extras.getString("appName");
        appKey = extras.getString("appKey");
        email = extras.getString("email");
        language = extras.getString("language");

        Map<String, String> map = new HashMap<>();
        map.put("userId",email);
        map.put("language",language);

        TriggerSDK.INSTANCE.setCallback(serverId,programKey,SurveyFragment.this);
        TriggerSDK.INSTANCE.notifyEvent(serverId, programKey, eventName, map);
    }

    @Override
    public void onSurveyDownloadCompleted(@NotNull TriggerInfo triggerInfo, @NotNull String s, @Nullable Exception e) {
        System.out.println(" fragmenttest onSurveyDownloadCompleted : "+triggerInfo);
    }

    @Override
    public void onSurveyStart(@NotNull SurveyFrameConfig surveyFrameConfig) {
        System.out.println(" fragmenttest onSurveyStart");
    }

    @Override
    public void onScenarioLoad(@NotNull TriggerInfo triggerInfo, @Nullable Exception e) {
        System.out.println(" fragmenttest onScenarioLoad");
    }

    @Override
    public void onScenarioError(@NotNull TriggerInfo triggerInfo, @NotNull Exception e) {
        System.out.println(" fragmenttest onScenarioError : "+e);
    }

    @Override
    public void onAppFeedback(@NotNull TriggerInfo triggerInfo, @NotNull Map<String, String> map) {
        System.out.println(" fragmenttest onAppFeedback : "+map);
    }

    @Override
    public void onWebSurveyStart(@NotNull SurveyWebViewFragment surveyWebViewFragment) {
        System.out.println("fragmenttest onWebSurveyStart");
        surveyWebViewFragment.setCallback(new MySurveyCallback());
        System.out.println("surveyWebViewFragment : "+surveyWebViewFragment.getCustomData());
        surveyWebViewFragment.show(this.getSupportFragmentManager(), "SurveyWebViewFragment");
    }

    public class MySurveyCallback implements SurveyWebViewFragmentCallback {


      @Override
      public void onWebViewSurveyError(@NotNull String serverId, @NotNull String projectId, @NotNull Exception e) {
        System.out.println("onWebViewSurveyError");
      }

      @Override
      public void onWebViewSurveyFinished(@NotNull String serverId, @NotNull String projectId) {
        System.out.println("onWebViewSurveyFinished");

        Log.d("onWebViewSurveyFinished",email+", "+eventName+", "+appName);
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject();
          jsonObject.put("email", email);
          jsonObject.put("eventName", eventName);
          jsonObject.put("serverLocation", "US");
          jsonObject.put("status", "1");
          jsonObject.put("appName", appName);
          System.out.println("updateSurveyStatus jsonObject : "+jsonObject);
        } catch (JSONException e) {
          System.out.println("updateSurveyStatus e : "+e);
          e.printStackTrace();

        }
        System.out.println("updateSurveyStatus 1: "+jsonObject);
        Locatme.updateSurveyStatus(jsonObject,appKey);
        finish();
      }

      @Override
      public void onWebViewSurveyQuit(@NotNull String serverId, @NotNull String projectId) {
        System.out.println("onWebViewSurveyQuit");
        JSONObject jsonObject = null;
        try {
          jsonObject = new JSONObject();
          jsonObject.put("email", email);
          jsonObject.put("eventName", eventName);
          jsonObject.put("serverLocation", "US");
          jsonObject.put("status", "1");
          jsonObject.put("appName", appName);
          System.out.println("updateSurveyStatus jsonObject : "+jsonObject);
        } catch (JSONException e) {
          System.out.println("updateSurveyStatus e : "+e);
          e.printStackTrace();

        }
        System.out.println("updateSurveyStatus 1: "+jsonObject);
        Locatme.updateSurveyStatus(jsonObject,appKey);
        finish();
      }
    }
}
