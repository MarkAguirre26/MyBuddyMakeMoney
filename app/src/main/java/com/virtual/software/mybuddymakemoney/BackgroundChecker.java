package com.virtual.software.mybuddymakemoney;

import android.content.Context;
import android.os.Handler;

public class BackgroundChecker {
    private Handler handler;
    private Runnable runnable;
    private static final long INTERVAL = 500; // 1 second

    private boolean shouldContinueChecking = true;
    private MainActivity mainActivity;

    public BackgroundChecker(MainActivity mainActivity) {
        this.mainActivity =  mainActivity;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                performBackgroundCheck();

                // Reschedule the task
                handler.postDelayed(this, INTERVAL);
            }
        };
    }

    public void startChecking() {
        handler.postDelayed(runnable, INTERVAL);
    }

    public void stopChecking() {
        handler.removeCallbacks(runnable);

    }

    private void performBackgroundCheck() {

        // Your background check logic goes here
        //  String yourString = mainActivity.txtStopLoss.getText().toString(); // Replace this with your actual string
        if (mainActivity != null) {
//
            if(!mainActivity.isLoaded){
                System.out.println("Main layout already loaded...");
                mainActivity.initializeComponents();
                mainActivity.ResetAll();
//                mainActivity.backgroundChecker.stopChecking();
//                mainActivity.backgroundChecker = null;

            }else{
                if(mainActivity.isViewPagerSLided){
                    mainActivity.isViewPagerSLided = false;
                    System.out.println("isViewPagerSLided:"+mainActivity.isViewPagerSLided);
                    mainActivity.populateFieldsInMainPage();
//                    mainActivity.backgroundChecker.stopChecking();
//                    mainActivity.backgroundChecker = null;
                }
            }

        }
    }
}
