package com.test.post;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.util.IPAddressUtil;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP地址
 */
public class IpUtils {
    private static final Logger logger = LoggerFactory.getLogger(IpUtils.class);

    private final static String IPV4 = "^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$";
    private final static String IP_MASK = "^((128|192)|2(24|4[08]|5[245]))(\\.(0|(128|192)|2((24)|(4[08])|(5[245])))){3}$";

    /**
     * 判断字符串是否为IPV4地址格式
     *
     * @param ip 要验证的ip字符串
     * @return 是 true/ 否 false
     */
    public static boolean isIp(String ip) {
        if (ip == null) {
            return false;
        }
        Pattern patt = Pattern.compile(IPV4);
        return patt.matcher(ip).matches();
    }

    /**
     * 判断字符串是否为IPV4 子网掩码格式
     *
     * @param mask 要验证的子网掩码字符串
     * @return 是 true/ 否 false
     */
    public static boolean isIpMask(String mask) {
        if (mask == null) {
            return false;
        }
        Pattern patt = Pattern.compile(IP_MASK);
        return patt.matcher(mask).matches();
    }

    /**
     * 获取IP地址或掩码二进制数组
     *
     * @param ip IP或子网掩码
     * @return 二进制数组如[11111111, 11111111, 11111111, 11111111]
     */
    public static String[] getIpBinary(String ip) {
        String[] strs = ip.split("\\.");
        for (int i = 0; i < 4; i++) {
            strs[i] = Integer.toBinaryString(Integer.parseInt(strs[i]));
            if (strs[i].length() < 8) {
                StringBuilder zero = new StringBuilder();
                for (int j = 0; j < 8 - strs[i].length(); j++) {
                    zero.append("0");
                }
                strs[i] = zero + strs[i];
            }
        }
        return strs;
    }

    /**
     * 将二进制字符串数组转换为byte数组,长度由第一个值的长度决定
     *
     * @param binaryStrArr 二进制数组
     * @return 如[1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0]
     * @throws ArrayIndexOutOfBoundsException 如果数组二进制字符串长度不同,将会抛出异常
     */
    public static byte[] toBinary(String[] binaryStrArr) {
        int bl = binaryStrArr[0].length();
        byte[] bytes = new byte[bl * binaryStrArr.length];
        for (int i = 0; i < binaryStrArr.length; i++) {
            for (int j = 0; j < bl; j++) {
                bytes[i * bl + j] = (byte) (binaryStrArr[i].charAt(j) == '1' ? 1 : 0);
            }
        }
        return bytes;
    }

    /**
     * 对二进制数组增加指定值
     * <p>如果增加的值超出此数组长度二进制最大表示值, 数组将重置为0, 从0开始增加</p>
     *
     * @param binaryArray 二进制数组值应当全为1或0
     * @param plus        增加的数值10进制
     */
    public static void binaryArrayPlus(byte[] binaryArray, int plus) {
        binaryArrayPlus(binaryArray, binaryArray.length - 1, plus);
    }

    /**
     * 对二进制数组增加指定值
     * <p>如果增加的值超出此数组长度二进制最大表示值, 数组将重置为0, 从0开始增加</p>
     *
     * @param binaryArray 二进制数组值应当全为1或0
     * @param index       下标
     * @param plus        增加的数值10进制
     */
    private static void binaryArrayPlus(byte[] binaryArray, int index, int plus) {
        if (index < 0) {
            binaryArray[0] = 0;
            return;
        }
        binaryArray[index] = (byte) (binaryArray[index] + 1);
        plus--;
        //如果进位,则递归进位
        if (binaryArray[index] > 1) {
            binaryArrayPlus(binaryArray, index - 1, 1);
            binaryArray[index] = 0;
        }
        //如果增加的数超过1
        if (plus > 0) {
            binaryArrayPlus(binaryArray, index, plus);
        }
    }

    /**
     * 获取局域网内的所有IP, 包含参数地址, 包含首尾地址
     *
     * @param ip   用作查找基础的IP,返回此IP网段的地址列表
     * @param mask 子网掩码
     * @return IP list 或 null, 如果地址非法则返回null
     */
    public static List<String> getLocalAreaIpList(String ip, String mask) {
        return getLocalAreaIpList(ip, mask, false);
    }

