package org.machine_coding;

import org.machine_coding.commons.OsName;
import org.machine_coding.managers.DeviceReleaseManager;
import org.machine_coding.models.Device;

public class Main {
    public static void main(String[] args) {
        Device samsungS20 = Device.builder()
                .deviceOS(OsName.ANDROID)
                .deviceId(123)
                .osVersion("icecream")
                .build();

        Device ios15 = Device.builder()
                .deviceOS(OsName.IOS)
                .deviceId(100)
                .osVersion("ios-17")
                .build();

        DeviceReleaseManager deviceReleaseManager = DeviceReleaseManager.getInstance();
    }
}