# [Choice] Java version: 11, 14
ARG VARIANT=11
FROM mcr.microsoft.com/vscode/devcontainers/java:${VARIANT}

# [Option] Install Maven
ARG INSTALL_MAVEN="true"
ARG MAVEN_VERSION=""

# [Option] Install Gradle
ARG INSTALL_GRADLE="false"
ARG GRADLE_VERSION=""
RUN if [ "${INSTALL_MAVEN}" = "true" ]; then su vscode -c "source /usr/local/sdkman/bin/sdkman-init.sh && sdk install maven \"${MAVEN_VERSION}\""; fi \
    && if [ "${INSTALL_GRADLE}" = "true" ]; then su vscode -c "source /usr/local/sdkman/bin/sdkman-init.sh && sdk install gradle \"${GRADLE_VERSION}\""; fi

# [Option] Install Node.js
ARG INSTALL_NODE="false"
ARG NODE_VERSION="lts/*"
RUN if [ "${INSTALL_NODE}" = "true" ]; then su vscode -c "source /usr/local/share/nvm/nvm.sh && nvm install ${NODE_VERSION} 2>&1"; fi

# [Option] Install Node.js
ARG INSTALL_TERRAFORM="true"
ARG TERRAFORM_VERSION="0.13.4"
RUN if [ "${INSTALL_TERRAFORM}" = "true" ]; then wget "https://releases.hashicorp.com/terraform/${TERRAFORM_VERSION}/terraform_${TERRAFORM_VERSION}_linux_amd64.zip"; unzip terraform_${TERRAFORM_VERSION}_linux_amd64.zip; sudo mv terraform /usr/local/bin/; rm terraform_${TERRAFORM_VERSION}_linux_amd64.zip; fi

# [Option] Install az cli, ref: https://docs.microsoft.com/en-us/cli/azure/install-azure-cli-apt
ARG INSTALL_AZCLI="true"
RUN if [ "${INSTALL_AZCLI}" = "true" ]; then curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash; fi

# [Optional] Uncomment this section to install additional OS packages.
# RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
#     && apt-get -y install --no-install-recommends <your-package-list-here>

# [Optional] Uncomment this line to install global node packages.
# RUN su vscode -c "source /usr/local/share/nvm/nvm.sh && npm install -g <your-package-here>" 2>&1