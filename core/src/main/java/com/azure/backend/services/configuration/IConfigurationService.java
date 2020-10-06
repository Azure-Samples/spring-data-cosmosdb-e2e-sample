package com.azure.backend.services.configuration;

import java.util.Map;


public interface IConfigurationService {

  Map<String, String> getConfigEntries();
}
