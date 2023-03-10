package com.bithumbsystems.chat.api.core.config;

import com.bithumbsystems.chat.api.core.config.property.AwsProperties;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsAsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.net.URI;

@Slf4j
@Getter
@Setter
@Configuration
@Profile("dev|qa|prod|eks-dev")
public class AwsConfig {

    private final AwsProperties awsProperties;

    private KmsAsyncClient kmsAsyncClient;

    public AwsConfig(AwsProperties awsProperties) {
        this.awsProperties = awsProperties;
    }

    @Bean
    public S3AsyncClient s3client() {
        return S3AsyncClient.builder()
            .region(Region.of(awsProperties.getRegion()))
            .build();
    }

    @PostConstruct
    public void init() {
        kmsAsyncClient = KmsAsyncClient.builder()
            .region(Region.of(awsProperties.getRegion()))
            .endpointOverride(URI.create(awsProperties.getKmsEndPoint()))
            .build();
    }

}