package com.zengcode.gateway.user;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;

public class CognitoSetPasswordExample {
    public static void main(String[] args) throws InterruptedException {

        String userPoolId = "ap-southeast-1_xxxxxx";

        // 👇 กุญแจของ IAM ที่มีสิทธิ์จัดการ Cognito
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(
                "AKIAXRGPDZUKICIxxxxx",
                "AnVZui8C7eI7zYRUNmDii5vGVHtXxxxxx"
        );

        // 👇 Client สำหรับคุยกับ Cognito
        AWSCognitoIdentityProvider cognitoClient = AWSCognitoIdentityProviderClientBuilder.standard()
                .withRegion(Regions.AP_SOUTHEAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();

        // ✅ สร้างผู้ใช้ใหม่
        AdminCreateUserRequest createUserRequest = new AdminCreateUserRequest()
                .withUserPoolId(userPoolId)
                .withUsername("chiwa")
                .withUserAttributes(
                        new AttributeType().withName("email").withValue("chiwa@example.com"),
                        new AttributeType().withName("email_verified").withValue("true")
                )
                .withMessageAction(MessageActionType.SUPPRESS) // ❌ ไม่ส่ง Email
                .withTemporaryPassword("TempPass123!");

        AdminCreateUserResult createUserResult = cognitoClient.adminCreateUser(createUserRequest);
        System.out.println("✅ User created: " + createUserResult.getUser().getUsername());

        // 💤 รอ 2-3 วิ เพื่อให้ Cognito สร้าง user สมบูรณ์
        Thread.sleep(3000);

        // ✅ ตั้งรหัสผ่านถาวรให้ทันที
        AdminSetUserPasswordRequest setPasswordRequest = new AdminSetUserPasswordRequest()
                .withUserPoolId(userPoolId)
                .withUsername("chiwa")
                .withPassword("Abcd1234!")
                .withPermanent(true);

        cognitoClient.adminSetUserPassword(setPasswordRequest);
        System.out.println("🔐 Password set successfully for user chiwa.");
    }
}