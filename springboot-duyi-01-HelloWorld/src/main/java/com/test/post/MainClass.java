package com.test.post;

import com.test.post.PoolsDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainClass {
    public static void main(String[] args) {
        // Original address pool
        PoolsDto addressPool = new PoolsDto("1.0.0.1", "1.0.0.255");

        // Exclusion ranges
        Dhcp4ExcludePool exclude1 = new Dhcp4ExcludePool("1.0.0.100", "1.0.0.150");
        Dhcp4ExcludePool exclude2 = new Dhcp4ExcludePool("1.0.0.200", "1.0.0.230");

        List<Dhcp4ExcludePool> excludeRanges = Arrays.asList(exclude1, exclude2);

        // Initialize with the original pool
        List<PoolsDto> poolsDto = new ArrayList<>();
        poolsDto.add(addressPool);

        // Process each exclusion range
        for (Dhcp4ExcludePool excludePool : excludeRanges) {
            List<PoolsDto> newPoolsDto = new ArrayList<>();

            for (PoolsDto existingRange : poolsDto) {
                if (IpUtils.checkIPv4RangeConflict(excludePool.getStartIpAddress(), excludePool.getEndIpAddress(),
                        existingRange.getStartIpAddress(), existingRange.getEndIpAddress())) {
                    // Adjust existing range based on the exclusion range
                    if (IpUtils.compareIPv4(existingRange.getStartIpAddress(), excludePool.getStartIpAddress())) {
                        // Add the remaining part before the exclusion range start
                        if (IpUtils.compareIPv4(existingRange.getStartIpAddress(), excludePool.getStartIpAddress())) {
                            newPoolsDto.add(new PoolsDto(existingRange.getStartIpAddress(), excludePool.getStartIpAddress()));
                        }
                        if (IpUtils.compareIPv4(existingRange.getEndIpAddress(), excludePool.getEndIpAddress())) {
                            newPoolsDto.add(new PoolsDto(excludePool.getEndIpAddress(), existingRange.getEndIpAddress()));
                        }
                    } else if (IpUtils.compareIPv4(existingRange.getEndIpAddress(), excludePool.getEndIpAddress())) {
                        // Add the remaining part after the exclusion range end
                        if (IpUtils.compareIPv4(existingRange.getEndIpAddress(), excludePool.getEndIpAddress())) {
                            newPoolsDto.add(new PoolsDto(excludePool.getEndIpAddress(), existingRange.getEndIpAddress()));
                        }
                    }
                } else {
                    // No conflict; retain the existing range
                    newPoolsDto.add(existingRange);
                }
            }

            // Update poolsDto for the next iteration
            poolsDto = newPoolsDto;
        }

        // Print the final result
        for (PoolsDto resultRange : poolsDto) {
            System.out.println("Address Pool: " + resultRange.getStartIpAddress() + " - " + resultRange.getEndIpAddress());
        }
    }
}