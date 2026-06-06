package notification_service.shared.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Extracts authenticated user identity from gateway-forwarded headers and stores it
 * in a thread-local {@link UserContext} for the duration of the request.
 *
 * Expected headers (set by the API gateway after authentication):
 * - X-User-Id
 * - X-User-Role
 *
 * If headers are absent, the context is left empty (e.g., for service-to-service webhooks
 * that carry identity in the request body).
 */
@Component
public class UserContextFilter extends OncePerRequestFilter {

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String userIdHeader = request.getHeader(USER_ID_HEADER);
            if (userIdHeader != null && !userIdHeader.isBlank()) {
                try {
                    UserContext.setUserId(Long.parseLong(userIdHeader));
                } catch (NumberFormatException ignored) {
                    // Invalid header format; leave context empty
                }
            }

            String userRoleHeader = request.getHeader(USER_ROLE_HEADER);
            if (userRoleHeader != null && !userRoleHeader.isBlank()) {
                UserContext.setUserRole(userRoleHeader);
            }

            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}
