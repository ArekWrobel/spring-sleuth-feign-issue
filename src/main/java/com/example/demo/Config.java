package com.example.demo;

import feign.Feign;
import feign.slf4j.Slf4jLogger;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.concurrent.TimeUnit;

@Configuration
public class Config
{
    @Bean
    public feign.okhttp.OkHttpClient httpClient()
    {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
        {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
                return new java.security.cert.X509Certificate[0];
            }
        } };

        HostnameVerifier noopHostnameVerifier = (s, sslSession) -> true;

        try
        {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(new KeyManager[0], trustAllCerts, new java.security.SecureRandom());

            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return new feign.okhttp.OkHttpClient(new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                                                                           .hostnameVerifier(noopHostnameVerifier)
                                                                           .writeTimeout(150, TimeUnit.SECONDS)
                                                                           .build());
        }
        catch (Exception e)
        {
            throw new RuntimeException("SSLContext not available", e);
        }
    }

    @Bean
    public TestService testService()
    {
        return Feign.builder()
                    .logger(new Slf4jLogger())
                    .client(httpClient())
                    .target(TestService.class, "http://localhost:8081");
    }
}
