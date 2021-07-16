package com.harunergul.permission.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harunergul.permission.auth.AuthenticationRequest;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthenticationController.class})
@ExtendWith(SpringExtension.class)
public class AuthenticationControllerTest {
    @Autowired
    private AuthenticationController authenticationController;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private HolidayService holidayService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private PartialUserRepository partialUserRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Test
    public void testCreateAuthenticationToken() throws Exception {
        User user = new User();
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<Role>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setEnabled(true);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        user.setHireDate(Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()));
        user.setFirstName("Jane");
        user.setExist(true);
        when(this.userService.getUserById((Long) any())).thenReturn(user);

        PartialUser partialUser = new PartialUser();
        partialUser.setLastName("Doe");
        partialUser.setEmail("jane.doe@example.org");
        partialUser.setRoles(new ArrayList<Role>());
        partialUser.setUsername("janedoe");
        partialUser.setId(123L);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        partialUser.setHireDate(Date.from(atStartOfDayResult1.atZone(ZoneId.systemDefault()).toInstant()));
        partialUser.setFirstName("Jane");
        partialUser.setExist(true);
        Optional<PartialUser> ofResult = Optional.<PartialUser>of(partialUser);
        when(this.partialUserRepository.findUserByUsername(anyString())).thenReturn(ofResult);
        when(this.jwtUtil.generateToken((PartialUser) any())).thenReturn("foo");
        when(this.authenticationManager.authenticate((org.springframework.security.core.Authentication) any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("iloveyou");
        authenticationRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(authenticationRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.authenticationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"jwt\":\"foo\"}"));
    }

    @Test
    public void testCreateAuthenticationToken2() throws Exception {
        User user = new User();
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<Role>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setEnabled(true);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        user.setHireDate(Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()));
        user.setFirstName("Jane");
        user.setExist(true);
        when(this.userService.getUserById((Long) any())).thenReturn(user);
        when(this.partialUserRepository.findUserByUsername(anyString())).thenReturn(Optional.<PartialUser>empty());
        when(this.jwtUtil.generateToken((PartialUser) any())).thenReturn("foo");
        when(this.authenticationManager.authenticate((org.springframework.security.core.Authentication) any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setPassword("iloveyou");
        authenticationRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(authenticationRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.authenticationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("\"INTERNAL_SERVER_ERROR\""));
    }

    @Test
    public void testCreateDefaultUser() throws Exception {
        User user = new User();
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<Role>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setEnabled(true);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        user.setHireDate(Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()));
        user.setFirstName("Jane");
        user.setExist(true);
        when(this.userRepository.save((User) any())).thenReturn(user);
        when(this.userRepository.findByUsername(anyString())).thenReturn(new ArrayList<User>());

        User user1 = new User();
        user1.setLastName("Doe");
        user1.setEmail("jane.doe@example.org");
        user1.setPassword("iloveyou");
        user1.setRoles(new ArrayList<Role>());
        user1.setUsername("janedoe");
        user1.setId(123L);
        user1.setEnabled(true);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        user1.setHireDate(Date.from(atStartOfDayResult1.atZone(ZoneId.systemDefault()).toInstant()));
        user1.setFirstName("Jane");
        user1.setExist(true);

        Role role = new Role();
        role.setRole("Role");
        role.setRoleId(123);
        role.setUser(user1);
        when(this.roleRepository.save((Role) any())).thenReturn(role);

        PublicHoliday publicHoliday = new PublicHoliday();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        publicHoliday.setEndDate(Date.from(atStartOfDayResult2.atZone(ZoneId.systemDefault()).toInstant()));
        publicHoliday.setStatus("Status");
        publicHoliday.setId(123L);
        publicHoliday.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        publicHoliday.setStartDate(Date.from(atStartOfDayResult3.atZone(ZoneId.systemDefault()).toInstant()));
        when(this.holidayService.savePublicHoliday((PublicHoliday) any())).thenReturn(publicHoliday);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/auth/create-users");
        MockMvcBuilders.standaloneSetup(this.authenticationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Admin user created!"));
    }

    @Test
    public void testCreateDefaultUser2() throws Exception {
        User user = new User();
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<Role>());
        user.setUsername("janedoe");
        user.setId(123L);
        user.setEnabled(true);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        user.setHireDate(Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()));
        user.setFirstName("Jane");
        user.setExist(true);

        ArrayList<User> userList = new ArrayList<User>();
        userList.add(user);

        User user1 = new User();
        user1.setLastName("Doe");
        user1.setEmail("jane.doe@example.org");
        user1.setPassword("iloveyou");
        user1.setRoles(new ArrayList<Role>());
        user1.setUsername("janedoe");
        user1.setId(123L);
        user1.setEnabled(true);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        user1.setHireDate(Date.from(atStartOfDayResult1.atZone(ZoneId.systemDefault()).toInstant()));
        user1.setFirstName("Jane");
        user1.setExist(true);
        when(this.userRepository.save((User) any())).thenReturn(user1);
        when(this.userRepository.findByUsername(anyString())).thenReturn(userList);

        User user2 = new User();
        user2.setLastName("Doe");
        user2.setEmail("jane.doe@example.org");
        user2.setPassword("iloveyou");
        user2.setRoles(new ArrayList<Role>());
        user2.setUsername("janedoe");
        user2.setId(123L);
        user2.setEnabled(true);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        user2.setHireDate(Date.from(atStartOfDayResult2.atZone(ZoneId.systemDefault()).toInstant()));
        user2.setFirstName("Jane");
        user2.setExist(true);

        Role role = new Role();
        role.setRole("Role");
        role.setRoleId(123);
        role.setUser(user2);
        when(this.roleRepository.save((Role) any())).thenReturn(role);

        PublicHoliday publicHoliday = new PublicHoliday();
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        publicHoliday.setEndDate(Date.from(atStartOfDayResult3.atZone(ZoneId.systemDefault()).toInstant()));
        publicHoliday.setStatus("Status");
        publicHoliday.setId(123L);
        publicHoliday.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        publicHoliday.setStartDate(Date.from(atStartOfDayResult4.atZone(ZoneId.systemDefault()).toInstant()));
        when(this.holidayService.savePublicHoliday((PublicHoliday) any())).thenReturn(publicHoliday);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/auth/create-users");
        MockMvcBuilders.standaloneSetup(this.authenticationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Admin user already exist!"));
    }
}

