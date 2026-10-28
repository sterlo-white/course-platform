
package kz.bitlab.courseplatform.mapper;

import kz.bitlab.courseplatform.dto.TokenResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthMapper {

    public TokenResponse toTokenResponse(Map<String, Object> response) {
        return new TokenResponse(
                (String) response.get("access_token"),
                (String) response.get("refresh_token"),
                (String) response.get("token_type"),
                ((Number) response.get("expires_in")).longValue(),
                ((Number) response.get("refresh_expires_in")).longValue()
        );
    }
}
