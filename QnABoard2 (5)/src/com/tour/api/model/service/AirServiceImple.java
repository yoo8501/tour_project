package com.tour.api.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tour.api.AirParser;

@Service
public class AirServiceImple implements AirService{
	
	private AirParser airParser;
	@Override
	public String[] getArea() {
		String[] area  = {"����", "�λ�", "�뱸", "��õ", "����", "����", "���", "���", "����", "���", "�泲", "����", "����", "���", "�泲", "����", "����"};
		return area;
	}

	@Override
	public List getAirCondition(String area) {
		airParser = new AirParser();
		List list=airParser.getAirCondition(area);
		return list;
	}
	
}
