package com.lzyyauto.jdcookie;


/**
 * @auther: zingliu
 * @description:
 * @Date: 2021/7/31
 */
public class JDCookieApplication {

    public static void main(String[] args) {
        try {
            JdCookie jdCookie = new JdCookie();
            jdCookie.jd_start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
