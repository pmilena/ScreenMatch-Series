/*package com.milena.screenmatch;

import com.milena.screenmatch.principal.Principal;
import com.milena.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ScreenmatchApplication_BACKUP implements CommandLineRunner {

	@Autowired
	private SerieRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(com.milena.screenmatch.ScreenmatchApplication_BACKUP.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal(repository);
		principal.ExibeMenu();
	}

}*/
