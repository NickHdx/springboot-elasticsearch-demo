package nick.tutorial.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = null;
		try {
			applicationContext = new SpringApplicationBuilder(Application.class).run(args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (applicationContext != null) {
				System.exit(SpringApplication.exit(applicationContext));
			}
		}
	}
}
