package domainapp.modules.simple.fixture.leadSource;

import java.util.Optional;

import org.apache.isis.applib.services.registry.ServiceRegistry;
import org.apache.isis.testing.fixtures.applib.api.PersonaWithBuilderScript;
import org.apache.isis.testing.fixtures.applib.api.PersonaWithFinder;
import org.apache.isis.testing.fixtures.applib.setup.PersonaEnumPersistAll;

import domainapp.modules.simple.dom.leadSource.LeadSource;
import domainapp.modules.simple.dom.leadSource.LeadSources;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LeadSource_persona 
implements PersonaWithBuilderScript<LeadSourceBuilder>, PersonaWithFinder<LeadSource> {

    EMAIL("Email"),
    PHONE("Phone"),
    CAMPAIGN("Campaign");

    private final String name;

    @Override
    public LeadSourceBuilder builder() {
        return new LeadSourceBuilder().setName(name);
    }

    @Override
    public LeadSource findUsing(final ServiceRegistry serviceRegistry) {
        LeadSources service = serviceRegistry.lookupService(LeadSources.class).orElse(null);
        Optional<LeadSource> opt = service.listAll().stream().filter(s -> s.getName().contentEquals(name)).findFirst();
        return opt.orElse(null);
    }

    public static class PersistAll
    extends PersonaEnumPersistAll<LeadSource_persona, LeadSource> {

        public PersistAll() {
            super(LeadSource_persona.class);
        }
    }
}
