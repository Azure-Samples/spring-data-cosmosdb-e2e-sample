package com.azure.autoconfigure;

import com.azure.backend.services.configuration.IConfigurationService;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.DirectConnectionConfig;
import com.azure.cosmos.GatewayConnectionConfig;
import com.azure.cosmos.implementation.RequestOptions;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.core.ResponseDiagnostics;
import com.azure.spring.data.cosmos.core.ResponseDiagnosticsProcessor;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import com.azure.spring.data.cosmos.repository.config.EnableReactiveCosmosRepositories;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.lang.Nullable;

import java.text.MessageFormat;

@Configuration
@EnableReactiveCosmosRepositories(basePackages = "com.azure.samples")
@EnableConfigurationProperties(CosmosDbProperties.class)
@PropertySource("classpath:application.properties")
@Slf4j
public class CosmosDbAutoConfiguration extends AbstractCosmosConfiguration {

    private static final Logger logger = LogManager.getLogger(CosmosDbAutoConfiguration.class);

    protected final RequestOptions requestOptions = new RequestOptions();
    protected IConfigurationService configurationService;
    private CosmosDbProperties dbProps;

    private AzureKeyCredential azureKeyCredential;

    @Autowired
    ApplicationContext context;

    /**
     */
    @Autowired
    public CosmosDbAutoConfiguration(IConfigurationService configurationService, CosmosDbProperties cosmosDbProperties) {

        this.configurationService = configurationService;
        this.dbProps = cosmosDbProperties;

        this.requestOptions.setConsistencyLevel(ConsistencyLevel.SESSION);
        this.requestOptions.setScriptLoggingEnabled(true);
    }

    @Bean
    @Primary
    public CosmosClientBuilder getCosmosDBConfigBuilder() {
        try {

            String cosmosKey = configurationService.getConfigEntries()
                    .get(dbProps.getKey());

            String cosmosUri = configurationService.getConfigEntries()
                    .get(dbProps.getUri());

            this.azureKeyCredential = new AzureKeyCredential(cosmosKey);
            DirectConnectionConfig directConnectionConfig = new DirectConnectionConfig();
            GatewayConnectionConfig gatewayConnectionConfig = new GatewayConnectionConfig();

            return new CosmosClientBuilder()
                    .endpoint(cosmosUri)
                    .credential(this.azureKeyCredential)
                    .directMode(directConnectionConfig, gatewayConnectionConfig);

        } catch (Exception ex) {
            logger.error(MessageFormat.format("getConfig failed with error: {0}",
                    ex.getMessage()));

            throw ex;
        }
    }

    @Override
    @Bean
    public CosmosConfig cosmosConfig() {
       return CosmosConfig.builder()
               .enableQueryMetrics(this.dbProps.isPopulateQueryMetrics())
               .responseDiagnosticsProcessor(new ResponseDiagnosticsProcessorImplementation())
               .build();
    }

    public void switchToSecondaryKey() {
        String secondaryKey = configurationService.getConfigEntries().get(this.dbProps.getSecondaryKey());
        this.azureKeyCredential.update(secondaryKey);
    }

    public CosmosAsyncContainer getContainer(String cosmosCollection) {

        return this.context
                .getBean(CosmosAsyncClient.class)
                .getDatabase(this.dbProps.getDatabaseName())
                .getContainer(cosmosCollection);
    }

    @Override
    protected String getDatabaseName() {
        return this.dbProps.getDatabaseName();
    }


    private static class ResponseDiagnosticsProcessorImplementation implements ResponseDiagnosticsProcessor {

        @Override
        public void processResponseDiagnostics(@Nullable ResponseDiagnostics responseDiagnostics) {
            log.info("Response Diagnostics {}", responseDiagnostics);
        }
    }

}
