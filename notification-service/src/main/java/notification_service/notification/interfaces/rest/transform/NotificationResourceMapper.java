package notification_service.notification.interfaces.rest.transform;

import notification_service.notification.domain.models.aggregates.Notification;
import notification_service.notification.domain.models.commands.SendNotificationCommand;
import notification_service.notification.domain.models.valueobjects.NotificationType;
import notification_service.notification.interfaces.rest.resources.NotificationResponseResource;
import notification_service.notification.interfaces.rest.resources.SendNotificationRequestResource;

public class NotificationResourceMapper {

    public static SendNotificationCommand toCommand(SendNotificationRequestResource resource) {
        return new SendNotificationCommand(
            NotificationType.valueOf(resource.type().toUpperCase()),
            resource.message(),
            resource.userId(),
            resource.role(),
            resource.temperature(),
            resource.ph(),
            resource.hardwareStatus()
        );
    }

    public static NotificationResponseResource toResource(Notification notification) {
        return new NotificationResponseResource(
            notification.getId(),
            notification.getType().name(),
            notification.getMessage(),
            notification.getRecipient().userId(),
            notification.getTriggerData().temperature(),
            notification.getTriggerData().ph(),
            notification.getTriggerData().hardwareStatus(),
            notification.getCreatedAt()
        );
    }
}
