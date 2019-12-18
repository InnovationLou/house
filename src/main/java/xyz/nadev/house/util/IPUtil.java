package xyz.nadev.house.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户获取本机IP
 */
public class IPUtil {

    /**
     * 获取本机IP地址
     * @return
     */
    public static String getIPAddress(){
        Enumeration allNetInterfaces = allNetInterfaces();
        InetAddress ip = null;
        if (null == allNetInterfaces)
            return null;
        while (allNetInterfaces.hasMoreElements())
        {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements())
            {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address)
                {
                    return ip.getHostAddress();
                }
            }
        }
        return null;
    }

    /**
     * 返回IP地址的最后32位长度 如果超长, 我也不知道写这个方法干嘛？ 会超过32位的长度？ 我只知道超过长度微信支付会出错.
     * @return
     */
    public static String getIPAddressByLast32(){
        String fullAddr = getIPAddress().trim();

        Pattern pattern = Pattern.compile("\r|\n|\t");
        Matcher matcher = pattern.matcher(fullAddr);
        matcher.replaceAll("");

        int size = fullAddr.length();
        if (size < 33)
            return fullAddr;
        else
            return fullAddr.substring(size - 32);
    }

    /**
     * 封装异常处理
     * @return
     */
    private static Enumeration allNetInterfaces(){
        try {
            return NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("hello:" + getIPAddressByLast32());
    }
}
