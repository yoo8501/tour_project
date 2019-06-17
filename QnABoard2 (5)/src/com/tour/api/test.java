package com.tour.api;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import org.apache.taglibs.standard.lang.jstl.IntegerLiteral;

import com.tour.common.board.CurrentDay;

import java.io.BufferedReader;
import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
    	String currentDay=CurrentDay.getCurrentDate();
    	//0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300;
    	String currentTime=CurrentDay.getCurrnetTime();
    	int time=Integer.parseInt(currentTime);
    	System.out.println(time);
    	if(time>300 && time<=600) {
    		currentTime = "0"+"200";
    	}else if(time>600 && time<=900) {
    		currentTime = "0"+"500";
    	}else if(time>900 && time<=1200) {
    		currentTime = "0"+"800";
    	}else if(time>1200 && time<=1500) {
    		currentTime = "1100";
    	}else if(time>1500 && time<=1800) {
    		currentTime = "1400";
    	}else if(time>1800 && time<=2100) {
    		currentTime = "1700";
    	}else if(time>2100 && time<=2359) {
    		currentTime = "2000";
    	}else if(time>0000 && time<=200) {
    		currentTime = "2300";
    	}
    	System.out.println(currentTime);
        StringBuilder urlBuilder = new StringBuilder("http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=aomUsxnLb3DTqHQIrlwe9AM5XaeflDF4oLW73sSIeo5EC6EEu6GUKunzwiTqsDuMycHJHeU4Y0weHHkYR%2BvUlw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode("aomUsxnLb3DTqHQIrlwe9AM5XaeflDF4oLW73sSIeo5EC6EEu6GUKunzwiTqsDuMycHJHeU4Y0weHHkYR%2BvUlw%3D%3D", "UTF-8")); /*���� ����*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(currentDay, "UTF-8")); /*��15�� 12�� 1�Ϲ�ǥ*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(currentTime, "UTF-8")); /*05�� ��ǥ * ������� ����*/
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("58", "UTF-8")); /*���������� X ��ǥ��*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("126", "UTF-8")); /*���������� Y ��ǥ��*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("72", "UTF-8")); /*�� ������ ��� ��*/
        urlBuilder.append("&" + URLEncoder.encode("ftype","UTF-8") + "=" + URLEncoder.encode("ODAM", "UTF-8")); /*���ϱ��� -ODAM: ���׿�����Ȳ -VSRT: ���׿����ʴܱ� -SHRT: ���׿����ܱ�*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*������ ��ȣ*/
        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8")); /*xml(�⺻��), json*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(),"utf-8"));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
    }
}