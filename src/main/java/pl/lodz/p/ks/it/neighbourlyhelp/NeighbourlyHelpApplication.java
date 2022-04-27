package pl.lodz.p.ks.it.neighbourlyhelp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NeighbourlyHelpApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeighbourlyHelpApplication.class, args);
	}

}
