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

package com.aevi.sdk.flow.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aevi.sdk.flow.BuildConfig;
import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.stage.StatusUpdateModel;

/**
 * Base service for handling status update requests defined by a request type and associated bespoke data.
 */
public abstract class BaseStatusUpdateService extends BaseApiService {

    public BaseStatusUpdateService() {
        super(BuildConfig.VERSION);
    }

    @Override
    protected final void processRequest(@NonNull ClientCommunicator clientCommunicator, @NonNull String request,
                                        @Nullable InternalData senderInternalData) {
        StatusUpdateModel statusUpdateModel = StatusUpdateModel.fromService(clientCommunicator, Request.fromJson(request), senderInternalData);
        processRequest(statusUpdateModel);
    }

    /**
     * Handle the status update request.
     *
     * @param stageModel The status update stage model
     */
    protected abstract void processRequest(@NonNull StatusUpdateModel stageModel);
}
