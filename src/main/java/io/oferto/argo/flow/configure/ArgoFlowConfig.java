package io.oferto.argo.flow.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
@ConfigurationProperties("argo-flow")
public class ArgoFlowConfig {
    @Value("${host:localhost}")
    String host;

    @Value("${port:2746}")
    String port;
}