    /**
     * 获取局域网内的所有IP, 包含首尾地址
     *
     * @param ip             用作查找基础的IP,返回此IP网段的地址列表
     * @param mask           子网掩码
     * @param containParamIp 返回结果是否包含传入参数的IP
     * @return IP list 或 null, 如果地址非法则返回null
     */
    public static List<String> getLocalAreaIpList(String ip, String mask, boolean containParamIp) {
        return getLocalAreaIpList(ip, mask, containParamIp, false);
    }

    /**
     * 获取局域网内的所有IP
     *
     * @param ip                   用作查找基础的IP,返回此IP网段的地址列表
     * @param mask                 子网掩码
     * @param containParamIp       返回结果是否包含传入参数的IP
     * @param ignoreFirstAndLastIp 是否忽略首尾IP,(网段地址与广播地址)
     * @return IP list 或 null, 如果地址非法则返回null
     */
    public static List<String> getLocalAreaIpList(String ip, String mask, boolean containParamIp, boolean ignoreFirstAndLastIp) {
        if (!isIp(ip) || !isIpMask(mask)) {
            return null;//非法ip或子网掩码
        }
        String[] maskBinary = getIpBinary(mask);//子网掩码二进制数组
        //[11000000, 10101000, 00000000, 11111110]
        String[] ipBinary = getIpBinary(ip);//IP地址二进制数组
        //取同网段部分
        byte[] maskArr = toBinary(maskBinary);//二进制掩码数组
        byte[] ipArr = toBinary(ipBinary);//二进制IP数组
        int maskLen = 0;//子网掩码长度
        for (int i = 0; i < maskArr.length; i++) {
            maskLen += maskArr[i];
        }
//        int maskNumberLen = maskLen % 8;//子网位数,若为0 则8位全为主机号
//        System.out.println("子网号位数:" + maskNumberLen);
        int hostNumberLen = 32 - maskLen;//主机IP位数
//        System.out.println("主机号位数:" + hostNumberLen);
        int maxHost = 1 << hostNumberLen;
//        System.out.println("支持主机个数:" + maxHost);
        byte[] mod = new byte[32];//同网段二进制数组
        for (int i = 0; i < 32; i++) {
            mod[i] = (byte) (maskArr[i] & ipArr[i]);
        }
        List<String> ipList = new ArrayList<>(maxHost);
        StringBuilder genIp = new StringBuilder();//生成的IP
        for (int i = 0; i < maxHost; i++) {
            //转换为IP地址
            int decimal = 0;
            for (int j = 0; j < 32; j++) {
                decimal += mod[j] << (7 - j % 8);
                if (j != 0 && (j + 1) % 8 == 0) {
                    if (genIp.length() == 0) {
                        genIp.append(decimal);
                    } else {
                        genIp.append(".").append(decimal);
                    }
                    decimal = 0;
                }
            }
            binaryArrayPlus(mod, 1);//从0开始增加maxHost次
//            System.out.println(genIp);//生成的IP
            String generateIp = genIp.toString();
            genIp.delete(0, genIp.length());//清空
            if (ignoreFirstAndLastIp && (i == 0 || i == maxHost - 1)) {
                continue;//跳过首位地址
            }
            if (containParamIp && generateIp.equals(ip)) {
                continue;//跳过相同地址
            }
            ipList.add(generateIp);
        }
        return ipList;
    }

    /**
     * IP地址转换为一个long整数
     *
     * @param ip XXX.XXX.XXX.XXX
     * @return long
     */
    public static long ipToNumeric(String ip) {
        String[] ips = ip.split("\\.");
        long ipNum = 0;
        for (int i = 0; i < ips.length; i++) {
            ipNum += (Long.parseLong(ips[i]) << 8 * (3 - i));
        }
        return ipNum;
    }

    /**
     * 将十进制整数形式转换成127.0.0.1形式的ip地址
     *
     * @param longIp 十进制ip
     * @return x.x.x.x格式
     */
    public static String longToIP(long longIp) {
        StringBuffer sb = new StringBuffer("");
        // 直接右移24位
        sb.append((longIp >>> 24));
        sb.append(".");
        // 将高8位置0，然后右移16位
        sb.append((longIp & 0x00FFFFFF) >>> 16);
        sb.append(".");
        // 将高16位置0，然后右移8位
        sb.append((longIp & 0x0000FFFF) >>> 8);
        sb.append(".");
        // 将高24位置0
        sb.append((longIp & 0x000000FF));
        return sb.toString();
    }

