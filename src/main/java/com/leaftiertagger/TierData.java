package com.leaftiertagger;

public class TierData {
    private final String minecraftUsername;
    private final String tier;
    private final String lastUpdated;

    public TierData(String minecraftUsername, String tier, String lastUpdated) {
        this.minecraftUsername = minecraftUsername;
        this.tier = tier;
        this.lastUpdated = lastUpdated;
    }

    public String getMinecraftUsername() {
        return minecraftUsername;
    }

    public String getTier() {
        return tier;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public String toString() {
        return "TierData{" +
                "minecraftUsername='" + minecraftUsername + '\'' +
                ", tier='" + tier + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                '}';
    }
}
