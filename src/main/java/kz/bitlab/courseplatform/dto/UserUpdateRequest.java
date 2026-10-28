
package kz.bitlab.courseplatform.dto;

public record UserUpdateRequest(
        String username,
        String email,
        String firstName,
        String lastName
) {
}
