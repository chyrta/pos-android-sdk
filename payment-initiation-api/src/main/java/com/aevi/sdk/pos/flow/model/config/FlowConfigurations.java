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

package com.aevi.sdk.pos.flow.model.config;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.aevi.sdk.flow.model.config.FlowConfig;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Exposes the various flows and helper methods to query for information.
 */
public class FlowConfigurations {

    private final List<FlowConfig> flowConfigurations;

    public FlowConfigurations(List<FlowConfig> flowConfigurations) {
        this.flowConfigurations = flowConfigurations;
    }

    /**
     * Get the list of flow configurations.
     *
     * @return The list of {@link FlowConfig}
     */
    @NonNull
    public List<FlowConfig> getAll() {
        return flowConfigurations;
    }

    /**
     * Stream the list of flow configurations for simple filtering, conversions, etc.
     *
     * @return An Observable stream of {@link FlowConfig}
     */
    @NonNull
    public Observable<FlowConfig> stream() {
        return Observable.fromIterable(flowConfigurations);
    }

    /**
     * Get the flow configuration with the provided name.
     *
     * @param flowName The flow name
     * @return The flow config
     */
    @Nullable
    public FlowConfig getFlowConfiguration(final String flowName) {
        return fromName(flowName);
    }

    /**
     * Get supported flow types for the given request class.
     *
     * If this should return flow types for initiating generic {@link com.aevi.sdk.flow.model.Request}, then use {@link FlowConfig#REQUEST_CLASS_GENERIC}
     *
     * If this should return flow types for initiating {@link com.aevi.sdk.pos.flow.model.Payment}, then use {@link FlowConfig#REQUEST_CLASS_PAYMENT}
     *
     * If null is passed, all types will be returned.
     *
     * @param requestClass {@link FlowConfig#REQUEST_CLASS_GENERIC}, {@link FlowConfig#REQUEST_CLASS_PAYMENT} or null for all types
     * @return A list of supported flow types
     */
    public List<String> getFlowTypes(@Nullable final String requestClass) {
        return stream()
                .filter(flowConfig -> requestClass == null || flowConfig.getRequestClass().equals(requestClass))
                .map(FlowConfig::getType)
                .toList()
                .blockingGet();
    }

    /**
     * Check whether a flow type is supported or not.
     *
     * A flow type is defined as supported if there is at least one flow configuration defined for that type.
     *
     * @param type The flow type to check
     * @return True if there is at least one flow for this type, false otherwise
     */
    public boolean isFlowTypeSupported(final String type) {
        return stream()
                .filter(flowConfig -> flowConfig.getType().equals(type))
                .count()
                .blockingGet() > 0;
    }

    /**
     * Get a list of all the flow names that are associated with the provided types.
     *
     * @param typesArray The types to filter by
     * @return The list of flow names
     */
    @NonNull
    public List<String> getFlowNamesForType(String... typesArray) {
        List<String> flowNames = new ArrayList<>();
        List<String> types = Arrays.asList(typesArray);
        for (FlowConfig flowConfiguration : flowConfigurations) {
            if (types.contains(flowConfiguration.getType())) {
                flowNames.add(flowConfiguration.getName());
            }
        }
        return flowNames;
    }

    /**
     * Get a list of all the flow configs that are associated with the provided types.
     *
     * @param typesArray The types to filter by
     * @return The list of flow names
     */
    @NonNull
    public List<FlowConfig> getFlowConfigsForType(String... typesArray) {
        List<FlowConfig> flowConfigs = new ArrayList<>();
        List<String> types = Arrays.asList(typesArray);
        for (FlowConfig flowConfiguration : flowConfigurations) {
            if (types.contains(flowConfiguration.getType())) {
                flowConfigs.add(flowConfiguration);
            }
        }
        return flowConfigs;
    }

    /**
     * Check whether a particular flow has the provided stage defined.
     *
     * @param stage    The flow stage
     * @param flowName The flow to check if the stage is defined for
     * @return True if the flow has the stage defined, false otherwise
     */
    public boolean isStageDefinedForFlow(String stage, String flowName) {
        FlowConfig flowConfig = fromName(flowName);
        if (flowConfig != null) {
            return flowConfig.hasStage(stage);
        }
        return false;
    }

    @Nullable
    private FlowConfig fromName(String flowName) {
        for (FlowConfig flowConfiguration : flowConfigurations) {
            if (flowConfiguration.getName().equals(flowName)) {
                return flowConfiguration;
            }
        }
        return null;
    }

}
