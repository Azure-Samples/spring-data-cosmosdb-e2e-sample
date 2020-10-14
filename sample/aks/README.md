# Deployment Walk Through

## Background


### Azure Components in Use

- Azure Container Registry
- Azure Kubernetes Service
  - Azure AAD Pod Identity
  - csi-secret store provider for azure
- Azure Key Vault
- Azure Cosmos DB

## Demo Install

### Prerequisites

- Azure subscription with permissions to create:
  - Resource Groups, Service Principals, Keyvault, Cosmos DB, AKS, Azure Container Registry
- Bash shell (tested on Mac, Ubuntu, Windows with WSL2)
- Azure CLI ([download](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest))
- Docker CLI ([download](https://docs.docker.com/install/))
- Visual Studio Code (optional) ([download](https://code.visualstudio.com/download))
- kubectl (install by using `sudo az aks install-cli`)
- Helm v3 ([Install Instructions](https://helm.sh/docs/intro/install/))

### Setup

Fork this repo and clone to your local machine

```bash

cd $HOME

mkdir azure-sample

cd zure-sample

git clone 
```

#### Login to Azure and select subscription

```bash

az login

# select the Azure account
az account set -s {subscription name or Id}
```

####  Save the Cosmos DB config to Key Vault

- All secrets have been stored in Azure Key Vault for security
  - This sample uses Managed Identity to access Key Vault


```bash

# add Cosmos DB config to Key Vault
az keyvault secret set --vault-name "<YOUR-KV-NAME>" --name "cosmosdburisecretname" --value "<Cosmosdb-URI>"
az keyvault secret set --vault-name "<YOUR-KV-NAME>" --name "cosmosdbkeysecretname" --value "<Cosmosdb-Key>"
az keyvault secret set --vault-name "<YOUR-KV-NAME>" --name "cosmosdbsecondarykeysecretname" --value "<Comsmosdb-secondary-key>"

```

Set the environment variables for Key Vault name

```bash
KEYVAULT_NAME=<YOUR-KV-NAME>
```

## Install Helm 3

Install the latest version of Helm by download the latest [release](https://github.com/helm/helm/releases):

```bash

# mac os
OS=darwin-amd64 && \
REL=v3.2.4 && \ #Should be lastest release from https://github.com/helm/helm/releases
mkdir -p $HOME/.helm/bin && \
curl -sSL "https://get.helm.sh/helm-${REL}-${OS}.tar.gz" | tar xvz && \
chmod +x ${OS}/helm && mv ${OS}/helm $HOME/.helm/bin/helm
rm -R ${OS}

```

or

```bash

# Linux/WSL
OS=linux-amd64 && \
REL=v3.2.4 && \
mkdir -p $HOME/.helm/bin && \
curl -sSL "https://get.helm.sh/helm-${REL}-${OS}.tar.gz" | tar xvz && \
chmod +x ${OS}/helm && mv ${OS}/helm $HOME/.helm/bin/helm
rm -R ${OS}

```

Add the helm binary to your path and set Helm home:

```bash

export PATH=$PATH:$HOME/.helm/bin
export HELM_HOME=$HOME/.helm

```

>NOTE: This will only set the helm command during the existing terminal session. Copy the 2 lines above to your bash or zsh profile so that the helm command can be run any time.

Verify the installation with:

```bash

helm version

```

Add the required helm repositories

```bash

helm repo add stable https://kubernetes-charts.storage.googleapis.com
helm repo add aad-pod-identity https://raw.githubusercontent.com/Azure/aad-pod-identity/master/charts
helm repo update

```

Install the sample helm chart

```bash

cd $REPO_ROOT/aks/cluster/charts

helm install java-azure-samplee java-azure-samplee

```