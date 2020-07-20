package net.savantly.horus.webapp.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import net.savantly.horus.modules.content.ContentModule;
import net.savantly.horus.modules.security.HorusSecurityModule;

@Configuration
@Import({
	ContentModule.class,
	HorusSecurityModule.class
})
@ComponentScan
public class ApplicationModule {

}
