package com.yesul.config;

import com.yesul.user.service.PrincipalDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    @ModelAttribute("receiverId")
    public Long receiverId(@AuthenticationPrincipal PrincipalDetails pd) {
        return pd != null ? pd.getUser().getId() : 0;
    }
}
