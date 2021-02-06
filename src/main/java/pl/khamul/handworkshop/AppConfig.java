package pl.khamul.handworkshop;


import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("pl.khamul")
@EnableJpaRepositories("pl.khamul.handworkshop.repository")
@EnableTransactionManagement
@ServletComponentScan
public class AppConfig {


}
