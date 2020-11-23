package cal.internshipmanager.service;

import cal.internshipmanager.model.Settings;
import cal.internshipmanager.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingsService {

    //
    // Dependencies
    //

    private final SettingsRepository settingsRepository;

    //
    // Cache
    //

    private Settings settingsCache;

    //
    // Constructors
    //

    @Autowired
    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    //
    // Private Method
    //

    private Settings findSettings() {

        if (settingsCache == null) {

            final List<Settings> settings = settingsRepository.findAll();

            if (settings.size() != 1)
                throw new IllegalStateException("No settings could be found!");

            settingsCache = settings.get(0);
        }

        return settingsCache;
    }

    //
    // Services
    //

    public void setSemester(String semester) {

        final Settings settings = findSettings();

        settings.setSemester(semester);

        settingsRepository.save(settings);
    }

    public String getSemester() {
        return findSettings().getSemester();
    }

    public void setRequireApproval(boolean requireApproval) {

        final Settings settings = findSettings();

        settings.setRequireApproval(requireApproval);

        settingsRepository.save(settings);
    }

    public boolean getRequireApproval() {
        return findSettings().getRequireApproval();
    }

}
