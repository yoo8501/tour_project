package com.tour.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;





public class AirParser {
	SAXParserFactory saxParserFactory;
	SAXParser saxParser;//�Ľ��� ����ϴ� ��ü
	URL url;
	URLConnection con;
	AirHandler airHandler;
	StringBuilder urlBuilder=null;
	List airList=null;
	public AirParser() {
		saxParserFactory = SAXParserFactory.newInstance();
	}
	public List getAirCondition(String area) {
		 	try {
				urlBuilder = new StringBuilder("http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst"); /*URL*/
				urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=aomUsxnLb3DTqHQIrlwe9AM5XaeflDF4oLW73sSIeo5EC6EEu6GUKunzwiTqsDuMycHJHeU4Y0weHHkYR%2BvUlw%3D%3D"); /*Service Key*/
				if(area.equals("����")) {
					urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("26", "UTF-8")); /*�� ������ ��� ��*/
				}
				if(area.equals("�λ�")) {
					urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("16", "UTF-8")); /*�� ������ ��� ��*/
				}
				if(area.equals("�뱸")) {
					urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("8", "UTF-8")); /*�� ������ ��� ��*/
				}
				if(area.equals("��õ")) {
					urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("9", "UTF-8")); /*�� ������ ��� ��*/
				}
				if(area.equals("����")) {
					urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("6", "UTF-8")); /*�� ������ ��� ��*/
				}
				if(area.equals("����")) {
					urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("6", "UTF-8")); /*�� ������ ��� ��*/
				}
				if(area.equals("���")) {
					urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("5", "UTF-8")); /*�� ������ ��� ��*/
				}
				if(area.equals("���")) {
					urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("31", "UTF-8")); /*�� ������ ��� ��*/
				}
				if(area.equals("����")) {
					urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("18", "UTF-8")); /*�� ������ ��� ��*/
				}
				if(area.equals("���")) {
					urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("11", "UTF-8")); /*�� ������ ��� ��*/
				}
				if(area.equals("�泲")) {
					urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("15", "UTF-8")); /*�� ������ ��� ��*/
				}
				if(area.equals("����")) {
					urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("14", "UTF-8")); /*�� ������ ��� ��*/
				}
				if(area.equals("����")) {
					urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("18", "UTF-8")); /*�� ������ ��� ��*/
				}
				if(area.equals("���")) {
					urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("12", "UTF-8")); /*�� ������ ��� ��*/
				}
				if(area.equals("�泲")) {
					urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("14", "UTF-8")); /*�� ������ ��� ��*/
				}
				if(area.equals("����")) {
					urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("2", "UTF-8")); /*�� ������ ��� ��*/
				}
				if(area.equals("����")) {
					urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*�� ������ ��� ��*/
				}
				urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*������ ��ȣ*/
				urlBuilder.append("&" + URLEncoder.encode("sidoName","UTF-8") + "=" + URLEncoder.encode(area, "UTF-8")); /*�õ� �̸� (����, �λ�, �뱸, ��õ, ����, ����, ���, ���, ����, ���, �泲, ����, ����, ���, �泲, ����, ����)*/
				urlBuilder.append("&" + URLEncoder.encode("searchCondition","UTF-8") + "=" + URLEncoder.encode("DAILY", "UTF-8")); /*��û �����ͱⰣ (�ð� : HOUR, �Ϸ� : DAILY)*/
				url = new URL(urlBuilder.toString());
				con=url.openConnection();
				saxParser = saxParserFactory.newSAXParser();
				saxParser.parse(con.getInputStream(),airHandler=new AirHandler());
				airList= airHandler.getAirList();
		 	} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return airList;
	}
}
