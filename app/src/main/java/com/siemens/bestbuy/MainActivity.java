package com.siemens.bestbuy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewResult = findViewById(R.id.text_view_result);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.bestbuy.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BestBuyApi bestBuyApi = retrofit.create(BestBuyApi.class);
        Call<JsonData> call = bestBuyApi.getProducts();

        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Call<JsonData> call, Response<JsonData> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                JsonData jsonData = response.body();
                List<Products> products = jsonData.getProducts();
                String content = "";
                for (Products product : products) {
                    content += product.getName() + "\n";
                    content += product.getSalePrice() + "\n";
                    content += product.getStartDate() + "\n";
                    content += product.getCustomerReviewAverage() + "\n";
                    content += product.getImage() + "\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<JsonData> call, Throwable t) {
                textViewResult.setText(t.getMessage());

            }
        });


    }
}
