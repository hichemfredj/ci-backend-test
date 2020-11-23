package cal.internshipmanager.controller;

import cal.internshipmanager.request.SendNotificationRequest;
import cal.internshipmanager.response.NotificationListResponse;
import cal.internshipmanager.service.NotificationService;
import cal.internshipmanager.validator.ExistingUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    //
    // Dependencies
    //

    private final NotificationService notificationService;

    //
    // Constructors
    //

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    //
    // Get
    //

    @PostMapping("notifications")
    public NotificationListResponse notifications(@Valid @ExistingUser UUID userUniqueId){
        return notificationService.notifications(userUniqueId);
    }

    //
    // Post
    //

    @PostMapping("send")
    public void send(@Valid @RequestBody SendNotificationRequest request){
        notificationService.send(request);
    }

}
