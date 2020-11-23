package cal.internshipmanager.service;

import cal.internshipmanager.model.Notification;
import cal.internshipmanager.model.User;
import cal.internshipmanager.repository.UserRepository;
import cal.internshipmanager.request.SendNotificationRequest;
import cal.internshipmanager.response.NotificationListResponse;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    //
    // Dependencies
    //

    private final UserRepository userRepository;

    //
    // Constructors
    //

    public NotificationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //
    // Services
    //

    public NotificationListResponse notifications(UUID userUniqueId)
    {
        User user = userRepository.findById(userUniqueId).orElse(null);

        NotificationListResponse response = new NotificationListResponse();

        response.setNotifications(user.getNotifications()
                .stream().map(NotificationListResponse::map).collect(Collectors.toList()));

        return response;
    }

    public void send(SendNotificationRequest request)
    {
        User user = userRepository.findById(request.getUserUniqueId()).orElse(null);

        Notification notification = new Notification();

        notification.setDate(new Date());
        notification.setType(request.getType());
        notification.setMessage(request.getMessage());

        user.getNotifications().add(notification);

        userRepository.save(user);
    }

}
