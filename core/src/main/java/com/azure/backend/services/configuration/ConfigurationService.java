package com.azure.backend.services.configuration;

import com.azure.backend.services.keyvault.IKeyVaultService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ConfigurationService implements IConfigurationService {
  private IKeyVaultService keyVaultService;

  Map<String, String> configEntries = new ConcurrentHashMap<>();

  public Map<String, String> getConfigEntries() {
    return configEntries;
  }

  /**
   * ConfigurationService.
   */
  @SuppressFBWarnings("DM_EXIT")
  @Autowired
  public ConfigurationService(IKeyVaultService kvService) {
    try {
      if (kvService == null) {
        log.info("keyVaultService is null");
        System.exit(-1);
      }

      keyVaultService = kvService;
      Map<String, String> secrets = keyVaultService.getSecrets();
      if (log.isInfoEnabled()) {
        log.info(MessageFormat.format("Secrets are {0}", secrets == null ? "NULL" : "NOT NULL"));
      }
      configEntries = secrets;

    } catch (Exception ex) {
      log.error(ex.getMessage());
      throw ex;
    }
  }
}