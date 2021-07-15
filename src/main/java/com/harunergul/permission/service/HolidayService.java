package com.harunergul.permission.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harunergul.permission.model.PublicHoliday;
import com.harunergul.permission.repository.PublicHolidayRepository;

@Service
public class HolidayService {

	@Autowired
	private PublicHolidayRepository holidayRepos;

	public Iterable<PublicHoliday> getPublicHolidays() {
		return holidayRepos.findAll();
	}

	public PublicHoliday savePublicHoliday(PublicHoliday newYearsDay) {
		return holidayRepos.save(newYearsDay);
	}

}
