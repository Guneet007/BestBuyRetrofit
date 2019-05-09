package com.siemens.bestbuy;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static ArrayList<String> mNames = new ArrayList<>();
    private static ArrayList<String> mImageUrls = new ArrayList<>();
    private static ArrayList<String> mReleaseDate = new ArrayList<>();
    private static ArrayList<Double> mPrice = new ArrayList<>();
    private static ArrayList<Double> mRating = new ArrayList<>();
    private BestBuyApi bestBuyApi;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    ProgressBar progressBar;
    LinearLayoutManager manager;

    public static final String API_KEY = "YQdX4YGQXWpMxkGpZKvl3fXR";

    private int pg_no = 1;

    private boolean isLoading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount, previousTotal = 0;
    private int view_threshold = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate:started ");

        initViews();
        createApiClient();
        progressBar.setVisibility(View.VISIBLE);
        loadData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = manager.getChildCount();
                totalItemCount = manager.getItemCount();
                pastVisibleItems = manager.findFirstVisibleItemPosition();
                if (dy > 0) {
                    if (isLoading) {
                        if (totalItemCount > previousTotal) {
                            isLoading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!isLoading && (totalItemCount - visibleItemCount) <= (pastVisibleItems + view_threshold)) {
                        pg_no++;
                        performPagination();
                        isLoading = true;
                    }
                }
            }
        });

    }

    private void createApiClient() {
        bestBuyApi = ApiClient.getApiClient().create(BestBuyApi.class);
    }

    private void loadData() {
        Call<JsonData> call = bestBuyApi.getProducts("json", API_KEY);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Call<JsonData> call, Response<JsonData> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }
                JsonData jsonData = response.body();
                List<Products> products = jsonData.getProducts();
                Log.e(TAG, "onResponse: " + products.size());
                for (Products product : products) {
                    mNames.add(product.getName());
                    mImageUrls.add(product.getImage());
                    mPrice.add(product.getSalePrice());
                    mReleaseDate.add(product.getStartDate());
                    mRating.add(product.getCustomerReviewAverage());
                    Log.e(TAG, "onResponse: Added");
                }
                adapter = new RecyclerViewAdapter(mNames, mImageUrls, mReleaseDate, mPrice, mRating, getApplicationContext());
                recyclerView.setAdapter(adapter);
                Toast.makeText(getApplicationContext(), "Page" + pg_no + " is loaded", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JsonData> call, Throwable t) {

            }
        });


    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progress);
        manager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
    }

    private void performPagination() {
        progressBar.setVisibility(View.VISIBLE);
        Call<JsonData> call = bestBuyApi.getProducts("json", API_KEY);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Call<JsonData> call, Response<JsonData> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }
                JsonData jsonData = response.body();
                List<Products> products = jsonData.getProducts();
                Log.e(TAG, "onResponse: " + products.size());
                adapter.addProducts(products);
                Toast.makeText(getApplicationContext(), "Page " + pg_no + " is loaded", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JsonData> call, Throwable t) {

            }
        });

    }


}
