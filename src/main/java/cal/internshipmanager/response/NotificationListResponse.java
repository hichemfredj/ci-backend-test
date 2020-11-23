package cal.internshipmanager.response;

import lombok.Data;

import java.util.List;

@Data
public class NotificationListResponse {

    //
    // Fields
    //

    private List<Notification> notifications;

    //
    // Inner classes & Enums
    //

    @Data
    public static class Notification {

        private long date;

        private String type;

        private String message;

    }

    //
    // Utils
    //

    public static Notification map(cal.internshipmanager.model.Notification from) {

        Notification notification = new Notification();

        notification.date = from.getDate().getTime();
        notification.type = from.getType();
        notification.message = from.getMessage();

        return notification;
    }
}
