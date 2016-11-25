package com.ar.azurewebservice.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.radyalabs.irfan.util.AppUtility;

/**
 * Created by aderifaldi on 25/11/2016.
 */

public class BaseActivty extends AppCompatActivity {

    public static final String AZURE_SERVICE_URL = "";
    public static final String AZURE_SERVICE_KEY = "";

    private ProgressDialog loading;

    public MobileServiceClient mobileServiceClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showLoading(){
        loading = AppUtility.showLoading(loading, this);
    }

    public void dismissLoading(){
        loading.dismiss();
    }

}
