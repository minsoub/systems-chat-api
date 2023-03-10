package com.bithumbsystems.chat.api.core.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class AwsProperties {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.region.static}")
  private String region;

  @Value("${cloud.aws.ssm.endpoint}")
  private String ssmEndPoint;

  @Value("${cloud.aws.kms.endpoint}")
  private String kmsEndPoint;

  @Value("${cloud.aws.param-store.prefix}")
  private String prefix;

  @Value("${cloud.aws.param-store.doc-name}")
  private String paramStoreDocName;

  @Value("${cloud.aws.param-store.kms-name}")
  private String paramStoreKmsName;
  @Value("${cloud.aws.param-store.salt-name}")
  private String paramStoreSaltName;
  @Value("${cloud.aws.param-store.iv-name}")
  private String paramStoreIvName;
  @Value("${cloud.aws.param-store.auth-name}")
  private String paramStoreAuthName;
  @Value("${cloud.aws.param-store.crypto-name}")
  private String paramCryptoName;

  private String kmsKey;
  private String saltKey;
  private String ivKey;
  private String jwtSecretKey;
  private String cryptoKey;

}
