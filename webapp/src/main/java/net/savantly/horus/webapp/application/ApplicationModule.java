package domainapp.webapp.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import domainapp.modules.content.ContentModule;

@Configuration
@Import({ContentModule.class})
@ComponentScan
public class ApplicationModule {

}
