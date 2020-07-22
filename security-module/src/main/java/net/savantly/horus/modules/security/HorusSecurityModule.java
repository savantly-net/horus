package net.savantly.horus.modules.security;

import java.util.Arrays;
import java.util.List;

import org.apache.isis.extensions.secman.api.SecurityModuleConfig;
import org.apache.isis.extensions.secman.api.SecurityModuleConfig.SecurityModuleConfigBuilder;
import org.apache.isis.extensions.secman.api.permission.PermissionsEvaluationServiceVetoBeatsAllow;
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
	
	List<String> defaultAdminPackagePermissions = Arrays.asList(
		"domainapp",
		"net.savantly.horus",
		"org.apache.isis.testing"
	);

	@Getter @Setter
	private @NonNull String adminUsername = "admin";
	@Getter @Setter
	private @NonNull String adminPassword = "password";
	@Getter @Setter
	private @NonNull List<String> adminPackagePermissions = defaultAdminPackagePermissions;

	@Bean
    public SecurityModuleConfig securityModuleConfigBean() {
        SecurityModuleConfigBuilder builder = SecurityModuleConfig.builder()
        		.adminUserName(adminUsername)
        		.adminPassword(adminPassword)
        		.adminAdditionalPackagePermissions(adminPackagePermissions);
        return builder.build();
    }
	
	@Bean
	public PermissionsEvaluationServiceVetoBeatsAllow permissionsEvaluator() {
		return new PermissionsEvaluationServiceVetoBeatsAllow();
	}
	
}
