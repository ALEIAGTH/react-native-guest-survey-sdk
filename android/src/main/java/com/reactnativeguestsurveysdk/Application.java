package com.reactnativeguestsurveysdk;

import com.confirmit.mobilesdk.ConfirmitSDK;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Application confirmi set up");
        new ConfirmitSDK.Setup(this).configure();
    }

}
