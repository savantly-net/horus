package domainapp.webapp.application.services.health;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.isis.applib.services.health.Health;
import org.apache.isis.applib.services.health.HealthCheckService;
import org.springframework.stereotype.Service;

import domainapp.modules.simple.dom.company.Companies;
import lombok.extern.log4j.Log4j2;

@Service
@Named("domainapp.HealthCheckServiceImpl")
@Log4j2
public class HealthCheckServiceImpl implements HealthCheckService {

    private final Companies simpleObjects;

    @Inject
    public HealthCheckServiceImpl(Companies simpleObjects) {
        this.simpleObjects = simpleObjects;
    }

    @Override
    public Health check() {
        try {
            simpleObjects.ping();
            return Health.ok();
        } catch (Exception ex) {
            return Health.error(ex);
        }
    }
}
