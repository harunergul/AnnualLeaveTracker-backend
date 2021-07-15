package com.harunergul.permission.service;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.harunergul.permission.exception.ApiRequestException;
import com.harunergul.permission.model.PartialUser;
import com.harunergul.permission.model.PermissionRequest;
import com.harunergul.permission.model.PermissionRequestAcceptanceStatus;
import com.harunergul.permission.model.PublicHoliday;
import com.harunergul.permission.model.User;
import com.harunergul.permission.repository.PermissionRequestRepository;
import com.harunergul.permission.util.DateUtil;
import com.harunergul.permission.util.LocaleKey;
import com.harunergul.permission.util.PermissionRequestSpecs;

@Service
public class PermissionRequestService {

	@Autowired
	private PermissionRequestRepository permissionRequestRepo;

	@Autowired
	private HolidayService holidayService;

	@Autowired
	private UserService userService;

	private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	public Iterable<PermissionRequest> getAllPermissionRequests() {
		return permissionRequestRepo.findAll(PermissionRequestSpecs.exist());
	}

	public List<PermissionRequest> getAllPermissionsByUser(Long userId) {
		PartialUser user = userService.getParitalUser(userId);
		Specification<PermissionRequest> spec = Specification.where(PermissionRequestSpecs.exist())
				.and(PermissionRequestSpecs.userIs(user));
		return permissionRequestRepo.findAll(spec);
	}

	public List<PermissionRequest> getUserRequestPermissionsExcept(Long userId, Long permissionRequestId) {
		PartialUser user = userService.getParitalUser(userId);
		Specification<PermissionRequest> spec = Specification.where(PermissionRequestSpecs.exist())
				.and(PermissionRequestSpecs.userIs(user))
				.and(PermissionRequestSpecs.notIncludeRequest(permissionRequestId));

		return permissionRequestRepo.findAll(spec);
	}

	public void deletePermissionRequestById(Long id) {
		PermissionRequest request = permissionRequestRepo.findById(id)
				.orElseThrow(() -> new ApiRequestException(LocaleKey.PERMISSION_REQUEST_NOT_AVAILABLE));

		if (request.getAcceptanceStatus().equals(PermissionRequestAcceptanceStatus.WAITING.ordinal())) {
			request.setStatus("0");
			permissionRequestRepo.save(request);
		} else {
			throw new ApiRequestException(LocaleKey.CAN_NOT_DELETE_REQUEST);
		}
	}

	public PermissionRequest getPermissionById(Long id) {
		return permissionRequestRepo.findById(id)
				.orElseThrow(() -> new ApiRequestException(LocaleKey.PERMISSION_REQUEST_NOT_AVAILABLE));
	}

	public Map<String, Object> getTotalAmountOfLeave(Long userId) {

		Map<String, Object> map = new HashMap<>();
		User user = userService.getUserById(userId);
		Date hireDate = user.getHireDate();

		long periodOfService = getDayCount(hireDate);

		long year = periodOfService / 365;

		map.put("year",year);
		if (year < 1) {
			map.put("leaveRight", Long.valueOf(5));
			return map;
		} else {
			map.put("leaveRight", calculatePermissionCount(year));
		}
		return map;
	}

	private long calculatePermissionCount(long year) {

		if (year < 1) {
			return 0;
		} else if (year <= 5) {
			return 15 + calculatePermissionCount(year - 1);
		} else if (year <= 10) {
			return 18 + calculatePermissionCount(year - 1);
		} else {
			return 24 + calculatePermissionCount(year - 1);
		}

	}

	public PermissionRequest updatePermissionRequest(PermissionRequest request) {
		controlRequest(request);
		request.setUpdateDate(new Date());
		return permissionRequestRepo.save(request);
	}

	public PermissionRequest addPermissionRequest(PermissionRequest request) {
		controlRequest(request);
		request.setAcceptanceStatus(PermissionRequestAcceptanceStatus.WAITING.ordinal());
		request.setStatus("1");
		request.setRequestDate(new Date());
		return permissionRequestRepo.save(request);
	}

	private void controlRequest(PermissionRequest request) {
		Map<String, Object> amountInfo = getTotalAmountOfLeave(request.getUser().getId());

		long year = (long) amountInfo.get("year");
		long leaveRight = (long) amountInfo.get("leaveRight");
		if (request.getStartDate().equals(request.getEndDate())) {
			throw new ApiRequestException(LocaleKey.DEPARTURE_DATE_AND_START_DATE_SAME);
		}

		if (leaveRight == 0) {
			throw new ApiRequestException(LocaleKey.NOT_ENTITLED_TO_ANNUAL_LEAVE);
		}

		long requestedDays = getRequestedDaysCount(request);
 		long availableRight = leaveRight - getUsedDayCount(request);

		if (requestedDays == 0) {
			throw new ApiRequestException(LocaleKey.REQUESTED_DAYS_ARE_HOLIDAY);
		} else if (requestedDays > availableRight) {

			if (year < 1) {
				throw new ApiRequestException(LocaleKey.MORE_THAN_FIRST_YEAR_ADVANCE, String.valueOf(requestedDays),
						String.valueOf(availableRight));
			}

			throw new ApiRequestException(LocaleKey.REQUESTED_DAYS_ARE_TOO_MUCH, String.valueOf(requestedDays),
					String.valueOf(availableRight));
		}
	}

