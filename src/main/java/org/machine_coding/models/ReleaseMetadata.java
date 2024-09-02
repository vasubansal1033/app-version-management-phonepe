package org.machine_coding.models;

import lombok.*;
import org.machine_coding.commons.OsName;

@AllArgsConstructor
@Data
@Builder
public class ReleaseMetadata {
    private int version;

    private int supportedOsVersion;
    private OsName supportedOS;

    private String releaseNotes;
}