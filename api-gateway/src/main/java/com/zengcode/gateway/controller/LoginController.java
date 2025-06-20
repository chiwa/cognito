package com.zengcode.gateway.controller;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;
import com.zengcode.gateway.dto.LoginRequest;
import com.zengcode.gateway.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AWSCognitoIdentityProvider cognitoClient;

    private final String clientId = "1qrv1oou1pmh9693gglxxxxxxx";
    private final String clientSecret = "1mbc8422ikgqh38cja5q843impb196qevbkfxxxxxx"; // 👈 ต้องใส่ client secret ที่ได้จาก AWS Cognito
    private final String userPoolId = "ap-southeast-1_H7NOxxxxx";

    public LoginController() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(
                "AKIAXRGPDZUKICINCH6N", // 🟥 ควรย้ายไป config/env ใน Production
                "AnVZui8C7eI7zYRUNmDii5vGVHtXGxFzQ9nahCUl"
        );

        this.cognitoClient = AWSCognitoIdentityProviderClientBuilder.standard()
                .withRegion(Regions.AP_SOUTHEAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String secretHash = calculateSecretHash(request.getUsername(), clientId, clientSecret);

            AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                    .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                    .withClientId(clientId)
                    .withUserPoolId(userPoolId)
                    .addAuthParametersEntry("USERNAME", request.getUsername())
                    .addAuthParametersEntry("PASSWORD", request.getPassword())
                    .addAuthParametersEntry("SECRET_HASH", secretHash);

            AdminInitiateAuthResult result = cognitoClient.adminInitiateAuth(authRequest);
            AuthenticationResultType authResult = result.getAuthenticationResult();

            LoginResponse response = new LoginResponse(
                    authResult.getIdToken(),
                    authResult.getAccessToken(),
                    authResult.getRefreshToken(),
                    authResult.getExpiresIn(),
                    authResult.getTokenType()
            );

            return ResponseEntity.ok(response);

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body("User not found");
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Login failed: " + e.getMessage());
        }
    }

    private String calculateSecretHash(String username, String clientId, String clientSecret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(clientSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);
            mac.update(username.getBytes(StandardCharsets.UTF_8));
            byte[] rawHmac = mac.doFinal(clientId.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Unable to calculate secret hash", e);
        }
    }
}