package io.oferto.argo.flow.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.oferto.argo.flow.configure.ArgoFlowConfig;

@Slf4j
@AllArgsConstructor
@Service
public class ArgoFlowService {
	private RestTemplate restTemplate;
	
	private ArgoFlowConfig argoFlowConfig;
	private final String TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6Ik1mSVQ0b29vYlJOd01wZ2plWWVtcHFaandOM2xXZXB4MFhCREV6MUtGaTAifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJhcmdvIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImFyZ28uc2VydmljZS1hY2NvdW50LXRva2VuIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImFyZ28iLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJlODAwMGM5Mi05MmNiLTQ2Y2MtYWU2My0xMDk5YjU4N2U5MGYiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6YXJnbzphcmdvIn0.YKLFon2xZVjht1ZLec-bHa7hm5C9wsv-M7O_JddPnstee7ACM6kb7YOlqfAqkSCCbJJyfA9S88ET8fzhGcpo_cxQFVr7Ctq09EZ24OCJJZgIegUypN_RcuVzcQCGvtVonJvOojotbfkvHFORqX00VZVxtGcrM_C1kK0-ef8aLRKGA9dN8z5iW-eQ3-h8-fklNXyZFdv0rQjHU8NI7fQ-mzF75V9eKAyIX4_s2RCStMlk892wYltn9jRH809UBV5SyBOF4VmN7gKcI8aPoIhA1-_gF3glTbt4tGmSuOzwfZrWAPggPYfM_2c6dGNhgUaAHrRKtBVuZjVW0dFd9u5iFg";
	private final String NAMESPACE = "argo";
	
	public Object submitFlow(String template) {
		log.debug("save annotation from service");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + TOKEN);
		
		String url = "https://" + argoFlowConfig.getHost() + ":" + argoFlowConfig.getPort() + "/api/v1/workflows/" + NAMESPACE + "/submit";
			
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HttpEntity body = new HttpEntity(template ,headers);
		  
		ResponseEntity<Object> argoResult = restTemplate.exchange(url, HttpMethod.POST, body, Object.class);
		
		return argoResult.getBody();
	}	
}
