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


package com.aevi.sdk.flow.model.config;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Jsonable;

import java.util.Objects;

/**
 * Represents a flow application in a flow configuration.
 */
public class FlowApp implements Jsonable {

    private final String id;
    private final boolean mandatory;
    private final String conditionalOn;
    private final boolean delegateCancellationsTo;

    /**
     * Construct with id.
     *
     * Mandatory field defaults to false.
     *
     * @param id The application id
     */
    public FlowApp(String id) {
        this.id = id != null ? id : "N/A";
        this.mandatory = false;
        this.conditionalOn = null;
        this.delegateCancellationsTo = false;
    }

    /**
     * Construct with id, mandatory flag and conditionalOn flag.
     *
     * @param id            The application id
     * @param mandatory     Whether or not the app is mandatory for the flow to be valid
     * @param conditionalOn Condition for this flow app to be eligible
     */
    public FlowApp(String id, boolean mandatory, String conditionalOn) {
        this.id = id != null ? id : "N/A";
        this.mandatory = mandatory;
        this.conditionalOn = conditionalOn;
        this.delegateCancellationsTo = false;
    }

    /**
     * Construct with all parameters.
     *
     * @param id                      The application id
     * @param mandatory               Whether or not the app is mandatory for the flow to be valid
     * @param conditionalOn           Condition for this flow app to be eligible
     * @param delegateCancellationsTo Whether or not FPS should delegate cancellation handling to this flow service
     */
    public FlowApp(String id, boolean mandatory, String conditionalOn, boolean delegateCancellationsTo) {
        this.id = id != null ? id : "N/A";
        this.mandatory = mandatory;
        this.conditionalOn = conditionalOn;
        this.delegateCancellationsTo = delegateCancellationsTo;
    }

    /**
     * Get the application id.
     *
     * @return The application id
     */
    @NonNull
    public String getId() {
        return id;
    }

    /**
     * Check whether this application is mandatory for the flow to be valid.
     *
     * If a flow app is marked as mandatory and not installed on a device, the flow will be considered invalid.
     *
     * @return True if the app is mandatory for the flow, false otherwise
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * Get the condition, if any, that is required for this flow app to be eligible.
     *
     * @return The condition, if any, that is required for this flow app to be eligible.
     */
    @Nullable
    public String getConditionalOnValue() {
        return conditionalOn;
    }

    /**
     * If true, FPS will delegate requests to cancel the transaction/flow to this flow service when
     * it is active in the flow.
     *
     * This is to support use cases where a flow service may be busy doing critical processing and
     * cancelling the flow at that time is not desirable. It is then up to the flow service whether
     * it can respect the cancellation request or not.
     *
     * @return True to delegate cancellations to the flow service, false to let FPS handle it
     */
    public boolean shouldDelegateCancellationsTo() {
        return delegateCancellationsTo;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlowApp flowApp = (FlowApp) o;
        return mandatory == flowApp.mandatory &&
                delegateCancellationsTo == flowApp.delegateCancellationsTo &&
                Objects.equals(id, flowApp.id) &&
                Objects.equals(conditionalOn, flowApp.conditionalOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mandatory, conditionalOn, delegateCancellationsTo);
    }
}
