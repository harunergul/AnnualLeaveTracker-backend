package com.harunergul.permission.auth;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.harunergul.permission.model.Role;
import com.harunergul.permission.model.User;
import com.harunergul.permission.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ApplicationUserDetailService.class})
@ExtendWith(SpringExtension.class)
public class ApplicationUserDetailServiceTest {
    @Autowired
    private ApplicationUserDetailService applicationUserDetailService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testLoadUserByUsername() throws UsernameNotFoundException {
        when(this.userRepository.findByUsername(anyString())).thenReturn(new ArrayList<User>());
        assertThrows(UsernameNotFoundException.class,
                () -> this.applicationUserDetailService.loadUserByUsername("janedoe"));
        verify(this.userRepository).findByUsername(anyString());
    }

    @Test
    public void testLoadUserByUsername2() throws UsernameNotFoundException {
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
        when(this.userRepository.findByUsername(anyString())).thenReturn(userList);
        assertSame(user, this.applicationUserDetailService.loadUserByUsername("janedoe"));
        verify(this.userRepository).findByUsername(anyString());
    }
}

