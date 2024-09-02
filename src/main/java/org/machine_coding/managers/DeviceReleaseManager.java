package org.machine_coding.managers;

import lombok.Getter;
import lombok.Setter;
import org.machine_coding.commons.Constants;
import org.machine_coding.commons.OsName;
import org.machine_coding.commons.RolloutStrategy;
import org.machine_coding.exceptions.OsNotFoundException;
import org.machine_coding.exceptions.UnsupportedRolloutStrategy;
import org.machine_coding.models.app_release.AppRelease;
import org.machine_coding.models.Device;
import org.machine_coding.models.ReleaseMetadata;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DeviceReleaseManager {
    private static List<Device> deviceList;
    private static List<Device> betaDeviceList;
    private static AppReleaseManager appReleaseManager;

    private static DeviceReleaseManager deviceReleaseManager;

    public DeviceReleaseManager(
    ) {
        deviceList = new ArrayList<>();
        betaDeviceList = new ArrayList<>();
        appReleaseManager = AppReleaseManager.getInstance();
    }

    public static DeviceReleaseManager getInstance() {
        if(deviceReleaseManager == null) {
            deviceReleaseManager = new DeviceReleaseManager();
        }

        return deviceReleaseManager;
    }

    public void addDevice(Device device) {
        deviceList.add(device);
    }

    public void addBetaDevice(Device device) {
        betaDeviceList.add(device);
    }

    public void removeBetaDevice(Device device) {
        int deviceId = device.getDeviceId();

        betaDeviceList = betaDeviceList.stream().filter(
                currentDevice -> currentDevice.getDeviceId() != deviceId
        ).collect(Collectors.toList());
    }

    public List<Device> getDeviceList() {
        return Collections
                .unmodifiableList(deviceList);
    }

    public List<Device> getBetaDeviceList() {
        return Collections
                .unmodifiableList(betaDeviceList);
    }

    public void uploadNewVersion(AppRelease appRelease) throws OsNotFoundException {
        ReleaseMetadata currentReleaseMetaData = appRelease.getCurrentReleaseMetaData();
        appReleaseManager.publishOrUpdateApp(
                appRelease.getAppName(),
                currentReleaseMetaData.getVersion(),
                currentReleaseMetaData.getSupportedOS(),
                currentReleaseMetaData.getSupportedOsVersion(),
                currentReleaseMetaData.getReleaseNotes()
        );
    }

    public void releaseVersion(
            String appName,
            RolloutStrategy rolloutStrategy
    ) throws UnsupportedRolloutStrategy {
        switch (rolloutStrategy) {
            case PERCENTAGE:
                int rolloutSize = Constants.ROLLOUT_PERCENTAGE*deviceList.size()/100;

                for(int i=0; i<rolloutSize; i++) {
                    rolloutUpdate(appName, deviceList.get(i));
                }
            case BETA:
                betaDeviceList.stream().forEach(
                        device -> {
                            rolloutUpdate(appName, device);
                        }
                );
            default:
                System.out.println("Received unsupported rollout strategy " + rolloutStrategy);
                throw new UnsupportedRolloutStrategy(
                        Constants.UNSUPPORTED_ROLLOUT_STRATEGY
                );
        }
    }

    private void rolloutUpdate(String appName, Device device) {
        List<AppRelease> appReleaseHistory = appReleaseManager
                .getAppReleaseHistory()
                .get(appName)
                .get(device.getDeviceOS());

        AppRelease latestAppRelease = appReleaseHistory.get(appReleaseHistory.size()-1);
        if(checkForUpdates(device, latestAppRelease)) {
            if(checkForInstall(device, latestAppRelease)) {
                device.installApp(appName);
            } else {
                device.updateApp(appName);
            }
        }
    }

    private byte[] createUpdatePatch(AppRelease appRelease, int fromVersion, int toVersion) {
        return String.format("%s-%d->%d", appRelease.getAppName(), fromVersion, toVersion).getBytes(StandardCharsets.UTF_8);
    }

    private boolean isAppVersionSupported(Device device, AppRelease appRelease) {
        OsName deviceOS = device.getDeviceOS();
        OsName releaseOs = appRelease.getCurrentReleaseMetaData().getSupportedOS();

        if(!deviceOS.equals(releaseOs)) {
            System.out.println("[DeviceReleaseManager] OS are not supported");
        }

        return checkForUpdates(device, appRelease);
    }

    private boolean checkForInstall(Device device, AppRelease appRelease) {
        OsName deviceOS = device.getDeviceOS();
        OsName releaseOs = appRelease.getCurrentReleaseMetaData().getSupportedOS();

        if(!deviceOS.equals(releaseOs)) {
            System.out.println("[DeviceReleaseManager] OS are not supported");
            return false;
        }

        if(device.getAppVersionMap().containsKey(appRelease.getAppName())) {
            System.out.println("[DeviceReleaseManager] App is already installed");
            return false;
        }

        return true;
    }

    private boolean checkForUpdates(Device device, AppRelease appRelease) {
        // check if device has the app installed
        if(!device.getAppVersionMap().containsKey(appRelease.getAppName())) {
            System.out.println("[DeviceReleaseManager] Device doesn't have the app installed");
            return false;
        }

        String appName = appRelease.getAppName();

        int currentVersion = device
                .getAppVersionMap()
                .getOrDefault(appName, -1);

        int appReleaseVersion = appRelease.getCurrentReleaseMetaData().getVersion();

        return currentVersion < appReleaseVersion;
    }


}
