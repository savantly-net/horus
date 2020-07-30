package net.savantly.horus.modules.security.config;

import java.util.Optional;

import org.apache.isis.applib.services.registry.ServiceRegistry;
import org.apache.isis.applib.value.Password;
import org.apache.isis.core.runtime.session.IsisSessionFactory;
import org.apache.isis.extensions.secman.api.user.ApplicationUser;
import org.apache.isis.extensions.secman.api.user.ApplicationUserRepository;
import org.apache.isis.extensions.secman.api.user.ApplicationUserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class AnonymousUserConfiguration {
	
	private static final Logger log = LoggerFactory.getLogger(AnonymousUserConfiguration.class);
	
	private final HorusSecurityConfigurationProperties horusSecurityConfiguration;
	private final ServiceRegistry registry;
	
	@EventListener
	@Order(Ordered.LOWEST_PRECEDENCE)
	public void refreshed(ContextRefreshedEvent event) {
		createAnonymousUser();
	}

	private void createAnonymousUser() {
		log.info("ensuring anonymous user exists");
        IsisSessionFactory sessionFactory = registry.lookupServiceElseFail(IsisSessionFactory.class);
        sessionFactory.runAnonymous(()->{
	        val userRepository = registry.lookupServiceElseFail(ApplicationUserRepository.class);
	        userRepository.findByUsername(horusSecurityConfiguration.getAnonymousUsername()).orElse(
	        		userRepository.newLocalUser(
	        				horusSecurityConfiguration.getAnonymousUsername(),
	        				new Password(horusSecurityConfiguration.getAnonymousPassword()), 
	        				ApplicationUserStatus.ENABLED));
        });
		
	}
}
