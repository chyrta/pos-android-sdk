package com.aevi.sdk.pos.flow.flowservicesample.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aevi.android.rxmessenger.activity.NoSuchInstanceException;
import com.aevi.android.rxmessenger.activity.ObservableActivityHelper;
import com.aevi.sdk.pos.flow.flowservicesample.R;
import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.Customer;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.sample.ui.ModelDisplay;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoyaltyBalanceActivity extends AppCompatActivity {

    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty_balance);
        ButterKnife.bind(this);
        request = Request.fromJson(getIntent().getStringExtra(BaseApiService.ACTIVITY_REQUEST_KEY));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ModelDisplay modelDisplay = (ModelDisplay) getSupportFragmentManager().findFragmentById(R.id.fragment_request_details);
        AdditionalData loyaltyData = new AdditionalData();
        Customer customer = request.getRequestData().getValue(AdditionalDataKeys.DATA_KEY_CUSTOMER, Customer.class);
        if (customer == null) {
            sendResponseAndFinish(new Response(request, false, "Customer data missing"));
        } else {
            loyaltyData.addData(AdditionalDataKeys.DATA_KEY_CUSTOMER, customer);
            loyaltyData.addData("loyaltyPointsBalance", new Random().nextInt(1000));
            loyaltyData.addData("loyaltySignUpDate", "2016-05-24");
            loyaltyData.addData("loyaltyAccumulatedTotal", new Random().nextInt(10000) + 1000);
            modelDisplay.showCustomData(loyaltyData);
            modelDisplay.showTitle(false);
        }
    }

    @OnClick(R.id.send_response)
    public void onFinish() {
        sendResponseAndFinish(new Response(request, true, "Loyalty balance presented"));
    }

    private void sendResponseAndFinish(Response response) {
        try {
            ObservableActivityHelper<Response> activityHelper = ObservableActivityHelper.getInstance(getIntent());
            activityHelper.publishResponse(response);
        } catch (NoSuchInstanceException e) {
            // Ignore
        }
        finish();
    }
}
