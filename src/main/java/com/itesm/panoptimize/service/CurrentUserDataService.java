package com.itesm.panoptimize.service;

import com.itesm.panoptimize.util.AwsRequestSigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.http.SdkHttpFullRequest;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.services.connect.ConnectClient;
import software.amazon.awssdk.services.connectcontactlens.ConnectContactLensClient;

import java.net.URI;
import java.util.Map;

@Service
public class CurrentUserDataService {
    private final AwsRequestSigner awsRequestSigner;
    private final RestTemplate restTemplate;
    private final ConnectClient connectClient;
    private final ConnectContactLensClient connectContactLensClient;

    @Autowired
    public CurrentUserDataService(AwsRequestSigner awsRequestSigner, RestTemplate restTemplate,
                                  ConnectClient connectClient, ConnectContactLensClient connectContactLensClient) {
        this.awsRequestSigner = awsRequestSigner;
        this.restTemplate = restTemplate;
        this.connectClient = connectClient;
        this.connectContactLensClient = connectContactLensClient;
    }

    private void sentimentAnalysis() {
        
    }

    public Object getCurrentUserData(String instanceId, Map<String, Object> requestPayload) {
        // Construye la URI completa incluyendo el ID de la instancia
        //String urlString = "https://connect.us-east-1.amazonaws.com/metrics/userdata/7c78bd60-4a9f-40e5-b461-b7a0dfaad848";
        String urlString = "https://connect.us-east-1.amazonaws.com/metrics/userdata/" + instanceId;
        URI endpointUri = URI.create(urlString);
            System.out.println("URL: " + urlString);
        // Firmar la solicitud
        SdkHttpFullRequest signedRequest = awsRequestSigner.signRequest(endpointUri, SdkHttpMethod.POST, "connect");

        // Convertir SdkHttpFullRequest a HttpEntity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.putAll(signedRequest.headers());
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestPayload, headers);

        // Enviar la solicitud firmada
        try {
            System.out.println("URL firmado: " + urlString);
            return restTemplate.postForObject(urlString, requestEntity, Object.class);
        } catch (Exception e) {
            System.out.println("Error al recuperar datos del usuario: " + e.getMessage());
            throw new RuntimeException("Error al recuperar datos del usuario: " + e.getMessage(), e);
        }
    }

//    public Object getCurrentUserData(String instanceId, Map<String, Object> requestPayload) {
//        URI endpointUri = URI.create("https://your-service-endpoint");
//        SdkHttpFullRequest signedRequest = awsRequestSigner.signRequest(endpointUri, SdkHttpMethod.POST, "execute-api");
//
//        String url = "https://connect.us-east-1.amazonaws.com/metrics/userdata/" + instanceId;
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestPayload, headers);
//
//        try {
//            return restTemplate.postForObject(url, requestEntity, Object.class);
//        } catch (Exception e) {
//            throw new RuntimeException("Error al recuperar datos del usuario: " + e.getMessage(), e);
//        }
//    }
}