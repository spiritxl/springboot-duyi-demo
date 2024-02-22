package com.test.post;

public class Dhcp4ExcludePool {
    private String startIpAddress;
    private String endIpAddress;

    public Dhcp4ExcludePool(String startIpAddress, String endIpAddress) {
        this.startIpAddress = startIpAddress;
        this.endIpAddress = endIpAddress;
    }

    public String getStartIpAddress() {
        return startIpAddress;
    }

    public void setStartIpAddress(String startIpAddress) {
        this.startIpAddress = startIpAddress;
    }

    public String getEndIpAddress() {
        return endIpAddress;
    }

    public void setEndIpAddress(String endIpAddress) {
        this.endIpAddress = endIpAddress;
    }


    // Getters and setters...
}
