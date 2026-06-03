package notification_service.notification.domain.models.valueobjects;

import java.math.BigDecimal;

public record TriggerSnapshot(
    BigDecimal temperature,
    BigDecimal ph,
    String hardwareStatus
) {
}
