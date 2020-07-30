package net.savantly.horus.modules.security;

import org.apache.isis.extensions.secman.api.SecurityModuleConfig;
import org.apache.isis.extensions.secman.api.SecurityModuleConfig.SecurityModuleConfigBuilder;
import org.apache.isis.extensions.secman.api.permission.PermissionsEvaluationServiceVetoBeatsAllow;
import org.apache.isis.extensions.secman.encryption.jbcrypt.IsisModuleExtSecmanEncryptionJbcrypt;
import org.apache.isis.extensions.secman.jdo.IsisModuleExtSecmanPersistenceJdo;
import org.apache.isis.extensions.secman.model.IsisModuleExtSecmanModel;
import org.apache.isis.extensions.secman.shiro.IsisModuleExtSecmanRealmShiro;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import net.savantly.horus.modules.security.config.HorusSecurityConfigurationProperties;

@org.springframework.context.annotation.Configuration
@Import({
    // Security Manager Extension (secman)
    IsisModuleExtSecmanModel.class,
    IsisModuleExtSecmanRealmShiro.class,
    IsisModuleExtSecmanPersistenceJdo.class,
    IsisModuleExtSecmanEncryptionJbcrypt.class
})
@ComponentScan
public class HorusSecurityModule {
	
	private final HorusSecurityConfigurationProperties horusSecurityConfiguration;
	
	public HorusSecurityModule(HorusSecurityConfigurationProperties horusSecurityConfiguration) {
		this.horusSecurityConfiguration = horusSecurityConfiguration;
	}

	@Bean
    public SecurityModuleConfig securityModuleConfigBean() {
        SecurityModuleConfigBuilder builder = SecurityModuleConfig.builder()
        		.adminUserName(horusSecurityConfiguration.getAdminUsername())
        		.adminPassword(horusSecurityConfiguration.getAdminPassword())
        		.adminAdditionalPackagePermissions(horusSecurityConfiguration.getAdminPackagePermissions());
        return builder.build();
    }
	
	@Bean
	public PermissionsEvaluationServiceVetoBeatsAllow permissionsEvaluator() {
		return new PermissionsEvaluationServiceVetoBeatsAllow();
	}
}
