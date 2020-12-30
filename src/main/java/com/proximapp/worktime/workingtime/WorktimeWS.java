package com.proximapp.worktime.workingtime;

import com.proximapp.worktime.entity.Movimento;
import com.proximapp.worktime.repo.IAccessRepo;
import com.proximapp.worktime.repo.impl.AccessRepo;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController

public class WorktimeWS {

	private IAccessRepo accessRepo;


	@GetMapping("/registraEntrata")
	public String registraEntrata(@RequestParam(value = "idUser", defaultValue= "1111") long idUser,
								@RequestParam(value = "dataAccesso", defaultValue = "1994-12-12 12:35:55") Timestamp dataAccesso,
								   @RequestParam(value = "entrata", defaultValue = "false") boolean entrata) throws Exception {

		initRepos();

			if(dataAccesso.equals((Timestamp) dataAccesso)){
				accessRepo.addMovimento(idUser, dataAccesso, entrata);
			} else throw new Exception("Errore formato data");

		return String.format("Operazione eseguita con successo.");
	}

	@GetMapping("/mostraMovimenti")
	public String mostraMovimenti() throws Exception {

		initRepos();


		return String.format("Operazione eseguita con successo.");
	}
	@GetMapping("/mostraMovimenti/{data}")
	public String movimentiByData(@RequestParam(value = "data", defaultValue = "1994-12-12") Date data) throws Exception {
		initRepos();
		return String.format("Operazione eseguita con successo.");
	}

	@GetMapping("/mostraMovimenti/{idUtente}")
	public String movimentiById(@RequestParam(value = "idUser", defaultValue = "1111") long idUser) throws Exception {
		initRepos();
		return String.format("Operazione eseguita con successo.");
	}



	private void initRepos() throws SQLException {
		if(accessRepo == null){
			accessRepo = new AccessRepo();
		}
		}
}
            