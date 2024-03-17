package io.oferto.argo.flow.configure;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import org.springframework.core.io.Resource;

@Configuration
public class RestTemplateConfig {
    @Value("${trust.store}")
    private Resource trustStore;

    @Value("${trust.store.password}")
    private String trustStorePassword; 

    /*@Bean
    RestTemplate getRestTemplateWithValidation() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, MalformedURLException, IOException {
		 SSLContext sslContext = new SSLContextBuilder()
				 .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
				 .build();
		 
		 SSLConnectionSocketFactory sslConFactory = new SSLConnectionSocketFactory(sslContext);
		 
		 HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslConFactory)
                .build();
		 
		 CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
		 
		 ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		 
        return new RestTemplate(requestFactory);
	}*/
    
    @Bean
    RestTemplate getRestTemplateWithoutValidation() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, MalformedURLException, IOException {
    	// Create SSL context to trust all certificates
        SSLContext sslContext = SSLContext.getInstance("TLS");
        
        // Define trust managers to accept all certificates
        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
            // Method to check client's trust - accepting all certificates
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            }

            // Method to check server's trust - accepting all certificates
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            }

            // Method to get accepted issuers - returning an empty array
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};
        
        // Initialize SSL context with the defined trust managers
        sslContext.init(null, trustManagers, null);

        // Disable SSL verification for RestTemplate
        
        // Set the default SSL socket factory to use the custom SSL context
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        
        // Set the default hostname verifier to allow all hostnames
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        // Create a RestTemplate with a custom request factory
        
        // Build RestTemplate with SimpleClientHttpRequestFactory
        RestTemplate restTemplate = new RestTemplateBuilder()
        		.requestFactory(SimpleClientHttpRequestFactory.class)
                .build();

        return restTemplate; // Return the configured RestTemplate        
    }
}
