package com.harunergul.permission.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.harunergul.permission.model.PermissionRequest;
import com.harunergul.permission.service.PermissionRequestService;

@RestController
@RequestMapping("/api/permission-request")
public class PermissionRequestController {

	@Autowired
	private PermissionRequestService permissionRequestService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<PermissionRequest>  getPermissionRequestById(@PathVariable Long id) {
		return new ResponseEntity<PermissionRequest>(permissionRequestService.getPermissionById(id), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<Iterable<PermissionRequest>> getAllPermissionRequests() {
		return new ResponseEntity<Iterable<PermissionRequest>>(permissionRequestService.getAllPermissionRequests(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/all/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<PermissionRequest>> getAllPermissionsByUser(@PathVariable Long id) {
		return new ResponseEntity<>(permissionRequestService.getAllPermissionsByUser(id), HttpStatus.OK);
	}
	
	@PostMapping(value = "/add")
	public ResponseEntity<PermissionRequest> addPermissionRequest(@RequestBody PermissionRequest request) {
		return new ResponseEntity<PermissionRequest>(permissionRequestService.addPermissionRequest(request), HttpStatus.OK);
	}
	
	@PostMapping(value = "/update")
	public ResponseEntity<PermissionRequest> updatePermissionRequest(@RequestBody PermissionRequest request) {
		return new ResponseEntity<PermissionRequest>(permissionRequestService.updatePermissionRequest(request), HttpStatus.OK);
	}
	
	
	@PatchMapping(value = "{id}")
	public ResponseEntity<?> updateAcceptanceStatus(@PathVariable Long id, @RequestBody Map<Object, Object> fields) {
		return permissionRequestService.updateAcceptanceStatus(id, fields);
	}
	
	
	
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?>  addPermissionRequest(@PathVariable Long id) {
		permissionRequestService.deletePermissionRequestById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
