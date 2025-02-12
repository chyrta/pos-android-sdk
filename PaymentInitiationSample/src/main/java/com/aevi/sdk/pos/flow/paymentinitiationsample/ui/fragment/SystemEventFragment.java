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

package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.model.SampleContext;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter.SystemEventAdapter;

import java.util.List;

public class SystemEventFragment extends BaseFragment {

    @BindView(R.id.items)
    RecyclerView infoItems;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.no_items)
    TextView noEvents;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView(infoItems);
        title.setText(R.string.system_events);
        List<FlowEvent> receivedFlowEvents = SampleContext.getInstance(getActivity()).getSystemEventHandler().getReceivedFlowEvents();
        if (!receivedFlowEvents.isEmpty()) {
            infoItems.setAdapter(new SystemEventAdapter(receivedFlowEvents, null));
        } else {
            infoItems.setVisibility(View.GONE);
            noEvents.setVisibility(View.VISIBLE);
            noEvents.setText(R.string.no_events);
        }
    }

}
