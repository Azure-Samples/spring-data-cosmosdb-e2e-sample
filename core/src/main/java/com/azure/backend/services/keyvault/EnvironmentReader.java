package com.azure.backend.services.keyvault;


import com.azure.backend.Constants;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

/**
 * EnvironmentReader
 * A class to read the environment variables and validate them.
 */
@Service
@Slf4j
public class EnvironmentReader implements IEnvironmentReader {

  private String keyVaultName;
  private static final String KV_NAME_REGEX = "^[a-zA-Z](?!.*--)([a-zA-Z0-9-]*[a-zA-Z0-9])?$";

  /**
   * setKeyVaultName.
   * @param kvName set the key vault name from env or cli.
   */
  @SuppressFBWarnings({"squid:S2629", "DM_EXIT"}) // suppressing conditional com.azure.backend.error log
  public void setKeyVaultName(String kvName) {
    if (kvName == null) {
      System.exit(-1);
    }

    if (Boolean.FALSE.equals(isValidKeyVaultName(kvName))) {
      // suppression of 2629 is not working so wrapped call in conditional
      if (log.isErrorEnabled()) {
        log.error(MessageFormat.format(Constants.KEYVAULT_NAME_ERROR_MSG, kvName));
      }
      System.exit(-1);
    }

    this.keyVaultName = kvName;
  }

  /**
   * getKeyVaultName.
   *
   * @return returns the key vault name.
   */
  @SuppressFBWarnings("DM_EXIT")
  public String getKeyVaultName() {
    if (this.keyVaultName != null) {
      if (log.isInfoEnabled()) {
        log.info(MessageFormat.format("KeyVaultName is {0}", this.keyVaultName));
      }
      return this.keyVaultName;
    }

    keyVaultName = System.getenv(Constants.KEY_VAULT_NAME);
    if (keyVaultName == null || !isValidKeyVaultName(keyVaultName)) {
      log.error("KeyVault name not set. Exiting");
      System.exit(-1);
    }

    return keyVaultName;
  }

  /**
   * isValidKeyVaultName.
   */
  private Boolean isValidKeyVaultName(final String keyVaultName) {
    Boolean validSetting = true;

    if (keyVaultName.length() < 3 || keyVaultName.length() > 24) {
      validSetting = false;
    }

    // validate key vault name with regex:
    // ^[a-zA-Z](?!.*--)([a-zA-Z0-9-]*[a-zA-Z0-9])?$
    // ^[a-zA-Z] - start of string, must be a alpha character
    // (?!.*--) - look ahead and make sure there are no double hyphens (e.g., "--")
    // [a-zA-Z0-9-]* - match alphanumeric and hyphen as many times as needed
    // [a-zA-Z0-9] - final character must be alphanumeric
    if (!keyVaultName.matches(KV_NAME_REGEX)) {
      validSetting = false;
    }

    return validSetting;
  }


}