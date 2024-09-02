package org.machine_coding.models.app_release;

import org.machine_coding.commons.OsName;
import org.machine_coding.models.ReleaseMetadata;

public class IosAppRelease extends AppRelease {

    public IosAppRelease(
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
                        .supportedOS(OsName.IOS)
                        .supportedOsVersion(supportedOsVersion)
                        .build()
        );
    }
}