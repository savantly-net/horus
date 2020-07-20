package net.savantly.horus.webapp.application.services.health;

import javax.inject.Named;

import org.apache.isis.applib.services.health.Health;
import org.apache.isis.applib.services.health.HealthCheckService;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Named("domainapp.HealthCheckServiceImpl")
@Log4j2
public class HealthCheckServiceImpl implements HealthCheckService {

    @Override
    public Health check() {
        try {
            return Health.ok();
        } catch (Exception ex) {
            return Health.error(ex);
        }
    }
}
