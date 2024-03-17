package io.oferto.argo.flow.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.oferto.argo.flow.service.ArgoFlowService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value="/argos", produces = MediaType.APPLICATION_JSON_VALUE)
public class ArgoFlowController {
	private final ArgoFlowService argoFlowService;
	
	@PostMapping(produces = "application/json", value = "/submitFlow")	
	public ResponseEntity<Object> submitFlow(@RequestBody String template) {	
		log.debug("submitFlow: submit flow with template: {}", template);
		
		Object argoResult = argoFlowService.submitFlow(template);
				
		return new ResponseEntity<Object>(argoResult, HttpStatus.OK);			
	}
}