    /**
     * 获取IP总数
     *
     * @param mask XX
     * @return
     */
    public static int getIpCount(String mask) {
        return BigDecimal.valueOf(Math.pow(2, 32 - Integer.parseInt(mask))).setScale(0, RoundingMode.DOWN).intValue();//IP总数，去小数点
    }

    /**
     * 获取掩码
     *
     * @param maskLength 网络ID位数  XX
     * @return XXX.XXX.XXX.XXX
     */
    public static String getMask(int maskLength) {
        int binaryMask = 0xFFFFFFFF << (32 - maskLength);
        StringBuilder sb = new StringBuilder();
        for (int shift = 24; shift > 0; shift -= 8) {
            sb.append((binaryMask >>> shift) & 0xFF);
            sb.append(".");
        }
        sb.append(binaryMask & 0xFF);
        return sb.toString();
    }

    /**
     * 检查ip是否冲突
     * true 唯一（默认） 、false 不唯一
     *
     * @return
     */
    public static Boolean compareIP(Long ip1, Long ip2, Long ip3, Long ip4) {
        for (int i = 0; i < 4; i++) {
            if ((ip1 <= ip3) && (ip2 >= ip3 && ip2 < ip4)) {
                return false;
            } else if ((ip1 >= ip3) && (ip1 <= ip4 && ip2 > ip4)) {
                return false;
            } else if (ip1 <= ip3 && ip2 >= ip4) {
                return false;
            } else if (ip1 >= ip3 && ip2 <= ip4) {
                return false;
            }
        }
        return true;
    }

    /**
     * 16进制编码转为十进制
     *
     * @param str 0x...
     * @return long
     */
    public static long toTen(String str) {
        String code = str.substring(2, str.length());
        BigInteger bigint = new BigInteger(code, 16);
        return bigint.longValue();
    }

    /**
     * 获取ipPool的所有IP
     *
     * @param ipPool x.x.x.x/x
     * @return IP list 或 null, 如果地址非法则返回null
     */
    public static List<String> getIpList(String ipPool) {
        List<String> ipList = new ArrayList<>();
        //1.判断是ip段还是单个ip
        String[] ip = ipPool.split("\\/");
        String[] ipNum = ip[0].split("\\.");
        if (!isSingleIp(ipPool)) {
            //ip段
            //获取掩码
            String mask = IpUtils.getMask(Integer.parseInt(ip[1]));
            //获取ipList
            ipList = IpUtils.getLocalAreaIpList(ip[0], mask, false, false);
        } else {
            //单个ip
            ipList.add(ip[0]);
        }

        return ipList;
    }

