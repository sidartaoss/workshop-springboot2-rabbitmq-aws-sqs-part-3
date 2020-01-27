package com.rollingstone.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

@Configuration
public class AWSCredentialProvider {

	  @Value("${aws.access-key}")
	  protected String accessKey;

	  @Value("${aws.secret-key}")
	  protected String secretKey;

	  @Bean
	  @Primary
	  public AWSCredentialsProvider buildAWSCredentialsProvider() {
	    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
	    return new AWSStaticCredentialsProvider(awsCredentials);
	  }	
}
