package cal.internshipmanager.service;

import cal.internshipmanager.model.Notification;
import cal.internshipmanager.model.User;
import cal.internshipmanager.repository.UserRepository;
import cal.internshipmanager.request.SendNotificationRequest;
import cal.internshipmanager.response.NotificationListResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NotificationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void sendNotification_validRequest() {

        // Arrange

        User user = new User();

        user.setUniqueId(UUID.randomUUID());

        List<Notification> notifications = new ArrayList<>();

        SendNotificationRequest request = new SendNotificationRequest();

        request.setUserUniqueId(user.getUniqueId());
        request.setMessage("TestMessage");
        request.setType("TestType");

        user.setNotifications(notifications);

        NotificationService service = new NotificationService(userRepository);

        when(userRepository.findById(user.getUniqueId())).thenReturn(Optional.of(user));

        // Act & Assert

        when(userRepository.save(any())).then(inv -> {

            User response = inv.getArgument(0);

            assertEquals(user.getUniqueId(), response.getUniqueId());
            assertFalse(response.getNotifications().isEmpty());
            assertEquals(request.getMessage(), response.getNotifications().get(0).getMessage());
            assertEquals(request.getType(), response.getNotifications().get(0).getType());
            assertNotNull(response.getNotifications().get(0).getDate());

            return null;
        });

        service.send(request);
    }

    @Test
    public void notifications_validRequest() {

        // Arrange

        User user = new User();

        user.setUniqueId(UUID.randomUUID());

        List<Notification> notifications = new ArrayList<>();

        Notification notification = new Notification();

        notification.setMessage("TestMessage");
        notification.setType("TestType");
        notification.setDate(new Date());

        notifications.add(notification);

        user.setNotifications(notifications);

        NotificationService service = new NotificationService(userRepository);

        when(userRepository.findById(user.getUniqueId())).thenReturn(Optional.of(user));

        // Act

        NotificationListResponse response = service.notifications(user.getUniqueId());

        // Assert

        assertEquals(1, response.getNotifications().size());

        NotificationListResponse.Notification rnotifcation = response.getNotifications().get(0);

        assertEquals(notification.getMessage(), rnotifcation.getMessage());
        assertEquals(notification.getType(), rnotifcation.getType());
        assertEquals(notification.getDate().getTime(), rnotifcation.getDate());
    }

}