    /**
     * 判断是否是单个Ip
     *
     * @param ipPool x.x.x.x/x
     * @return ture 是，false 不是
     */
    public static Boolean isSingleIp(String ipPool) {
        boolean result = false;
        //1.判断是ip段还是单个ip
        String[] ip = ipPool.split("\\/");
        int mask = Integer.parseInt(ip[1]);
        if (mask == 32) {
            return true;
        }
        String[] ipBinary = getIpBinary(ip[0]);//IP地址二进制数组
        byte[] ipArr = toBinary(ipBinary);//二进制IP数组
        //当主机位全是0时，为网段，其余全是单个IP
        for (int i = mask; i < ipArr.length; i++) {
            if (ipArr[i] == 1) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 判断某IP是否在某一个IP段内
     *
     * @param ip   IP  x.x.x.x
     * @param cidr IP段  x.x.x.x/x
     * @return true/false
     */
    public static boolean isInRange(String ip, String cidr) {
        String[] ips = ip.split("\\.");
        int ipAddr = (Integer.parseInt(ips[0]) << 24)
                | (Integer.parseInt(ips[1]) << 16)
                | (Integer.parseInt(ips[2]) << 8) | Integer.parseInt(ips[3]);
        int type = Integer.parseInt(cidr.replaceAll(".*/", ""));
        int mask = 0xFFFFFFFF << (32 - type);
        String cidrIp = cidr.replaceAll("/.*", "");
        String[] cidrIps = cidrIp.split("\\.");
        int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24)
                | (Integer.parseInt(cidrIps[1]) << 16)
                | (Integer.parseInt(cidrIps[2]) << 8)
                | Integer.parseInt(cidrIps[3]);

        return (ipAddr & mask) == (cidrIpAddr & mask);
    }

    /**
     * 根据 ip/掩码位 计算IP段的起始IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 起始IP的字符串表示
     */
    public static String getBeginIpStr(String ip, String maskBit) {
        return longToIP(getBeginIpLong(ip, maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的起始IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 起始IP的Long表示
     */
    public static Long getBeginIpLong(String ip, String maskBit) {
        return ipToNumeric(ip) & ipToNumeric(getMask(Integer.parseInt(maskBit)));
    }

    /**
     * 根据 ip/掩码位 计算IP段的终止IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 终止IP的字符串表示
     */
    public static String getEndIpStr(String ip, String maskBit) {
        return longToIP(getEndIpLong(ip, maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的终止IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return 终止IP的Long表示
     */
    public static Long getEndIpLong(String ip, String maskBit) {
        return getBeginIpLong(ip, maskBit) + getIpCount(maskBit) - 1L;
    }

    /**
     * 一个大的IP池  根据掩码位平均分割成多个IP池   如：  10.0.0.0/8   --->  10.0.0.0/30 , 10.0.0.4/30 , ...
     *
     * @param ipPool  给定的IP池，如10.0.0.0/8
     * @param maskBit 给定的掩码位，如30
     * @return IP池  List  10.0.0.0/30 , 10.0.0.4/30 , ...
     */
    public static List<String> ipPoolToMultiIpPool(String ipPool, String maskBit) {

        List<String> ipPoolList = new ArrayList<>();
        String[] split = ipPool.split("\\/");
        long beginIpLong = getBeginIpLong(split[0], split[1]);
        int total = getIpCount(split[1]);
        int ipCount = getIpCount(maskBit);
        for (int i = 0; i < total / ipCount; i++) {
            ipPoolList.add(longToIP(beginIpLong + (long) i * ipCount) + "/" + maskBit);
        }

        return ipPoolList;
    }

    /**
     * 判断IP段是否冲突, 冲突为false, 不冲突为true
     *
     * @param ipPool1 ip段1
     * @param ipPool2 ip段2
     * @return 冲突为false, 不冲突为true
     */
    public static boolean checkIpPoolConflict(String ipPool1, String ipPool2) {
        List<String> ipList1 = IpUtils.getIpList(ipPool1);
        List<String> ipList2 = IpUtils.getIpList(ipPool2);
        String a1 = ipList1.get(0);
        String a2 = ipList1.get(ipList1.size() - 1);
        String b1 = ipList2.get(0);
        String b2 = ipList2.get(ipList2.size() - 1);
        long a1num = IpUtils.ipToNumeric(a1);
        long a2num = IpUtils.ipToNumeric(a2);
        long b1num = IpUtils.ipToNumeric(b1);
        long b2num = IpUtils.ipToNumeric(b2);
        if (a1num >= b1num && a1num <= b2num) {
            return false;
        }
        return a1num >= b1num || a2num < b2num;
    }

    /**
     * 判断IP地址是否为内网IP,
     *
     * @param ip 例如 ： 192.168.3.211  ,  202.3.2.1
     * @return 是否为内网IP, true/false
     */
    public static boolean internalIp(String ip) {
        byte[] addr = IPAddressUtil.textToNumericFormatV4(ip);
        return internalIp(addr);
    }

    public static boolean internalIp(byte[] addr) {
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        //10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        //172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        //192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true;
                }
            case SECTION_5:
                if (b1 == SECTION_6) {
                    return true;
                }
            default:
                return false;

        }
    }




    /**
     * 校验端口号是否合法有效
     *
     * @param port 端口号
     * @return true or false
     */
    public static Boolean checkPort(String port) {
        return port.matches("^\\d{1,5}$") && Integer.parseInt(port) >= 1 && Integer.parseInt(port) <= 65535;
    }


    /**
     * 校验ipv6
     *
     * @param ipAddress ip
     * @return true or false
     */
    public static boolean isValidIPv6(String ipAddress) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            // 判断是否为 IPv6 地址
            return inetAddress instanceof Inet6Address;
        } catch (UnknownHostException e) {
            return false; // 无效的地址冲突
        }
    }

    /**
     * @description: 校验ipv6地址范围是否冲突
     * @param startIp1 开待校验始地址
     * @param endIp1 待校验结束地址
     * @param startIp2 开始地址
     * @param endIp2 结束地址
     * @return boolean
     * @author: wangyang
     * @date: 2023/12/25 16:47
     */
    public static boolean checkIPv6RangeConflict(String startIp1, String endIp1, String startIp2, String endIp2) {
        BigInteger startIpNum1 = convertIPv6ToBigInteger(startIp1);
        BigInteger startIpNum2 = convertIPv6ToBigInteger(startIp2);
        BigInteger endIpNum1 = convertIPv6ToBigInteger(endIp1);
        BigInteger endIpNum2 = convertIPv6ToBigInteger(endIp2);
        return (startIpNum1.compareTo(startIpNum2) >= 0 && startIpNum1.compareTo(endIpNum2) <= 0)
                || (endIpNum1.compareTo(startIpNum2) >= 0 && endIpNum1.compareTo(endIpNum2) <= 0);
    }

    public static boolean checkIPv4RangeConflict(String startIp1, String endIp1, String startIp2, String endIp2) {
        BigInteger startIpNum1 = BigInteger.valueOf(convertIpv4ToInt(startIp1));
        BigInteger startIpNum2 = BigInteger.valueOf(convertIpv4ToInt(startIp2));
        BigInteger endIpNum1 = BigInteger.valueOf(convertIpv4ToInt(endIp1));
        BigInteger endIpNum2 = BigInteger.valueOf(convertIpv4ToInt(endIp2));
        return (startIpNum1.compareTo(startIpNum2) >= 0 && startIpNum1.compareTo(endIpNum2) <= 0)
                || (endIpNum1.compareTo(startIpNum2) >= 0 && endIpNum1.compareTo(endIpNum2) <= 0);
    }

    private static String octetsToBinaryString(String[] octets) {
        StringBuilder binaryString = new StringBuilder();
        for (String octet : octets) {
            int value = Integer.parseInt(octet);
            binaryString.append(String.format("%8s", Integer.toBinaryString(value)).replace(' ', '0'));
        }
        return binaryString.toString();
    }


    /**
     * 校验mac地址
     *
     * @param mac mac地址
     * @return true or false
     */
    public static boolean isValidMacAddress(String mac) {
        String regex = "([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})";

        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);

        // 创建Matcher对象
        Matcher matcher = pattern.matcher(mac);

        // 执行匹配并返回结果
        return matcher.matches();
    }

    /**
     * 校验子网掩码输入格式是否正确
     *
     * @param subnetMask 子网掩码
     * @return true or false
     */
    public static boolean isValidSubnetMask(String subnetMask) {
        // 切分子网掩码
        String[] octets = subnetMask.split("\\.");

        // 检查是否有四个部分
        if (octets.length != 4) {
            return false;
        }

        // 检查每个部分是否是合法的整数
        for (String octet : octets) {
            try {
                int value = Integer.parseInt(octet);
                // 检查每个部分的取值范围
                if (value < 0 || value > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                // 如果无法解析为整数，返回 false
                return false;
            }
        }

        // 检查特殊情况：0.0.0.0 和 255.255.255.255 不是有效的子网掩码
        if (subnetMask.equals("0.0.0.0") || subnetMask.equals("255.255.255.255")) {
            return false;
        }

        // 检查是否是连续的二进制 1
        String binaryString = octetsToBinaryString(octets);
        if (!binaryString.matches("1*0*")) {
            return false;
        }

        return true;
    }

    /**
     * @param ip1 ip1
     * @param ip2 ip2
     * @return boolean
     * @description: ipv6地址比较 第一个地址大于等于第二个地址返回true
     * @author: wangyang
     * @date: 2023/12/25 15:03
     */
    public static boolean compareIPv6(String ip1, String ip2) {
        return convertIPv6ToBigInteger(ip1).compareTo(convertIPv6ToBigInteger(ip2)) >= 0;
    }

    /**
     * @param ip1 ip1
     * @param ip2 ip2
     * @return boolean
     * @description: ipv4地址比较 第一个地址大于等于第二个地址返回true
     * @author: wangyang
     * @date: 2023/12/25 15:03
     */
    public static boolean compareIPv4(String ip1, String ip2) {
        return convertIpv4ToInt(ip1) >= convertIpv4ToInt(ip2);
    }

    public static boolean isPrefixOverlap(String prefix1, int prefixLength1, String prefix2, int prefixLength2) {
        try {
            Inet6Address address1 = (Inet6Address) Inet6Address.getByName(prefix1);
            Inet6Address address2 = (Inet6Address) Inet6Address.getByName(prefix2);
            // 检查前缀长度
            if (prefixLength1 != prefixLength2) {
                return false;
            }
            // 获取前缀长度的字节数
            int prefixBytes = prefixLength1 / 8;
            // 获取前缀部分的字节数
            byte[] prefixBytes1 = address1.getAddress();
            byte[] prefixBytes2 = address2.getAddress();
            // 比较前缀部分
            for (int i = 0; i < prefixBytes; i++) {
                if (prefixBytes1[i] != prefixBytes2[i]) {
                    return false;
                }
            }
            return true;
        } catch (UnknownHostException e) {
            logger.error("Invalid IP address: {}", e.getMessage());
            return false;
        }
    }

    /**
     * @param ipAddress ipv6地址
     * @return int
     * @description: 将ipv4地址转成int类型
     * @author: wangyang
     * @date: 2023/12/25 16:07
     */
    public static int convertIpv4ToInt(String ipAddress) {
        String[] ipParts = ipAddress.split("\\.");
        if (ipParts.length != 4) {
            throw new IllegalArgumentException("IPv4 地址格式无效");
        }
        int ipInt = 0;
        for (int i = 0; i < 4; i++) {
            int part = Integer.parseInt(ipParts[i]);
            if (part < 0 || part > 255) {
                throw new IllegalArgumentException("IPv4 地址格式无效");
            }
            ipInt = (ipInt << 8) | part;
        }
        return ipInt;
    }

    /**
     * @param ipv6Address ipv6地址
     * @return java.math.BigInteger
     * @description: 将ipv6转成int类型
     * @author: wangyang
     * @date: 2023/12/25 16:09
     */
    public static BigInteger convertIPv6ToBigInteger(String ipv6Address) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipv6Address);
            byte[] addressBytes = inetAddress.getAddress();
            return new BigInteger(1, addressBytes);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("IPv6 地址格式无效");
        }
    }

