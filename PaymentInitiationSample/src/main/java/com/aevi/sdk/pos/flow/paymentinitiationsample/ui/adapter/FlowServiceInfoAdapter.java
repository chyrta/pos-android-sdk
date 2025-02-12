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

package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import com.aevi.sdk.pos.flow.model.PaymentFlowServiceInfo;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;

import java.util.Map;

public class FlowServiceInfoAdapter extends BaseServiceInfoAdapter<PaymentFlowServiceInfo> {

    public FlowServiceInfoAdapter(Context context, PaymentFlowServiceInfo paymentFlowServiceInfo) {
        super(context, R.array.flow_service_labels, paymentFlowServiceInfo);
    }

    @Override
    protected boolean isPositionHeader(int position) {
        return false;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.label.setText(labels[position]);
        String value = "";
        switch (resIds[position]) {
            case R.string.service_label_id:
                value = info.getId();
                break;
            case R.string.service_label_app_version:
                value = info.getServiceVersion();
                break;
            case R.string.service_label_api_version:
                value = info.getApiVersion();
                break;
            case R.string.service_label_vendor:
                value = info.getVendor();
                break;
            case R.string.service_label_accessible_mode:
                value = getYesNo(info.supportsAccessibilityMode());
                break;
            case R.string.fs_label_stages:
                value = getSetValue(info.getStages());
                break;
            case R.string.fs_label_can_adjust_amounts:
                value = getYesNo(info.canAdjustAmounts());
                break;
            case R.string.fs_label_can_pay_amounts:
                value = getPayAmountsValue();
                break;
            case R.string.service_label_currencies:
                value = info.getSupportedCurrencies().size() > 0 ? getSetValue(info.getSupportedCurrencies()) : "All currencies";
                break;
            case R.string.service_label_supported_data_keys:
                value = getSetValue(info.getSupportedDataKeys());
                break;
            case R.string.service_label_request_types:
                value = getSetValue(info.getCustomRequestTypes());
                break;
            case R.string.service_label_flow_types:
                value = getSetValue(info.getSupportedFlowTypes());
                break;
            case R.string.service_label_flow_info:
                value = getFlowAndStageInfo();
                break;
        }
        holder.value.setText(value);
    }

    private String getPayAmountsValue() {
        if (info.canPayAmounts()) {
            return yes + ", via payment methods: " + getSetValue(info.getPaymentMethods());
        }
        return no;
    }

    private String getFlowAndStageInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String[]> flowAndStagesDefinitions = info.getFlowAndStagesDefinitions();
        for (String flow : flowAndStagesDefinitions.keySet()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(flow);
            stringBuilder.append(" --> ");
            stringBuilder.append(getArrayValue(flowAndStagesDefinitions.get(flow)));
        }
        return stringBuilder.toString();
    }
}
