package it.idIta.idempiere.LIT_ServerSilosREST.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import it.idIta.idempiere.LIT_ServerSilosREST.model.Silos;
import it.idIta.idempiere.LIT_ServerSilosREST.repository.SilosRepository;

@RestController
public class SilosController {
	
	@Autowired
	SilosRepository silosRepository;
	
	@GetMapping(value="isiplast/Silos")
	public ResponseEntity<List<Silos>> getAllSilos() {
		try {
			List<Silos> allSilos = new ArrayList<Silos>();
			silosRepository.findAll().forEach(allSilos::add);
			
			if(allSilos.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			return new ResponseEntity<>(allSilos, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