    /**
    * @description: 通过前缀长度获取前缀地址
    * @param ipv6Address 起始地址
    * @param prefixLength 前缀长度
    * @return java.lang.String
    * @author: wangyang
    * @date: 2023/12/29 17:09
    */
    public static String generateIPv6NetworkPrefix(String ipv6Address, int prefixLength) {
        try {
            Inet6Address inet6Address = (Inet6Address) Inet6Address.getByName(ipv6Address);
            byte[] addressBytes = inet6Address.getAddress();

            // 根据前缀长度截取生成的 IPv6 地址，得到网络前缀
            byte[] networkPrefixBytes = new byte[16];
            System.arraycopy(addressBytes, 0, networkPrefixBytes, 0, prefixLength / 8);

            // 转换网络前缀为 IPv6 地址的标准表示形式
            Inet6Address networkPrefixAddress = (Inet6Address) Inet6Address.getByAddress(networkPrefixBytes);
            return parseAbbrIPv6(networkPrefixAddress.getHostAddress());
        }catch (Exception e){
            logger.error("通过前缀长度获取前缀地址失败,{}",e.getMessage());
        }
        return null;
    }


    /**
     * 将一个IPv6地址转为全写格式，全写中的前导0省略
     * 例：将1ade:03da:0::转为1ade:3da:0:0:0:0:0:0
     * @param IPv6Str ipv6地址
     * @return fullIPv6
     */
    public static String parseFullIPv6(String IPv6Str) {
        String[] arr = new String[]{"0", "0", "0", "0", "0", "0", "0", "0"};
        // 将IPv6地址用::分开
        // 如果IPv6地址为::，tempArr.length==0
        // 如果不包含::或以::结尾，tempArr.length==1
        // 如果以::开头或::在中间，tempArr.length==2
        String[] tempArr = IPv6Str.split("::");
        // tempArr[0]用:分开，填充到arr前半部分
        if (tempArr.length > 0) {
            // new String[0]为空数组，因为"".split(":")为{""}，如果tempArr[0]==""，此时数组包含一个元素
            String[] tempArr0 = tempArr[0].isEmpty() ? new String[0] : tempArr[0].split(":");
            for (int i = 0; i < tempArr0.length; i++) {
                // 如果是纯数字，用parseInt去除前导0，如果包含字母，用正则去除前导0
                arr[i] = tempArr0[i].matches("\\d+")
                        ? (Integer.parseInt(tempArr0[i]) + "")
                        : tempArr0[i].replaceAll("^(0+)", "");
            }
        }
        // tempArr[1]用:分开，填充到arr后半部分
        if (tempArr.length > 1) {
            String[] tempArr1 = tempArr[1].isEmpty() ? new String[0] : tempArr[1].split(":");
            for (int i = 0; i < tempArr1.length; i++) {
                arr[i + arr.length - tempArr1.length] = tempArr1[i].matches("\\d+")
                        ? (Integer.parseInt(tempArr1[i]) + "")
                        : tempArr1[i].replaceAll("^(0+)", "");
            }
        }

        return StringUtils.join(arr, ":");
    }

