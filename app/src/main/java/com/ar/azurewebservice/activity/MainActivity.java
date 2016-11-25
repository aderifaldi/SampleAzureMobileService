package com.ar.azurewebservice.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.ar.azurewebservice.R;
import com.ar.azurewebservice.adapter.PromoAdapter;
import com.ar.azurewebservice.model.TPromo;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.radyalabs.irfan.util.AppUtility;

import java.net.MalformedURLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivty {
    private static final String TAG = "MainActivity";

    private MobileServiceTable<TPromo> mobileServiceTable;
    private ArrayList<TPromo> tPromos;

    @BindView(R.id.list) RecyclerView list;

    private PromoAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        adapter = new PromoAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);

        setupAzureService();
        loadPromos();

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TPromo tPromo = adapter.getData().get(position);
                Intent intent = new Intent(getApplicationContext(), PromoDetailActivity.class);
                intent.putExtra("data", tPromo);
                startActivity(intent);
            }
        });

    }

    private void loadPromos(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final MobileServiceList<TPromo> result = mobileServiceTable.execute().get();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            tPromos = new ArrayList<>();
                            tPromos.clear();

                            for(TPromo item : result){
                                tPromos.add(item);
                            }

                            adapter.getData().addAll(tPromos);
                            adapter.notifyItemInserted(adapter.getData().size() - 1);

                            list.setLayoutManager(linearLayoutManager);
                            list.setAdapter(adapter);

                        }
                    });
                } catch (Exception exception) {
                    AppUtility.logD(TAG, "get data error");
                }
                return null;
            }
        }.execute();
    }

    public void setupAzureService() {
        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mobileServiceClient = new MobileServiceClient(
                    AZURE_SERVICE_URL,
                    AZURE_SERVICE_KEY,
                    this).withFilter(new ProgressFilter());

            // Get the Mobile Service Table instance to use
            mobileServiceTable = mobileServiceClient.getTable(TPromo.class);
        } catch (MalformedURLException e) {
            AppUtility.logD(TAG, "There was an error creating the Mobile Service. Verify the URL");
        }
    }

    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(
                ServiceFilterRequest request, NextServiceFilterCallback next) {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    showLoading();
                }
            });

            SettableFuture<ServiceFilterResponse> result = SettableFuture.create();
            try {
                ServiceFilterResponse response = next.onNext(request).get();
                result.set(response);
            } catch (Exception exc) {
                result.setException(exc);
            }

            dismissProgressBar();
            return result;
        }

        private void dismissProgressBar() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    dismissLoading();
                }
            });
        }
    }

}
