package com.lzyyauto.jdcookie;

/**
 * @auther: zingliu
 * @description:
 * @Date: 2021/8/1
 */
public interface JDConstant {
    /**
     * User-Agent
     */
    String UA = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_3_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 SP-engine/2.14.0 main/1.0 baiduboxapp/11.18.0.16 (Baidu; P2 13.3.1) NABar/0.0 TM/%s";
    /**
     * referer-gettoken
     */
    String REFERER_GETTOKEN = "https://plogin.m.jd.com/cgi-bin/mm/new_login_entrance?lang=chs&appid=300&returnurl=https://wq.jd.com/passport/LoginRedirect?state=%s&returnurl=https://home.m.jd.com/myJd/newhome.action?sceneval=2&ufc=&/myJd/home.action&source=wq_passport";
    /**
     * URL-gettoken
     */
    String URL_GETTOKEN = "https://plogin.m.jd.com/cgi-bin/mm/new_login_entrance?lang=chs&appid=300&returnurl=https://wq.jd.com/passport/LoginRedirect?state=%s&returnurl=https://home.m.jd.com/myJd/newhome.action?sceneval=2&ufc=&/myJd/home.action&source=wq_passport";
    /**
     * referer-posttoken
     */
    String REFERER_POSTTOKEN = "https://plogin.m.jd.com/login/login?appid=300&returnurl=https://wqlogin2.jd.com/passport/LoginRedirect?state=%s&returnurl=//home.m.jd.com/myJd/newhome.action?sceneval=2&ufc=&/myJd/home.action&source=wq_passport";
    /**
     * URL-posttoken
     */
    String URL_POSTTOKEN = "https://plogin.m.jd.com/cgi-bin/m/tmauthreflogurl?s_token=%s&v=%s&remember=true";
    /**
     * Content-Type
     */
    String CT = "application/x-www-form-urlencoded; Charset=UTF-8";
    /**
     * lang-posttoken
     */
    String LANG = "chs";
    /**
     * appid-posttoken
     */
    int APPID = 300;
    /**
     * returnurl-posttoken
     */
    String RETURNURL = "https://wqlogin2.jd.com/passport/LoginRedirect?state=%sreturnurl=//home.m.jd.com/myJd/newhome.action?sceneval=2&ufc=&/myJd/home.action&source=wq_passport";
    /**
     * url_print
     */
    String URL_PRINT = "https://plogin.m.jd.com/cgi-bin/m/tmauth?client_type=m&appid=300&token=%s";
    /**
     * url_check
     */
    String URL_CHECK = "https://plogin.m.jd.com/cgi-bin/m/tmauthchecktoken?&token=%s&ou_state=0&okl_token=%s";
    /**
     * returnurl-check
     */
    String RETURNURL_CHECK = "https://wqlogin2.jd.com/passport/LoginRedirect?state=%s&returnurl=//home.m.jd.com/myJd/newhome.action?sceneval=2&ufc=&/myJd/home.action";
    /**
     * wq_passport
     */
    String WQ_PASSPORT = "wq_passport";
}