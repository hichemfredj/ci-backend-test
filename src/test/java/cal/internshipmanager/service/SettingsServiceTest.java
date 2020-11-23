package cal.internshipmanager.service;

import cal.internshipmanager.model.Settings;
import cal.internshipmanager.repository.SettingsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class SettingsServiceTest {

    @Mock
    private SettingsRepository settingsRepository;

    @Test
    public void getSemester_firstRequest(){

        // Arrange

        Settings settings = new Settings();

        settings.setSemester("TestSemester");

        SettingsService service = new SettingsService(settingsRepository);

        when(settingsRepository.findAll()).thenReturn(List.of(settings));

        // Act

        String semester = service.getSemester();

        // Assert

        assertEquals(settings.getSemester(), semester);
    }

    @Test
    public void getSemester_invalidSettingsAmount(){

        // Arrange

        SettingsService service = new SettingsService(settingsRepository);

        when(settingsRepository.findAll()).thenReturn(List.of());

        // Act & Assert

        assertThrows(IllegalStateException.class, () -> service.getSemester());
    }

    @Test
    public void setSemester_validRequest(){

        // Arrange

        Settings settings = new Settings();

        settings.setCreationTimestamp(new Date().getTime());
        settings.setSemester("TestSemester");

        SettingsService service = new SettingsService(settingsRepository);

        when(settingsRepository.findAll()).thenReturn(List.of(settings));

        // Act & Assert

        when(settingsRepository.save(any())).then(inv -> {

            Settings result = inv.getArgument(0);

            assertEquals(settings.getCreationTimestamp(), result.getCreationTimestamp());
            assertEquals("TestSemester2", result.getSemester());

            return null;
        });

        service.setSemester("TestSemester2");
    }

    @Test
    public void getRequireApproval_firstRequest(){

        // Arrange

        Settings settings = new Settings();

        settings.setRequireApproval(true);

        SettingsService service = new SettingsService(settingsRepository);

        when(settingsRepository.findAll()).thenReturn(List.of(settings));

        // Act

        Boolean requireApproval = service.getRequireApproval();

        // Assert

        assertEquals(settings.getRequireApproval(), requireApproval);
    }
    @Test
    public void setRequireApproval_validRequest(){

        // Arrange

        Settings settings = new Settings();

        settings.setRequireApproval(false);

        SettingsService service = new SettingsService(settingsRepository);

        when(settingsRepository.findAll()).thenReturn(List.of(settings));

        // Act & Assert

        when(settingsRepository.save(any())).then(inv -> {

            Settings result = inv.getArgument(0);

            assertEquals(settings.getRequireApproval(), result.getRequireApproval());

            return null;
        });

        service.setRequireApproval(true);
    }
}
