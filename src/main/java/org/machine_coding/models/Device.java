package org.machine_coding.models;

import lombok.Builder;
import lombok.Data;
import org.machine_coding.commons.OsName;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Device {
    private int deviceId;
    private OsName deviceOS;
    private String osVersion;

    private Map<String, Integer> appVersionMap = new HashMap<>();

    public boolean installApp(String appName) {
        System.out.println("Installed app!" + appName);
        appVersionMap.put(appName, 1);

        return true;
    }

    public boolean updateApp(String appName) {
        System.out.println("Updated app!" + appName);
        appVersionMap.put(appName, appVersionMap.get(appName) + 1);

        return true;
    }

    public String uploadFile(String fileContent) {
        System.out.println("Uploaded file" + fileContent);
        return String.format("http://test_host/%d", Math.random()*100);
    }

    public String getFile(String fileUrl) {
        return "lorem ipsum";
    }
}
