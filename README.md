# Description
Poc Argo WorkFlows and SpringBoot

## TLS connection

Generate a keystore with the ca.crt value (root ca certificate) used by Argo to auto-signed the certificates. This value can be recover from kubernetes secret called argo-service-account-token

```
keytool -import -alias argo -file ca.crt -storetype PKCS12 -storepass underground -keystore argo.p12
keytool -list -v -storetype PKCS12 -storepass underground -keystore argo.p12
```

## Create Java API Client

1. The Argo java API client can be generated from swagger REST Contract recovered from API docs menu option inside Argo UI Portal. 

2. Save this file locally and open the portal Swagger Editor from Swagger (https://editor.swagger.io). Load the previous swagger contract file and generate the Java Client.

3. Load the Maven Project Argo API client and rename the packages and project as you want.

4. Use the jar compiled of the Argo API Client inside your microservice to be used. You must create a keystore folder inside and copy the previous keystore with the root ca used by Argo to connect throw TLS.


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
