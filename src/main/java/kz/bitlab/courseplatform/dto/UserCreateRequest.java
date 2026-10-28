
package kz.bitlab.courseplatform.dto;

public record UserCreateRequest(
        String username,
        String email,
        String password,
        String role
) {
}
