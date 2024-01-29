package org.cyberspeed.model;

import java.util.List;

public class WinCombination {
    private Double reward_multiplier;
    private String when;
    private Integer count;
    private String group;
    private List<List<String>> covered_areas;

    public WinCombination() {
    }

    public WinCombination(double rewardMultiplier, String when, Integer count, String group) {
        this.reward_multiplier = rewardMultiplier;
        this.when = when;
        this.count = count;
        this.group = group;
    }

    public WinCombination(double rewardMultiplier, String when, String group, List<List<String>> coveredArea) {
        this.reward_multiplier = rewardMultiplier;
        this.when = when;
        this.group = group;
        this.covered_areas = coveredArea;
    }

    public Double getReward_multiplier() {
        return reward_multiplier;
    }

    public void setReward_multiplier(Double reward_multiplier) {
        this.reward_multiplier = reward_multiplier;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<List<String>> getCovered_areas() {
        return covered_areas;
    }

    public void setCovered_areas(List<List<String>> covered_areas) {
        this.covered_areas = covered_areas;
    }
}
