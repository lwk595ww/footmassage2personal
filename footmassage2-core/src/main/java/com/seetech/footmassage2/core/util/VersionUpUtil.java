package com.seetech.footmassage2.core.util;


public class VersionUpUtil {

    private VersionUpUtil() {
    }

    //版本 +1 例如：传入0.0.1 返回 0.0.2 每个数的最大值为99 ，总体最大版本：99.99.99
    public static String versionUp(String version) {
        String[] split = version.split("\\.");
        if (Integer.parseInt(split[2]) == 99 && Integer.parseInt(split[1]) == 99) {
            version = (Integer.parseInt(split[0]) + 1) + "." + 0 + "." + 0;
        } else if (Integer.parseInt(split[2]) == 99) {
            version = split[0] + "." + (Integer.parseInt(split[1]) + 1) + "." + 0;
        } else {
            version = split[0] + "." + split[1] + "." + (Integer.parseInt(split[2]) + 1);
        }

        return version;
    }

}
