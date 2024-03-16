package io.oferto.argo.flow.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class ArgoFlowConfig {
    @Value("${argo-flow.host:localhost}")
    String host;

    @Value("${argo-flow.port:2746}")
    String port;
}
