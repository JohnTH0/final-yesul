package com.yesul.user.auth;

import java.util.Map;

public interface OauthUserInfo {
    Map<String, Object> getAttributes();
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
