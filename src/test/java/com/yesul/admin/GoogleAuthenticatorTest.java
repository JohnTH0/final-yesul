package com.yesul.admin;

import org.junit.jupiter.api.Test;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

class GoogleAuthenticatorTest {

    @Test
    void createSecretKeytest() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();

        GoogleAuthenticatorKey key = gAuth.createCredentials();
        System.out.println(key.getKey());
        }
    }