	private long getRequestedDaysCount(PermissionRequest request) {

		Set<String> holidays = publicHolidays();
		Set<String> existingRequests = existingPermissionRequests(request);

		Date startDate = request.getStartDate();
		Date endDate = request.getEndDate();

		if (endDate.before(startDate)) {
			throw new ApiRequestException(LocaleKey.PERMISSION_DATE_BEFORE_REQUEST_DATE);
		}

		List<Date> requestedDates = DateUtil.getDates(startDate, endDate);
		int requestedCount = 0;
		for (Date date : requestedDates) {
			String formattedDate = sdf.format(date);
			if (existingRequests.contains(formattedDate)) {
				throw new ApiRequestException(LocaleKey.PERMISSION_COLLISION, formattedDate);
			} else if (isWeekend(date) || holidays.contains(formattedDate)) {

			} else {
				requestedCount += 1;
			}
		}
		return requestedCount;
	}

	private int getUsedDayCount(PermissionRequest request) {

		int count = 0;
		List<PermissionRequest> existingRequests = null;
		if (request.getId() != null) {
			existingRequests = getUserRequestPermissionsExcept(request.getUser().getId(), request.getId());
		} else {
			existingRequests = getAllPermissionsByUser(request.getUser().getId());
		}
 		Set<String> holidays = publicHolidays();
		for (PermissionRequest existingRequest : existingRequests) {
			if(existingRequest.getAcceptanceStatus()==PermissionRequestAcceptanceStatus.REFUESED.ordinal()) {
				continue;
			}
			List<Date> dates = DateUtil.getDates(existingRequest.getStartDate(), existingRequest.getEndDate());
			for (Date date : dates) {
				String formattedDate = sdf.format(date);
				
				if (isWeekend(date) || holidays.contains(formattedDate)) {

				} else {
					count += 1;
				}
 			}
			
			 
		}
		return count;
	}
	
	private Set<String> existingPermissionRequests(PermissionRequest request) {

		List<PermissionRequest> existingRequests = null;
		if (request.getId() != null) {
			existingRequests = getUserRequestPermissionsExcept(request.getUser().getId(), request.getId());
		} else {
			existingRequests = getAllPermissionsByUser(request.getUser().getId());
		}
		Set<String> existingDates = new HashSet<>();
		for (PermissionRequest existingRequest : existingRequests) {
			if(existingRequest.getAcceptanceStatus()==PermissionRequestAcceptanceStatus.REFUESED.ordinal()) {
				continue;
			}
			List<Date> dates = DateUtil.getDates(existingRequest.getStartDate(), existingRequest.getEndDate());
			for (Date date : dates) {
				existingDates.add(sdf.format(date));
			}
		}
		return existingDates;
	}

	private Set<String> publicHolidays() {
		Set<String> publicDays = new HashSet<String>();
		Iterable<PublicHoliday> holidays = holidayService.getPublicHolidays();
		for (PublicHoliday holiday : holidays) {
			Date startDate = holiday.getStartDate();
			Date endDate = holiday.getEndDate();

			if (startDate.before(endDate)) {
				List<Date> dates = DateUtil.getDates(startDate, endDate);

				if (dates != null) {
					for (Date date : dates) {
						publicDays.add(sdf.format(date));
					}
				}

			}
		}
		return publicDays;
	}

	private long getDayCount(Date hireDate) {
		Date today = new Date();
		long diffInMillies = Math.abs(today.getTime() - hireDate.getTime());
		return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	private boolean isWeekend(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if ((cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
				|| (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
			return true;
		} else {
			return false;
		}
	}

	public ResponseEntity<?> updateAcceptanceStatus(Long id, Map<Object, Object> fields) {

		Optional<PermissionRequest> request = permissionRequestRepo.findById(id);

		if (request.isPresent()) {

			fields.forEach((key, value) -> {
				Field field = ReflectionUtils.findField(PermissionRequest.class, (String) key);
				field.setAccessible(true);
				ReflectionUtils.setField(field, request.get(), value);
			});
		} else {
			throw new ApiRequestException(LocaleKey.RECORD_DOES_NOT_EXIST);
		}

		permissionRequestRepo.save(request.get());
		return new ResponseEntity<>(HttpStatus.OK);

	}

}
