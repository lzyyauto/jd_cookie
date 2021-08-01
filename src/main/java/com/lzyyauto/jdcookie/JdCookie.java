package com.lzyyauto.jdcookie;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * @author zingliu
 * @auther zingliu
 * @description
 * @Date 2021/7/31
 */
public class JdCookie {
    RestTemplate restTemplate = getRestTemplate();
    String sToken = "";
    String oklToken = "";

    public JdCookie() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
    }

    public void jd_start() {
        List<String> cookies = token_get();
        System.out.println(sToken);
        token_post(cookies);
        System.out.println(oklToken);
        image_print();
        boolean result = true;
        while (result) {
            try {
                Thread.sleep(5000);
                result = check_token(cookies);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> token_get() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", String.format(JDConstant.UA, System.currentTimeMillis()));
        headers.set("referer", String.format(JDConstant.REFERER_GETTOKEN, System.currentTimeMillis()));

        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format(JDConstant.URL_GETTOKEN, System.currentTimeMillis()), HttpMethod.GET, entity, String.class);
        sToken = JSONObject.parseObject(response.getBody()).getString("s_token");
        return response.getHeaders().get("Set-Cookie");
    }

    public void token_post(List<String> cookies) {
        HttpHeaders headers = new HttpHeaders();
        headers.put("Cookie", cookies);
        headers.set("User-Agent", String.format(JDConstant.UA, System.currentTimeMillis()));
        headers.set("referer", String.format(JDConstant.REFERER_POSTTOKEN, System.currentTimeMillis()));
        headers.set("Content-Type", JDConstant.CT);

        MultiValueMap map = new LinkedMultiValueMap();
        map.add("chs", JDConstant.LANG);
        map.add("appid", JDConstant.APPID);
        map.add("returnurl", String.format(JDConstant.RETURNURL, System.currentTimeMillis()));

        HttpEntity entity = new HttpEntity(map, headers);

        ResponseEntity<String> response = restTemplate.exchange(String.format(JDConstant.URL_POSTTOKEN, sToken, System.currentTimeMillis()), HttpMethod.POST, entity, String.class);

        sToken = JSONObject.parseObject(response.getBody()).getString("token");
        String[] cos = response.getHeaders().get("Set-Cookie").toString().split(";");
        for (String c : cos) {
            if (c.indexOf("okl_token") > 0) {
                oklToken = c.substring(c.indexOf("=") + 1);
                break;
            }
        }
    }

    public void image_print() {
        String priUrl = String.format(JDConstant.URL_PRINT, oklToken);
        System.out.println(getQr(priUrl));
    }

    public boolean check_token(List<String> cookies) {
        HttpHeaders headers = new HttpHeaders();
        headers.put("Cookie", cookies);
        headers.set("User-Agent", String.format(JDConstant.UA, System.currentTimeMillis()));
        headers.set("referer", String.format(JDConstant.REFERER_POSTTOKEN, System.currentTimeMillis()));
        headers.set("Content-Type", JDConstant.CT);

        MultiValueMap map = new LinkedMultiValueMap();
        map.add("chs", JDConstant.LANG);
        map.add("appid", JDConstant.APPID);
        map.add("returnurl", String.format(JDConstant.RETURNURL_CHECK, System.currentTimeMillis()));
        map.add("source", JDConstant.WQ_PASSPORT);

        HttpEntity entity = new HttpEntity(map, headers);

        ResponseEntity<String> response = restTemplate.exchange(String.format(JDConstant.URL_CHECK, sToken, oklToken), HttpMethod.POST, entity, String.class);

        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        if (jsonObject.getInteger("errcode") == 0) {
            System.out.println("扫码成功");
            System.out.println(jsonObject.getString("message"));
            return false;
        } else {
            System.out.println("轮询中....");
            System.out.println(jsonObject.getInteger("errcode"));
            System.out.println(jsonObject.getString("message"));
        }
        return true;

    }


    public RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (x509Certificates, s) -> true;
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }


    public String getQr(String text) {
        String s = "生成二维码失败";
        int width = 5;
        int height = 5;
        // 用于设置QR二维码参数
        Hashtable<EncodeHintType, Object> qrParam = new Hashtable<EncodeHintType, Object>();
        // 设置QR二维码的纠错级别——这里选择最低L级别
        qrParam.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        qrParam.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, qrParam);
            s = toAscii(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return s;
    }

    public String toAscii(BitMatrix bitMatrix) {
        StringBuilder sb = new StringBuilder();
        for (int rows = 0; rows < bitMatrix.getHeight(); rows++) {
            for (int cols = 0; cols < bitMatrix.getWidth(); cols++) {
                boolean x = bitMatrix.get(rows, cols);
                if (!x) {
                    // white
                    sb.append("\033[47m  \033[0m");
                } else {
                    sb.append("\033[30m  \033[0;39m");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
