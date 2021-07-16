package com.harunergul.permission.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harunergul.permission.model.PartialUser;
import com.harunergul.permission.model.PermissionRequest;
import com.harunergul.permission.model.Role;
import com.harunergul.permission.service.PermissionRequestService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {PermissionRequestController.class})
@ExtendWith(SpringExtension.class)
public class PermissionRequestControllerTest {
    @Autowired
    private PermissionRequestController permissionRequestController;

    @MockBean
    private PermissionRequestService permissionRequestService;

    @Test
    public void testGetAllPermissionsByUser() throws Exception {
        when(this.permissionRequestService.getAllPermissionsByUser((Long) any()))
                .thenReturn(new ArrayList<PermissionRequest>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/permission-request/all/{id}", 123L);
        MockMvcBuilders.standaloneSetup(this.permissionRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testAddPermissionRequest() throws Exception {
        PartialUser partialUser = new PartialUser();
        partialUser.setLastName("Doe");
        partialUser.setEmail("jane.doe@example.org");
        partialUser.setRoles(new ArrayList<Role>());
        partialUser.setUsername("janedoe");
        partialUser.setId(123L);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        partialUser.setHireDate(Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()));
        partialUser.setFirstName("Jane");
        partialUser.setExist(true);

        PermissionRequest permissionRequest = new PermissionRequest();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest.setEndDate(Date.from(atStartOfDayResult1.atZone(ZoneId.systemDefault()).toInstant()));
        permissionRequest.setStatus("Status");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest.setRequestDate(Date.from(atStartOfDayResult2.atZone(ZoneId.systemDefault()).toInstant()));
        permissionRequest.setUser(partialUser);
        permissionRequest.setId(123L);
        permissionRequest.setAcceptanceStatus(1);
        permissionRequest.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest.setUpdateDate(Date.from(atStartOfDayResult3.atZone(ZoneId.systemDefault()).toInstant()));
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest.setStartDate(Date.from(atStartOfDayResult4.atZone(ZoneId.systemDefault()).toInstant()));
        when(this.permissionRequestService.addPermissionRequest((PermissionRequest) any())).thenReturn(permissionRequest);

        PartialUser partialUser1 = new PartialUser();
        partialUser1.setLastName("Doe");
        partialUser1.setEmail("jane.doe@example.org");
        partialUser1.setRoles(new ArrayList<Role>());
        partialUser1.setUsername("janedoe");
        partialUser1.setId(123L);
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        partialUser1.setHireDate(Date.from(atStartOfDayResult5.atZone(ZoneId.systemDefault()).toInstant()));
        partialUser1.setFirstName("Jane");
        partialUser1.setExist(true);

        PermissionRequest permissionRequest1 = new PermissionRequest();
        LocalDateTime atStartOfDayResult6 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest1.setEndDate(Date.from(atStartOfDayResult6.atZone(ZoneId.systemDefault()).toInstant()));
        permissionRequest1.setStatus("Status");
        LocalDateTime atStartOfDayResult7 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest1.setRequestDate(Date.from(atStartOfDayResult7.atZone(ZoneId.systemDefault()).toInstant()));
        permissionRequest1.setUser(partialUser1);
        permissionRequest1.setId(123L);
        permissionRequest1.setAcceptanceStatus(1);
        permissionRequest1.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult8 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest1.setUpdateDate(Date.from(atStartOfDayResult8.atZone(ZoneId.systemDefault()).toInstant()));
        LocalDateTime atStartOfDayResult9 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest1.setStartDate(Date.from(atStartOfDayResult9.atZone(ZoneId.systemDefault()).toInstant()));
        String content = (new ObjectMapper()).writeValueAsString(permissionRequest1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/permission-request/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.permissionRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"user\":{\"id\":123,\"username\":\"janedoe\",\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe"
                                        + "@example.org\",\"hireDate\":-7200000,\"roles\":[],\"exist\":true},\"startDate\":-7200000,\"endDate\":-7200000,"
                                        + "\"description\":\"The characteristics of someone or something\",\"requestDate\":-7200000,\"updateDate\":-7200000"
                                        + ",\"acceptanceStatus\":1,\"status\":\"Status\"}"));
    }

    @Test
    public void testUpdatePermissionRequest() throws Exception {
        PartialUser partialUser = new PartialUser();
        partialUser.setLastName("Doe");
        partialUser.setEmail("jane.doe@example.org");
        partialUser.setRoles(new ArrayList<Role>());
        partialUser.setUsername("janedoe");
        partialUser.setId(123L);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        partialUser.setHireDate(Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()));
        partialUser.setFirstName("Jane");
        partialUser.setExist(true);

        PermissionRequest permissionRequest = new PermissionRequest();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest.setEndDate(Date.from(atStartOfDayResult1.atZone(ZoneId.systemDefault()).toInstant()));
        permissionRequest.setStatus("Status");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest.setRequestDate(Date.from(atStartOfDayResult2.atZone(ZoneId.systemDefault()).toInstant()));
        permissionRequest.setUser(partialUser);
        permissionRequest.setId(123L);
        permissionRequest.setAcceptanceStatus(1);
        permissionRequest.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest.setUpdateDate(Date.from(atStartOfDayResult3.atZone(ZoneId.systemDefault()).toInstant()));
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest.setStartDate(Date.from(atStartOfDayResult4.atZone(ZoneId.systemDefault()).toInstant()));
        when(this.permissionRequestService.updatePermissionRequest((PermissionRequest) any()))
                .thenReturn(permissionRequest);

        PartialUser partialUser1 = new PartialUser();
        partialUser1.setLastName("Doe");
        partialUser1.setEmail("jane.doe@example.org");
        partialUser1.setRoles(new ArrayList<Role>());
        partialUser1.setUsername("janedoe");
        partialUser1.setId(123L);
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        partialUser1.setHireDate(Date.from(atStartOfDayResult5.atZone(ZoneId.systemDefault()).toInstant()));
        partialUser1.setFirstName("Jane");
        partialUser1.setExist(true);

        PermissionRequest permissionRequest1 = new PermissionRequest();
        LocalDateTime atStartOfDayResult6 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest1.setEndDate(Date.from(atStartOfDayResult6.atZone(ZoneId.systemDefault()).toInstant()));
        permissionRequest1.setStatus("Status");
        LocalDateTime atStartOfDayResult7 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest1.setRequestDate(Date.from(atStartOfDayResult7.atZone(ZoneId.systemDefault()).toInstant()));
        permissionRequest1.setUser(partialUser1);
        permissionRequest1.setId(123L);
        permissionRequest1.setAcceptanceStatus(1);
        permissionRequest1.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult8 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest1.setUpdateDate(Date.from(atStartOfDayResult8.atZone(ZoneId.systemDefault()).toInstant()));
        LocalDateTime atStartOfDayResult9 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest1.setStartDate(Date.from(atStartOfDayResult9.atZone(ZoneId.systemDefault()).toInstant()));
        String content = (new ObjectMapper()).writeValueAsString(permissionRequest1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/permission-request/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.permissionRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"user\":{\"id\":123,\"username\":\"janedoe\",\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe"
                                        + "@example.org\",\"hireDate\":-7200000,\"roles\":[],\"exist\":true},\"startDate\":-7200000,\"endDate\":-7200000,"
                                        + "\"description\":\"The characteristics of someone or something\",\"requestDate\":-7200000,\"updateDate\":-7200000"
                                        + ",\"acceptanceStatus\":1,\"status\":\"Status\"}"));
    }

    @Test
    public void testAddPermissionRequest2() throws Exception {
        doNothing().when(this.permissionRequestService).deletePermissionRequestById((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/permission-request/delete/{id}",
                123L);
        MockMvcBuilders.standaloneSetup(this.permissionRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testAddPermissionRequest3() throws Exception {
        doNothing().when(this.permissionRequestService).deletePermissionRequestById((Long) any());
        MockHttpServletRequestBuilder deleteResult = MockMvcRequestBuilders.delete("/api/permission-request/delete/{id}",
                123L);
        deleteResult.contentType("Not all who wander are lost");
        MockMvcBuilders.standaloneSetup(this.permissionRequestController)
                .build()
                .perform(deleteResult)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetPermissionRequestById() throws Exception {
        PartialUser partialUser = new PartialUser();
        partialUser.setLastName("Doe");
        partialUser.setEmail("jane.doe@example.org");
        partialUser.setRoles(new ArrayList<Role>());
        partialUser.setUsername("janedoe");
        partialUser.setId(123L);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        partialUser.setHireDate(Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()));
        partialUser.setFirstName("Jane");
        partialUser.setExist(true);

        PermissionRequest permissionRequest = new PermissionRequest();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest.setEndDate(Date.from(atStartOfDayResult1.atZone(ZoneId.systemDefault()).toInstant()));
        permissionRequest.setStatus("Status");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest.setRequestDate(Date.from(atStartOfDayResult2.atZone(ZoneId.systemDefault()).toInstant()));
        permissionRequest.setUser(partialUser);
        permissionRequest.setId(123L);
        permissionRequest.setAcceptanceStatus(1);
        permissionRequest.setDescription("The characteristics of someone or something");
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest.setUpdateDate(Date.from(atStartOfDayResult3.atZone(ZoneId.systemDefault()).toInstant()));
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        permissionRequest.setStartDate(Date.from(atStartOfDayResult4.atZone(ZoneId.systemDefault()).toInstant()));
        when(this.permissionRequestService.getPermissionById((Long) any())).thenReturn(permissionRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/permission-request/{id}", 123L);
        MockMvcBuilders.standaloneSetup(this.permissionRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"user\":{\"id\":123,\"username\":\"janedoe\",\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe"
                                        + "@example.org\",\"hireDate\":-7200000,\"roles\":[],\"exist\":true},\"startDate\":-7200000,\"endDate\":-7200000,"
                                        + "\"description\":\"The characteristics of someone or something\",\"requestDate\":-7200000,\"updateDate\":-7200000"
                                        + ",\"acceptanceStatus\":1,\"status\":\"Status\"}"));
    }

    @Test
    public void testUpdateAcceptanceStatus() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/permission-request/{id}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.permissionRequestController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}

