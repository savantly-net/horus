package net.savantly.horus.modules.security.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Configuration
@ConfigurationProperties("horus.modules.security")
public class HorusSecurityConfigurationProperties {

	List<String> defaultAdminPackagePermissions = Arrays.asList(
		"domainapp",
		"net.savantly.horus",
		"org.apache.isis"
	);

	@Getter @Setter
	private @NonNull String adminUsername = "admin";
	@Getter @Setter
	private @NonNull String adminPassword = "password";
	@Getter @Setter
	private @NonNull List<String> adminPackagePermissions = defaultAdminPackagePermissions;

	@Getter @Setter
	private @NonNull String anonymousUsername = "anonymous";
	@Getter @Setter
	private @NonNull String anonymousPassword = "password";
}
