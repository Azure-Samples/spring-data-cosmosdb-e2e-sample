package com.azure.backend.services.keyvault;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.backend.error.SampleAzureException;
import com.azure.security.keyvault.certificates.CertificateAsyncClient;
import com.azure.security.keyvault.certificates.CertificateClientBuilder;
import com.azure.security.keyvault.certificates.models.KeyVaultCertificateWithPolicy;
import com.azure.security.keyvault.keys.KeyAsyncClient;
import com.azure.security.keyvault.keys.KeyClientBuilder;
import com.azure.security.keyvault.keys.models.KeyVaultKey;
import com.azure.security.keyvault.secrets.SecretAsyncClient;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.azure.security.keyvault.secrets.models.SecretProperties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class KeyVaultService implements IKeyVaultService {
  private final String keyVaultName;
  private KeyAsyncClient keyAsyncClient;
  private SecretClient secretClient;
  private SecretAsyncClient secretAsyncClient;
  private CertificateAsyncClient certificateAsyncClient;
  private TokenCredential credential; 

  /**
   * KeyVaultService.
   */
  public KeyVaultService(IEnvironmentReader environmentReader)
          throws SampleAzureException {

    this.keyVaultName = environmentReader.getKeyVaultName();
    if (log.isInfoEnabled()) {
      log.info(MessageFormat.format("KeyVaultName is {0}", this.keyVaultName));
    }

    credential = new DefaultAzureCredentialBuilder().build();

    //build KeyVault clients
    secretClient = new SecretClientBuilder()
        .vaultUrl(getKeyVaultUri())
        .credential(credential)
        .buildClient();

    secretAsyncClient = new SecretClientBuilder()
        .vaultUrl(getKeyVaultUri())
        .credential(credential)
        .buildAsyncClient();

    //build key client
    keyAsyncClient = new KeyClientBuilder()
      .vaultUrl(getKeyVaultUri())
      .credential(credential)
      .buildAsyncClient();
  
    //build certificate client
    certificateAsyncClient = new CertificateClientBuilder()
    .vaultUrl(getKeyVaultUri())
    .credential(credential)
    .buildAsyncClient();
  }

  /**
   * getKeyVaultUri.
   */
  public String getKeyVaultUri() {
    String kvUri = "";

    if (keyVaultName != null && !keyVaultName.isEmpty()) {
      if (keyVaultName.toUpperCase().startsWith("HTTPS://")) {
        kvUri = keyVaultName;
      } else {
        kvUri = "https://" + keyVaultName + ".vault.azure.net";
      }
    }
    if (log.isInfoEnabled()) {
      log.info(MessageFormat.format("KeyVaultUrl is {0}", kvUri));
    }
    return kvUri;
  }

  /**
   * getSecret.
   */
  public Mono<String> getSecretValue(final String secretName) {
    
    if (log.isInfoEnabled()) {
      log.info(MessageFormat.format("Secrets in getSecret are {0}",
          secretName == null ? "NULL" : "NOT NULL"));
    }

    return  getSecret(secretName)
      .map(keyVaultSecret -> keyVaultSecret.getValue());
  }

  public Mono<KeyVaultSecret> getSecret(final String secretName) {
    return  secretAsyncClient.getSecret(secretName);
  }

  /**
   * listSecretsSync.
   * Returns null on com.azure.backend.error and logs com.azure.backend.error.
   * Created as a blocking function as app start-up is dependent on success.
   */
  public List<SecretProperties> listSecrets() {
    List<SecretProperties> listSecretProps = new ArrayList<>();
    
    Iterator<SecretProperties> secretPropsIterator = secretClient
        .listPropertiesOfSecrets()
        .iterator();
        
    secretPropsIterator.forEachRemaining(secretProps -> 
        listSecretProps.add(secretProps));

    return listSecretProps;
  }

  /**
   * getSecretsSync.
   * Created as a blocking function as app start-up is dependent on success.
   */
  public Map<String, String> getSecrets() {
    final List<SecretProperties> secretItems = listSecrets();

    final Map<String, String> secrets = new ConcurrentHashMap<>();
    try {
      secretItems.forEach(item -> {
        final String itemName = item.getName();
        final String secretValue = getSecretValue(itemName).block();
        if (log.isInfoEnabled()) {
          log.info(MessageFormat.format("lengths of secretItem name and value are {0} {1}",
              itemName.length(),
              secretValue.length()));
        }
        secrets.put(itemName, secretValue);
      });
    } catch (Exception ex) {
      log.error("Exception retrieving secrets from com.azure.backend.keyvault ", ex);
      throw ex;
    }
    return secrets;
  }

  /**
   * getKey.
   */
  public Mono<KeyVaultKey> getKey(final String keyName) {
    return  keyAsyncClient.getKey(keyName);
  }

  /**
   * getCertificate.
   */
  public Mono<KeyVaultCertificateWithPolicy> getCertificate(final String certName) {
    return certificateAsyncClient.getCertificate(certName);
  }
}
