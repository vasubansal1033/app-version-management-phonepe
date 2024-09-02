package org.machine_coding.commons;

public enum RolloutStrategy {
    BETA("beta"),
    PERCENTAGE("percentage");

    public String label;

    RolloutStrategy(String label) {
        this.label = label;
    }
}
