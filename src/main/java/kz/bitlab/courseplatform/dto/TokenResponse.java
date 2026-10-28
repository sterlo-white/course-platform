package kz.bitlab.courseplatform.dto;

public record TokenResponse(String accessToken,
                            String refreshToken,
                            String tokenType,
                            long expiresIn,
                            long refreshExpiresIn) {
}
