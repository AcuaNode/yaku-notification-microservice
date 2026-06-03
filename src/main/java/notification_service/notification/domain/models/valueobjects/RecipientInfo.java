package notification_service.notification.domain.models.valueobjects;

public record RecipientInfo(
    Long userId,
    String role
) {
    public RecipientInfo {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be valid");
        }
    }
}