    /**
     * 将一个IPv6地址转为简写格式
     * 例：将1ade:03da:0::转为1ade:3da::
     *
     * @param IPv6Str ipv6地址
     * @return AbbrIPv6
     */
    public static String parseAbbrIPv6(String IPv6Str) {
        // 将一个IPv6地址转为全写格式，全写中的前导0省略
        String fullIPv6 = parseFullIPv6(IPv6Str);
        if (fullIPv6.isEmpty()) { // 如果IPv6地址的格式不正确
            return "";
        }
        // 将1ade:3da:0:0:0:0:0:99转为{"-","-","0","0","0","0","0","-"}
        String[] arr = fullIPv6.split(":");
        for (int i = 0, len = arr.length; i < len; i++) {
            if (!"0".equals(arr[i])) {
                arr[i] = "-";
            }
        }
        // 找到最长的连续的0
        Pattern pattern = Pattern.compile("0{2,}");
        Matcher matcher = pattern.matcher(StringUtils.join(arr, ""));
        String maxStr = "";
        int start = -1;
        int end = -1;
        while (matcher.find()) {
            if (maxStr.length() < matcher.group().length()) {
                maxStr = matcher.group();
                start = matcher.start();
                end = matcher.end();
            }
        }
        // 合并
        arr = fullIPv6.split(":");
        if (maxStr.length() > 0) {
            for (int i = start; i < end; i++) {
                arr[i] = ":";
            }
        }
        return StringUtils.join(arr, ":").replaceAll(":{2,}", "::");
    }

