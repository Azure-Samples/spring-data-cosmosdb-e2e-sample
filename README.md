---
page_type: sample
languages:
- Java
products:
- azure
- azure-key-vault	
- azure-cosmos-db	
description: "This repository contains an end-2-end sample for a spring-boot application on how to use the new Java Azure SDK to pull secrets from keyvault and read/write/query from CosmosDb using spring data"
urlFragment: spring-data-cosmosdb-e2e-sample
---

# spring-data-cosmosdb-e2e-sample

This is an end-2-end sample for a spring-boot application on how to use the new [Azure SDK for Java](https://github.com/azure/azure-sdk-for-java) to pull secrets from Key Vault and read/write/query from Cosmos DB using [Spring Data](https://github.com/Azure/azure-sdk-for-java/tree/master/sdk/cosmos/azure-spring-data-cosmos)

## Features

- Pulling secrets from azure Key Vault via MI 
- Read, write and perform queries using spring data CosmosDB libreary 
- Set Cosmos DB configuration in a separate package to demonistrate a real scenario with microservices 


## Getting Started

### Prerequisites

- Java 1.8 or 11
- Azure subscription with the following ressources:
  - Resource Groups
  - Key Vault (You will need to set a policy for get & list permission)
  - Cosmos DB
- Bash shell (tested on Mac, Ubuntu, Windows with WSL2)
- Visual Studio Code (optional) ([download](https://code.visualstudio.com/download)) or IntelliJ


### Installation

Create the following secrets in your Key Vault

```bash
az keyvault secret set --vault-name "<YOUR-KV-NAME>" --name "cosmosdburisecretname" --value "<Cosmosdb-URI>"

az keyvault secret set --vault-name "<YOUR-KV-NAME>" --name "cosmosdbkeysecretname" --value "<Cosmosdb-Key>"

az keyvault secret set --vault-name "<YOUR-KV-NAME>" --name "cosmosdbsecondarykeysecretname" --value "<Comsmosdb-secondary-key>"

```

### Quickstart

- Clone the repo to your local machine

```bash
git clone https://github.com/Azure-Samples/spring-data-cosmosdb-e2e-sample.git
```

- Set the environment variables for Key Vault name

```bash
KEYVAULT_NAME=<YOUR-KV-NAME>
```

## Resources

- [Azure Spring Data Cosmos client library for Java](hhttps://github.com/Azure/azure-sdk-for-java/tree/master/sdk/cosmos/azure-spring-data-cosmos)
- [Azure Cosmos DB SQL API: Java SDK v4 examples](https://docs.microsoft.com/en-us/azure/cosmos-db/sql-api-java-sdk-samples#query-examples)
- [Async Programming with Project Reactor and the new Azure SDK for Java](https://devblogs.microsoft.com/azure-sdk/async-programming-with-project-reactor/)
- [Quickstart: Build a Java app to manage Azure Cosmos DB SQL API data](https://docs.microsoft.com/en-us/azure/cosmos-db/create-sql-api-java?tabs=sync)
### Credit

Thanks for [Helium](https://github.com/retaildevcrews/helium-java) project for providing the best practice for pulling secrets from Key Vault
