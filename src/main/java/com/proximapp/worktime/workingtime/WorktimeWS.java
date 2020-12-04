package com.proximapp.worktime.workingtime;

import com.proximapp.worktime.entity.Movimento;
import com.proximapp.worktime.repo.IAccessRepo;
import com.proximapp.worktime.repo.impl.AccessRepo;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Date;

@RestController

public class WorktimeWS {

	private IAccessRepo accessRepo;

	
	@GetMapping("/registraEntrata")
	public String registraEntrata(@RequestParam(value = "idUser", defaultValue= "1111") long idUser,
								@RequestParam(value = "dataAccesso", defaultValue = "1994-12-12") String dataAccesso,
								   @RequestParam(value = "entrata", defaultValue = "false") boolean entrata) throws SQLException {

		initRepos();
		accessRepo.addMovimento(idUser, dataAccesso, entrata);

		return String.format("Operazione eseguita con successo.");
	}


	private void initRepos() throws SQLException {
		if(accessRepo == null){
			accessRepo = new AccessRepo();
		}
		}
}
            