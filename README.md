# Description
Poc Argo WorkFlows and SpringBoot

## TLS connection

Generate a keystore pkcs12 with the **ca.crt** file (root ca certificate) used by Argo to auto-signed the certificates. This file can be created from kubernetes secret called **argo-service-account-token** located in argo namespace

```
keytool -import -alias argo -file ca.crt -storetype PKCS12 -storepass underground -keystore argo.p12
keytool -list -v -storetype PKCS12 -storepass underground -keystore argo.p12
```

This keystore must be include in a keystore folder resource in our project to connect to Argo API using TLS protocol.

## Create Java API Client

1. The Argo java API client contract can be download from swagger REST Contract recovered from API docs menu item inside Argo UI Portal. 

2. Save this file locally and open the portal Swagger Editor from Swagger (https://editor.swagger.io). Load the previous swagger contract file and generate the Java Client Project Resources.

3. Load the Maven Project, rename the packages and project as you want and compile to generate the java client artifact to be imported in our projects to access Argo using its API.

4. Use the jar compiled of the Argo API Client inside your microservice to be used. You must create a keystore folder inside and copy the previous keystore with the root ca used by Argo to connect throw TLS.

Other way to generate the argo API Client resources is using the swagger generator like this:

```
java --add-opens=java.base/java.util=ALL-UNNAMED -jar swagger-codegen-cli.jar generate \
  -i argo-swagger.json \
  --api-package io.oferto.argo.flow.client.api \
  --model-package io.oferto.argo.flow.client.model \
  --invoker-package io.oferto.argo.flow.client.invoker \
  --group-id io.oferto \
  --artifact-id poc-argo-flow-api-client \
  --artifact-version 0.0.1-SNAPSHOT \
  -l java \
  --library resttemplate \
  -o io-oferto-argo-flow-api-client
```
  
## Debug

First submit a workflow template from Argo UI

```
metadata:
  name: arm-hello-world
  namespace: argo
  labels:
    example: 'true'
spec:
  workflowMetadata:
    labels:
      example: 'true'
  entrypoint: echotest
  templates:
    - name: echotest
      container:
        image: alpine
        command: ["sh","-c"]
        args: ["echo","hello"]
  ttlStrategy:
    secondsAfterCompletion: 300
  podGC:
    strategy: OnPodCompletion
```

Recover the Authentication Token to send requests

```
ARGO_TOKEN="Bearer $(kubectl get secret -n argo argo.service-account-token -o=jsonpath='{.data.token}' | base64 --decode)"
```

Execute the POST submit request for the previous workflow template from curl like this:

```
curl https://localhost:2746/api/v1/workflows/argo --insecure -H "Authorization: $ARGO_TOKEN"
-d '{"namespace": "argo", "resourceKind": "WorkflowTemplate", "resourceName": "arm-hello-world"}'
```
