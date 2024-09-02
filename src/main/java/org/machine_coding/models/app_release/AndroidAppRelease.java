package org.machine_coding.models.app_release;

import org.machine_coding.commons.OsName;
import org.machine_coding.models.ReleaseMetadata;

public class AndroidAppRelease extends AppRelease {

    public AndroidAppRelease(
            String appName,
            int appVersion,
            String releaseNotes,
            int supportedOsVersion
    ) {
        super(
                appName,
                ReleaseMetadata.builder()
                        .version(appVersion)
                        .releaseNotes(releaseNotes)
                        .supportedOS(OsName.ANDROID)
                        .supportedOsVersion(supportedOsVersion)
                        .build()
        );
    }
}