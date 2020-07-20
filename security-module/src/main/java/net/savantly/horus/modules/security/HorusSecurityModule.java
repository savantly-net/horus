package net.savantly.horus.modules.security;

import org.apache.isis.extensions.secman.api.SecurityModuleConfig;
import org.apache.isis.extensions.secman.api.permission.PermissionsEvaluationServiceAllowBeatsVeto;
import org.apache.isis.extensions.secman.encryption.jbcrypt.IsisModuleExtSecmanEncryptionJbcrypt;
import org.apache.isis.extensions.secman.jdo.IsisModuleExtSecmanPersistenceJdo;
import org.apache.isis.extensions.secman.model.IsisModuleExtSecmanModel;
import org.apache.isis.extensions.secman.shiro.IsisModuleExtSecmanRealmShiro;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@org.springframework.context.annotation.Configuration
@Import({
    // Security Manager Extension (secman)
    IsisModuleExtSecmanModel.class,
    IsisModuleExtSecmanRealmShiro.class,
    IsisModuleExtSecmanPersistenceJdo.class,
    IsisModuleExtSecmanEncryptionJbcrypt.class
})
@ComponentScan
@ConfigurationProperties("horus.modules.security")
public class HorusSecurityModule {

	@Getter @Setter
	private @NonNull String adminUsername = "admin";
	@Getter @Setter
	private @NonNull String adminPassword = "password";

	@Bean
    public SecurityModuleConfig securityModuleConfigBean() {
        return SecurityModuleConfig.builder()
        		.adminUserName(adminUsername)
        		.adminPassword(adminPassword)
                .build();
    }
	
	@Bean
	public PermissionsEvaluationServiceAllowBeatsVeto permissionsEvaluator() {
		return new PermissionsEvaluationServiceAllowBeatsVeto();
	}
	
}