    public static void main(String[] args) {
//        System.out.println(checkIPv6RangeConflict("2004::1", "2004::2000", "2003::1", "2003::1000"));
//        System.out.println(checkIPv6RangeConflict( "2003::1", "2003::1000","2004::1", "2004::2000"));
//        System.out.println(checkIPv6RangeConflict( "2003::1", "2004::1000","2004::1", "2004::2000"));
//        System.out.println(checkIPv6RangeConflict( "2003::1", "2004::2000","2004::1", "2004::2000"));
//        System.out.println(checkIPv6RangeConflict( "2004::1", "2004::2","2004::2", "2004::2000"));
//        System.out.println(checkIPv6RangeConflict( "2004::2001", "2004::3000","2004::1", "2004::2000"));
//        System.out.println(checkIPv6RangeConflict("192.168.1.1","192.168.1.4","192.168.0.5","192.168.0.255"));
//        System.out.println(checkIPv4RangeConflict("192.168.0.1","192.168.0.100","192.168.0.101","192.168.0.255"));
//        System.out.println(BigInteger.valueOf(convertIpv4ToInt("192.168.0.100")));
        System.out.println(generateIPv6NetworkPrefix("2001:db8::1", 64));
//        System.out.println(isValidSubnetMask("255.0.0.0"));
    }


}
