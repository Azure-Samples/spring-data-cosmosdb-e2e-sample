package com.azure.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "azure.cosmosdb")
public class CosmosDbProperties {

    private String uri;
    private String key;
    private String secondaryKey;
    private String databaseName;
    private boolean populateQueryMetrics;
}
