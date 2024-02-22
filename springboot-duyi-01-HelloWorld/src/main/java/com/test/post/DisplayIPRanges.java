package com.test.post;

import java.util.ArrayList;
import java.util.List;

public class DisplayIPRanges {
    public static void main(String[] args) {
        List<String> excludedRanges = new ArrayList<>();
//        excludedRanges.add("192.168.20.1-192.168.20.100");
//        excludedRanges.add("192.168.20.101-192.168.20.255");
//        excludedRanges.add("192.168.20.92-192.168.20.101");
//        excludedRanges.add("192.168.20.150-192.168.20.254");

        List<String> ipRanges = generateIPRanges("192.168.20.1", "192.168.20.255", excludedRanges);

        // 打印结果
        for (String ipRange : ipRanges) {
            System.out.println(ipRange);
        }
    }

//    private static List<String> generateIPRanges(String startIP, String endIP, List<String> excludedRanges) {
//        List<String> ipRanges = new ArrayList<>();
//
//        String currentIP = startIP;
//        String rangeStart = startIP;
//
//        while (compareIP(currentIP, endIP) < 0) {
//            if (isExcluded(currentIP, excludedRanges)) {
//                // 如果当前 IP 在排除范围内，结束当前范围并更新起始 IP
//                if (!rangeStart.equals(currentIP)) {
//                    ipRanges.add(rangeStart + "-" + getPreviousIP(currentIP));
//                }
//                rangeStart = getNextIP(currentIP);
//            }
//            currentIP = getNextIP(currentIP);
//        }
//
//        // 处理最后一个范围
//        if (!rangeStart.equals(endIP)) {
//            ipRanges.add(rangeStart + "-" + endIP);
//        }
//
//        return ipRanges;
//    }
private static List<String> generateIPRanges(String startIP, String endIP, List<String> excludedRanges) {
    List<String> ipRanges = new ArrayList<>();

    long currentIP = ipToLong(startIP);
    long end = ipToLong(endIP);

    while (currentIP <= end) {
        String currentIPStr = longToIP(currentIP);

        if (!isExcluded(currentIPStr, excludedRanges)) {
            long rangeEnd = currentIP;
            while (rangeEnd <= end && !isExcluded(longToIP(rangeEnd), excludedRanges)) {
                rangeEnd++;
            }
            ipRanges.add(currentIPStr + "-" + longToIP(rangeEnd - 1));
            currentIP = rangeEnd;
        } else {
            currentIP++;
        }
    }

    return ipRanges;
}

    private static long ipToLong(String ip) {
        String[] parts = ip.split("\\.");
        return (Long.parseLong(parts[0]) << 24)
                + (Long.parseLong(parts[1]) << 16)
                + (Long.parseLong(parts[2]) << 8)
                + Long.parseLong(parts[3]);
    }

    private static String longToIP(long ip) {
        return String.format("%d.%d.%d.%d",
                (ip >> 24) & 0xFF,
                (ip >> 16) & 0xFF,
                (ip >> 8) & 0xFF,
                ip & 0xFF);
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

//    private static String getPreviousIP(String ip) {
//        String[] parts = ip.split("\\.");
//        int[] ipParts = new int[4];
//
//        for (int i = 0; i < 4; i++) {
//            ipParts[i] = Integer.parseInt(parts[i]);
//        }
//
//        // 减小IP的最后一部分
//        ipParts[3]--;
//
//        // 处理借位
//        for (int i = 3; i > 0; i--) {
//            if (ipParts[i] < 0) {
//                ipParts[i] = 255;
//                ipParts[i - 1]--;
//            }
//        }
//
//        // 构造前一个IP
//        return String.format("%d.%d.%d.%d", ipParts[0], ipParts[1], ipParts[2], ipParts[3]);
//    }
    private static String getPreviousIP(String ip) {
        String[] parts = ip.split("\\.");
        int[] ipParts = new int[4];

        for (int i = 0; i < 4; i++) {
            ipParts[i] = Integer.parseInt(parts[i]);
        }

        // 减小IP的最后一部分
        ipParts[3]--;

        // 处理借位
        for (int i = 3; i > 0; i--) {
            if (ipParts[3] < 0) {
                ipParts[3] = 255;
                ipParts[i - 1]--;
            }
        }

        // 构造前一个IP
        return String.format("%d.%d.%d.%d", ipParts[0], ipParts[1], ipParts[2], ipParts[3]);
    }

}
