package org.machine_coding.managers;

import org.machine_coding.models.app_release.AndroidAppRelease;
import org.machine_coding.models.app_release.IosAppRelease;
import org.machine_coding.commons.OsName;
import org.machine_coding.exceptions.OsNotFoundException;
import org.machine_coding.models.app_release.AppRelease;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppReleaseManager {
    private Map<String, Map<OsName, List<AppRelease> >> appReleaseHistory; // appName -> os, list of apps
    private static AppReleaseManager appReleaseManager;

    public static AppReleaseManager getInstance() {
        if(appReleaseManager == null) {
            appReleaseManager = new AppReleaseManager();
        }

        return appReleaseManager;
    }


    public AppReleaseManager() {
        this.appReleaseHistory = new HashMap<>();
    }

    public Map<String, Map<OsName, List<AppRelease>>> getAppReleaseHistory() {
        return appReleaseHistory;
    }

    public void publishOrUpdateApp(
            String appName,
            int appVersion,
            OsName supportedOs,
            int supportedOsVersion,
            String releaseNotes
    ) throws OsNotFoundException {
        // If app is released
        if(!appReleaseHistory.containsKey(appName)) {
            appReleaseHistory.put(appName, new HashMap<>());
        }

        // If OS support for the app is released for the first time
        if(!appReleaseHistory.get(appName).containsKey(supportedOs)) {
            appReleaseHistory.get(appName).put(supportedOs, new ArrayList<>());
        }

        AppRelease appRelease = null;
        switch (supportedOs) {
            case ANDROID:
                appRelease = new AndroidAppRelease(
                        appName,
                        appVersion,
                        releaseNotes,
                        supportedOsVersion
                );
                break;
            case IOS:
                appRelease = new IosAppRelease(
                        appName,
                        appVersion,
                        releaseNotes,
                        supportedOsVersion
                );
                break;
            default:
                System.out.printf("[AppManager] OS %s: is not supported.\n");
                throw new OsNotFoundException("UNSUPPORTED_OS_PROVIDED");
        }

        appReleaseHistory
                .get(appName)
                .get(supportedOs)
                .add(appRelease);

        System.out.printf("[AppManager] Succesfully released %s:%s on OS %s\n", appName, supportedOsVersion, supportedOs);
    }
}
