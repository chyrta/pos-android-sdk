/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


package com.aevi.sdk.pos.flow.stage;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.service.ClientCommunicator;
import com.aevi.sdk.flow.stage.BaseStageModel;
import com.aevi.sdk.pos.flow.model.Card;
import com.aevi.sdk.pos.flow.model.TransactionRequest;
import com.aevi.sdk.pos.flow.model.TransactionResponseBuilder;
import com.aevi.sdk.pos.flow.service.ActivityProxyService;
import com.aevi.sdk.pos.flow.service.BasePaymentFlowService;

/**
 * Model for the card-reading stage that exposes all the data functions and other utilities required for any app to process this stage.
 *
 * See {@link BasePaymentFlowService} for how to retrieve the model from a service context, and {@link ActivityProxyService} for
 * how to proxy the request onto an activity from where this can be instantiated via {@link #fromActivity(Activity)}.
 *
 * One of {@link #approveWithCard(Card)}, {@link #declineTransaction(String)} or {@link #skipCardReading()} must be called.
 */
public class CardReadingModel extends BaseStageModel {

    private final TransactionRequest transactionRequest;
    private final TransactionResponseBuilder transactionResponseBuilder;

    private CardReadingModel(Activity activity, TransactionRequest request) {
        super(activity);
        this.transactionRequest = request;
        this.transactionResponseBuilder = new TransactionResponseBuilder(transactionRequest.getId());
    }

    private CardReadingModel(ClientCommunicator clientCommunicator, TransactionRequest request, InternalData senderInternalData) {
        super(clientCommunicator, senderInternalData);
        this.transactionRequest = request;
        this.transactionResponseBuilder = new TransactionResponseBuilder(transactionRequest.getId());
    }

    /**
     * Create an instance from an activity context.
     *
     * This assumes that the activity was started via {@link BaseStageModel#processInActivity(Context, Class)},
     * or via the {@link ActivityProxyService}.
     *
     * @param activity The activity that was started via one of the means described above
     * @return An instance of {@link CardReadingModel}
     */
    @NonNull
    public static CardReadingModel fromActivity(Activity activity) {
        return new CardReadingModel(activity, TransactionRequest.fromJson(getActivityRequestJson(activity)));
    }

    /**
     * Create an instance from a service context.
     *
     * @param clientCommunicator The client communicator for sending/receiving messages at this point in the flow
     * @param request            The deserialised TransactionRequest
     * @param senderInternalData The internal data of the app that started this stage
     * @return An instance of {@link CardReadingModel}
     */
    @NonNull
    public static CardReadingModel fromService(ClientCommunicator clientCommunicator, TransactionRequest request, InternalData senderInternalData) {
        return new CardReadingModel(clientCommunicator, request, senderInternalData);
    }

    /**
     * Get the transaction request.
     *
     * @return The transaction request
     */
    @NonNull
    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    /**
     * Approve the card reading with a valid card instance.
     *
     * This will send the response back but it does NOT finish any activity or stop any service. That is down to the activity/service to manage internally.
     *
     * @param card The card details
     */
    public void approveWithCard(Card card) {
        transactionResponseBuilder.approve();
        transactionResponseBuilder.withCard(card);
        sendResponse();
    }

    /**
     * Call to skip the card reading stage.
     *
     * This will send the response back but it does NOT finish any activity or stop any service. That is down to the activity/service to manage internally.
     */
    public void skipCardReading() {
        transactionResponseBuilder.approve();
        sendResponse();
    }

    /**
     * Decline the transaction due to invalid card, cancellation, etc.
     *
     * This will send the response back but it does NOT finish any activity or stop any service. That is down to the activity/service to manage internally.
     *
     * @param message The decline message
     */
    public void declineTransaction(String message) {
        declineTransaction(message, null);
    }

    /**
     * Decline the transaction due to invalid card, cancellation, etc.
     *
     * This will send the response back but it does NOT finish any activity or stop any service. That is down to the activity/service to manage internally.
     *
     * @param message      The decline message
     * @param responseCode The response code
     */
    public void declineTransaction(String message, String responseCode) {
        transactionResponseBuilder.decline(message);
        transactionResponseBuilder.withResponseCode(responseCode);
        sendResponse();
    }

    private void sendResponse() {
        doSendResponse(transactionResponseBuilder.build().toJson());
    }

    @Override
    @NonNull
    public String getRequestJson() {
        return transactionRequest.toJson();
    }
}
