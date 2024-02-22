package com.test.post;

public class PoolsDto {
    private String startIpAddress;
    private String endIpAddress;

    public PoolsDto(String startIpAddress, String endIpAddress) {
        this.startIpAddress = startIpAddress;
        this.endIpAddress = endIpAddress;
    }

    // Getters and setters...


    public String getStartIpAddress() {
        return startIpAddress;
    }

    public void setStartIpAddress(String startIpAddress) {
        this.startIpAddress = startIpAddress;
    }

    public void setEndIpAddress(String endIpAddress) {
        this.endIpAddress = endIpAddress;
    }

    public String getEndIpAddress() {
        return endIpAddress;
    }
}