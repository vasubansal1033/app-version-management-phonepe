package org.machine_coding.models.app_release;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.machine_coding.models.ReleaseMetadata;

@AllArgsConstructor
@Data
@Builder
public class AppRelease {
    private final String appName;
    private final ReleaseMetadata currentReleaseMetaData;
}