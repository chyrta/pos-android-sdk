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

package com.aevi.sdk.pos.flow.flowservicesample;


import androidx.annotation.NonNull;
import android.util.Log;
import com.aevi.sdk.pos.flow.model.PaymentFlowServiceInfo;
import com.aevi.sdk.pos.flow.model.PaymentFlowServiceInfoBuilder;
import com.aevi.sdk.pos.flow.provider.BasePaymentFlowServiceInfoProvider;

import static com.aevi.sdk.flow.constants.FlowTypes.*;
import static com.aevi.sdk.flow.constants.PaymentMethods.*;
import static com.aevi.sdk.flow.constants.ServiceInfoErrors.*;
import static com.aevi.sdk.pos.flow.flowservicesample.service.GenericRequestService.SHOW_LOYALTY_POINTS_REQUEST;

public class PaymentFlowServiceInfoProvider extends BasePaymentFlowServiceInfoProvider {

    private static final String TAG = PaymentFlowServiceInfoProvider.class.getSimpleName();

    @Override
    protected PaymentFlowServiceInfo getPaymentFlowServiceInfo() {
        return new PaymentFlowServiceInfoBuilder()
                .withVendor("AEVI")
                .withDisplayName("Flow Service Sample")
                .withCanAdjustAmounts(true)
                .withCanPayAmounts(true, PAYMENT_METHOD_LOYALTY_POINTS, PAYMENT_METHOD_GIFT_CARD, PAYMENT_METHOD_CASH)
                .withSupportedFlowTypes(FLOW_TYPE_SALE, FLOW_TYPE_TOKENISATION, FLOW_TYPE_RECEIPT_DELIVERY, FLOW_TYPE_BASKET_STATUS_UPDATE)
                .withCustomRequestTypes(SHOW_LOYALTY_POINTS_REQUEST)
                .build(getContext());
    }

    @Override
    protected boolean onServiceInfoError(@NonNull String errorType, @NonNull String errorMessage) {
        switch (errorType) {
            case RETRIEVAL_TIME_OUT:
                Log.d(TAG, "Retrieval of service info timed out");
                break;
            case INVALID_STAGE_DEFINITIONS:
                Log.d(TAG, "Problems with stage definitions: " + errorMessage);
                break;
            case INVALID_SERVICE_INFO:
                Log.d(TAG, "Invalid service info: " + errorMessage);
                break;
        }
        return true;
    }

}
