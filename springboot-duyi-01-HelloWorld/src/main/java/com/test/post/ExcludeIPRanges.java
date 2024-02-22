package com.test.post;

import java.util.ArrayList;
import java.util.List;

public class ExcludeIPRanges {
    public static void main(String[] args) {
        List<String> excludedRanges = new ArrayList<>();
        excludedRanges.add("192.168.1.19-192.168.1.60");
        excludedRanges.add("192.168.1.90-192.168.1.150");

        List<String> ipRange = generateIPRange("192.168.1.1", "192.168.255.255", excludedRanges);

        // 打印结果
        System.out.println("Final IP Range: " + ipRange);
    }

    private static List<String> generateIPRange(String startIP, String endIP, List<String> excludedRanges) {
        List<String> ipRange = new ArrayList<>();

        String currentIP = startIP;

        while (compareIP(currentIP, endIP) <= 0) {
            if (!isExcluded(currentIP, excludedRanges)) {
                ipRange.add(currentIP);
            }

            currentIP = getNextIP(currentIP);
        }

        return ipRange;
    }

    private static boolean isExcluded(String ip, List<String> excludedRanges) {
        for (String excludedRange : excludedRanges) {
            String[] range = excludedRange.split("-");
            String start = range[0].trim();
            String end = range[1].trim();

            if (compareIP(ip, start) >= 0 && compareIP(ip, end) <= 0) {
                return true; // IP在排除范围内
            }
        }
        return false; // IP不在排除范围内
    }

    private static int compareIP(String ip1, String ip2) {
        String[] ip1Parts = ip1.split("\\.");
        String[] ip2Parts = ip2.split("\\.");

        for (int i = 0; i < 4; i++) {
            int part1 = Integer.parseInt(ip1Parts[i]);
            int part2 = Integer.parseInt(ip2Parts[i]);

            if (part1 != part2) {
                return Integer.compare(part1, part2);
            }
        }

        return 0; // 两个IP相等
    }

    private static String getNextIP(String ip) {
        String[] parts = ip.split("\\.");
        int[] ipParts = new int[4];

        for (int i = 0; i < 4; i++) {
            ipParts[i] = Integer.parseInt(parts[i]);
        }

        // 增加IP的最后一部分
        ipParts[3]++;

        // 处理进位
        for (int i = 3; i > 0; i--) {
            if (ipParts[i] > 255) {
                ipParts[i] = 0;
                ipParts[i - 1]++;
            }
        }

        // 构造下一个IP
        return String.format("%d.%d.%d.%d", ipParts[0], ipParts[1], ipParts[2], ipParts[3]);
    }
}
