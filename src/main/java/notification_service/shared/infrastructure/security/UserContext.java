package notification_service.shared.infrastructure.security;

/**
 * Thread-local holder for the authenticated user identity forwarded by the API gateway.
 * Cleared automatically after each request by {@link UserContextFilter}.
 */
public class UserContext {

    private static final ThreadLocal<Long> currentUserId = new ThreadLocal<>();
    private static final ThreadLocal<String> currentUserRole = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        currentUserId.set(userId);
    }

    public static Long getUserId() {
        return currentUserId.get();
    }

    public static void setUserRole(String role) {
        currentUserRole.set(role);
    }

    public static String getUserRole() {
        return currentUserRole.get();
    }

    public static void clear() {
        currentUserId.remove();
        currentUserRole.remove();
    }
}
