package com.harunergul.permission.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.harunergul.permission.auth.AuthenticationRequest;
import com.harunergul.permission.auth.AuthenticationResponse;
import com.harunergul.permission.auth.JwtUtil;
import com.harunergul.permission.model.PartialUser;
import com.harunergul.permission.model.PublicHoliday;
import com.harunergul.permission.model.Role;
import com.harunergul.permission.model.User;
import com.harunergul.permission.repository.PartialUserRepository;
import com.harunergul.permission.repository.RoleRepository;
import com.harunergul.permission.repository.UserRepository;
import com.harunergul.permission.service.HolidayService;
import com.harunergul.permission.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private HolidayService holidayService;

	@Autowired
	private PartialUserRepository partialUserRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {

		try {
			Authentication result = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
			System.out.println(result);
		} catch (BadCredentialsException e) {
			throw new UsernameNotFoundException("Incorrect username or password", e);
		}

		Optional<PartialUser> partialUser = this.partialUserRepo
				.findUserByUsername(authenticationRequest.getUsername());
		if (partialUser.isPresent()) {
			PartialUser pUser = partialUser.get();
			User user = userService.getUserById(pUser.getId());
			pUser.setRoles(user.getRoles());
			final String jwt = jwtUtil.generateToken(pUser);

			return ResponseEntity.ok(new AuthenticationResponse(jwt));
		} else {
			return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/create-users", method = RequestMethod.GET)
	public ResponseEntity<?> createDefaultUser() {
		List<User> userList = userRepo.findByUsername("admin");
		if (userList != null && userList.size() > 0) {
			return new ResponseEntity<>("Admin user already exist!", HttpStatus.OK);
		} else {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String hashedPassword = passwordEncoder.encode("admin");

			ArrayList<Role> roles = new ArrayList<>();

			User user = new User("admin", hashedPassword, roles);
			user.setFirstName("Admin");
			user.setLastName("Admin");
			user.setEnabled(true);
			user.setExist(true);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2014);
			cal.set(Calendar.MONTH, 7);
			cal.set(Calendar.DAY_OF_MONTH, 21);

			user.setHireDate(cal.getTime());
			user = userRepo.save(user);

			Role userRole = new Role();
			userRole.setRole("ROLE_USER");
			userRole.setUser(user);
			roleRepository.save(userRole);

			Role adminRole = new Role();
			adminRole.setRole("ROLE_ADMIN");
			adminRole.setUser(user);
			roleRepository.save(adminRole);

			ArrayList<Role> normalRoles = new ArrayList<>();
			User normalUser = new User("harun", passwordEncoder.encode("harun"), normalRoles);
			normalUser.setFirstName("Harun");
			normalUser.setLastName("Ergul");
			normalUser.setEnabled(true);
			normalUser.setExist(true);

			cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2009);
			cal.set(Calendar.MONTH, 2);
			cal.set(Calendar.DAY_OF_MONTH, 3);
			normalUser.setHireDate(cal.getTime());
			userRepo.save(normalUser);

			Role normalUserRole = new Role();
			normalUserRole.setRole("ROLE_USER");
			normalUserRole.setUser(normalUser);

			roleRepository.save(normalUserRole);
			
			
			normalUser = new User("hasan", passwordEncoder.encode("hasan"), normalRoles);
			normalUser.setFirstName("Hasan");
			normalUser.setLastName("Akarca");
			normalUser.setEnabled(true);
			normalUser.setExist(true);

			cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2020);
			cal.set(Calendar.MONTH, 9);
			cal.set(Calendar.DAY_OF_MONTH, 21);
			normalUser.setHireDate(cal.getTime());
			userRepo.save(normalUser);

			normalUserRole = new Role();
			normalUserRole.setRole("ROLE_USER");
			normalUserRole.setUser(normalUser);
			roleRepository.save(normalUserRole);
			
			
			normalUser = new User("ayhan", passwordEncoder.encode("ayhan"), normalRoles);
			normalUser.setFirstName("Ayhan");
			normalUser.setLastName("Tuncel");
			normalUser.setEnabled(true);
			normalUser.setExist(true);

			cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2000);
			cal.set(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, 17);
			normalUser.setHireDate(cal.getTime());
			userRepo.save(normalUser);

			normalUserRole = new Role();
			normalUserRole.setRole("ROLE_USER");
			normalUserRole.setUser(normalUser);
			roleRepository.save(normalUserRole);
			
			
			
			
			
			
			createHolidays();

			return new ResponseEntity<>("Admin user created!", HttpStatus.OK);

		}

	}

	private void createHolidays() {
		// yeni yil
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2021);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		PublicHoliday newYearsDay = new PublicHoliday();
		newYearsDay.setStartDate(cal.getTime());

		cal.set(Calendar.DAY_OF_MONTH, 2);
		newYearsDay.setEndDate(cal.getTime());
		newYearsDay.setDescription("New Year's Holiday");
		newYearsDay.setStatus("1");

		holidayService.savePublicHoliday(newYearsDay);

		// 23 nisan
		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2021);
		cal.set(Calendar.MONTH, Calendar.APRIL);
		cal.set(Calendar.DAY_OF_MONTH, 23);

		PublicHoliday april23 = new PublicHoliday();
		april23.setStartDate(cal.getTime());

		cal.set(Calendar.DAY_OF_MONTH, 27);
		april23.setEndDate(cal.getTime());
		april23.setDescription("23 Nisan");
		april23.setStatus("1");

		holidayService.savePublicHoliday(april23);

		// labour day

		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2021);
		cal.set(Calendar.MONTH, Calendar.MAY);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		PublicHoliday labourDay = new PublicHoliday();
		labourDay.setStartDate(cal.getTime());

		cal.set(Calendar.DAY_OF_MONTH, 2);
		labourDay.setEndDate(cal.getTime());
		labourDay.setDescription("isci bayrami");
		labourDay.setStatus("1");

		holidayService.savePublicHoliday(labourDay);
		// jully 15

		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2021);
		cal.set(Calendar.MONTH, Calendar.JULY);
		cal.set(Calendar.DAY_OF_MONTH, 15);

		PublicHoliday jully15 = new PublicHoliday();
		jully15.setStartDate(cal.getTime());

		cal.set(Calendar.DAY_OF_MONTH, 16);
		jully15.setEndDate(cal.getTime());
		jully15.setDescription("15 Temmuz");
		jully15.setStatus("1");

		holidayService.savePublicHoliday(jully15);
		// eid 15

		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2021);
		cal.set(Calendar.MONTH, Calendar.JULY);
		cal.set(Calendar.DAY_OF_MONTH, 20);

		PublicHoliday eid = new PublicHoliday();
		eid.setStartDate(cal.getTime());

		cal.set(Calendar.DAY_OF_MONTH, 24);
		eid.setEndDate(cal.getTime());
		eid.setDescription("Kurban bayrami");
		eid.setStatus("1");

		holidayService.savePublicHoliday(eid);

	}
}
