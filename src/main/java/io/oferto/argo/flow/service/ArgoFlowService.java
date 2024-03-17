package io.oferto.argo.flow.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.oferto.argo.flow.configure.ArgoFlowConfig;
import io.oferto.argo.client.ApiClient;
import io.oferto.argo.client.ApiException;
import io.oferto.argo.client.Configuration;
import io.oferto.argo.client.api.WorkflowServiceApi;
import io.oferto.argo.client.model.IoArgoprojWorkflowV1alpha1Workflow;
import io.oferto.argo.client.model.IoArgoprojWorkflowV1alpha1WorkflowSubmitRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArgoFlowService {
	private final RestTemplate restTemplate;
	
	private final ArgoFlowConfig argoFlowConfig;
	
	private final String TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6Ik1mSVQ0b29vYlJOd01wZ2plWWVtcHFaandOM2xXZXB4MFhCREV6MUtGaTAifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJhcmdvIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImFyZ28uc2VydmljZS1hY2NvdW50LXRva2VuIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImFyZ28iLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJlODAwMGM5Mi05MmNiLTQ2Y2MtYWU2My0xMDk5YjU4N2U5MGYiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6YXJnbzphcmdvIn0.YKLFon2xZVjht1ZLec-bHa7hm5C9wsv-M7O_JddPnstee7ACM6kb7YOlqfAqkSCCbJJyfA9S88ET8fzhGcpo_cxQFVr7Ctq09EZ24OCJJZgIegUypN_RcuVzcQCGvtVonJvOojotbfkvHFORqX00VZVxtGcrM_C1kK0-ef8aLRKGA9dN8z5iW-eQ3-h8-fklNXyZFdv0rQjHU8NI7fQ-mzF75V9eKAyIX4_s2RCStMlk892wYltn9jRH809UBV5SyBOF4VmN7gKcI8aPoIhA1-_gF3glTbt4tGmSuOzwfZrWAPggPYfM_2c6dGNhgUaAHrRKtBVuZjVW0dFd9u5iFg";
	private final String NAMESPACE = "argo";
	
    @Value("${trust.store}")
    private Resource trustStore;

    @Value("${trust.store.password}")
    private String trustStorePassword; 
    
	public Object submitFlow(String template) {
		log.debug("save annotation from service");

		/*HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + TOKEN);
		
		String url = "https://" + argoFlowConfig.getHost() + ":" + argoFlowConfig.getPort() + "/api/v1/workflows/" + NAMESPACE + "/submit";
			
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HttpEntity body = new HttpEntity(template ,headers);
		  
		ResponseEntity<Object> argoResult = restTemplate.exchange(url, HttpMethod.POST, body, Object.class);
		
		return argoResult.getBody();*/
		
		KeyStore sr = null;
		try {
			sr = KeyStore.getInstance("pkcs12");
			try {
				sr.load(trustStore.getInputStream(), "underground".toCharArray());
				
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		ApiClient defaultClient = Configuration.getDefaultApiClient();
	    defaultClient.setBasePath("https://" + argoFlowConfig.getHost() + ":" + argoFlowConfig.getPort());
		defaultClient.setVerifyingSsl(false);
		try {
			defaultClient.setSslCaCert(new ByteArrayInputStream(sr.getCertificate("argo").getEncoded()));
		} catch (CertificateEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		defaultClient.setApiKeyPrefix("Bearer");
		defaultClient.setApiKey(TOKEN);
		
	    WorkflowServiceApi apiInstance = new WorkflowServiceApi(defaultClient);
	    
		IoArgoprojWorkflowV1alpha1WorkflowSubmitRequest body = new IoArgoprojWorkflowV1alpha1WorkflowSubmitRequest();
		body.setNamespace(NAMESPACE);
		body.setResourceKind("WorkflowTemplate");
		body.setResourceName("arm-hello-world");
		
		IoArgoprojWorkflowV1alpha1Workflow argoResult = null;
		try {
			argoResult = apiInstance.workflowServiceSubmitWorkflow(NAMESPACE, body);
		} catch (ApiException e) {
			System.err.println("Exception when calling WorkflowServiceApi#workflowServiceSubmitWorkflow");
		    System.err.println("Status code: " + e.getCode());
		    System.err.println("Reason: " + e.getResponseBody());
		    System.err.println("Response headers: " + e.getResponseHeaders());
		    
		    e.printStackTrace();
		}
		
		return argoResult;
	}	
}
