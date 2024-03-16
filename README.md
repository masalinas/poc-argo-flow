# Description
Poc Argo WorkFlows and SpringBoot

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
