package com.itesm.panoptimize.util;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.http.SdkHttpFullRequest;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.signer.Aws4Signer;
import software.amazon.awssdk.auth.signer.params.Aws4PresignerParams;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;


import java.net.URI;
import java.time.Instant;

public class AwsRequestSigner {
        private final Aws4Signer signer;
        private final Region region;

        public AwsRequestSigner() {
            this.signer = Aws4Signer.create();
            this.region = Region.US_EAST_1; // Ajusta la región según sea necesario
        }

        public SdkHttpFullRequest signRequest(URI endpointUri, SdkHttpMethod method, String serviceName) {
            Aws4PresignerParams params = Aws4PresignerParams.builder()
                    .awsCredentials(EnvironmentVariableCredentialsProvider.create().resolveCredentials())
                    .signingName("connect")
                    .signingRegion(region)
                    //.instant(Instant.now())  // Asegura que la fecha/tiempo actual está siendo usado
                    .build();

            SdkHttpFullRequest request = SdkHttpFullRequest.builder()
                    .method(method)
                    .uri(endpointUri)
                    .build();

            // Firma la solicitud
            return signer.sign(request, params);
        }
        //////////////////////////////////
//    private final Aws4Signer signer;
//    private final Region region;
//
//    public AwsRequestSigner() {
//        this.signer = Aws4Signer.create();
//        this.region = Region.US_EAST_1; // Adjust the region as needed
//    }
//
//    public SdkHttpFullRequest signRequest(URI endpointUri, SdkHttpMethod method, String serviceName) {
//        SdkHttpFullRequest request = SdkHttpFullRequest.builder()
//                .method(method)
//                .uri(endpointUri)
//                .build();
//
//        Aws4PresignerParams params = Aws4PresignerParams.builder()
//                .signingName(serviceName)
//                .signingRegion(region)
//                .build();
//
//        return signer.sign(request, params);
//    }
}