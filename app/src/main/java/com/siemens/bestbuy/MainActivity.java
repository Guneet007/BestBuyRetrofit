package com.siemens.bestbuy;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    boolean isScrolling = false;
    boolean isFirstTime = true;
    int currentItems, totalItems, scrolledOutItems;
    public static final String API_KEY = "YQdX4YGQXWpMxkGpZKvl3fXR";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate:started ");

        initViews();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.bestbuy.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        bestBuyApi = retrofit.create(BestBuyApi.class);
        getMovies();
        implementListener();

    }

    private void implementListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrolledOutItems = manager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItems + scrolledOutItems == totalItems)) {
                    isScrolling = false;
                    fetchData();
                }

            }
        });
    }

    private void initViews() {
        manager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progress);
    }

    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getMovies();
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        }, 2000);
    }

    private void getMovies() {
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
                String content = "";
                for (Products product : products) {
                    mNames.add(product.getName());
                    mImageUrls.add(product.getImage());
                    mPrice.add(product.getSalePrice());
                    mReleaseDate.add(product.getStartDate());
                    mRating.add(product.getCustomerReviewAverage());
                    Log.e(TAG, "onResponse: Added");
                }
                initRecyclerView();
            }

            @Override
            public void onFailure(Call<JsonData> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void initRecyclerView() {
        adapter = new RecyclerViewAdapter(mNames, mImageUrls, mReleaseDate, mPrice, mRating, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
    }
}
