package jpabook.jpashop;
// h2:  jdbc:h2:tcp://localhost/~/jpashop
// https://rebeccacho.gitbooks.io/spring-study-group/content/chapter1.html

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

}
