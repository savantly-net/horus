package domainapp.modules.simple.fixture.company;

import org.apache.isis.applib.services.registry.ServiceRegistry;
import org.apache.isis.testing.fixtures.applib.api.PersonaWithBuilderScript;
import org.apache.isis.testing.fixtures.applib.api.PersonaWithFinder;
import org.apache.isis.testing.fixtures.applib.setup.PersonaEnumPersistAll;

import domainapp.modules.simple.dom.company.Company;
import domainapp.modules.simple.dom.company.Companies;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Company_persona 
implements PersonaWithBuilderScript<CompanyBuilder>, PersonaWithFinder<Company> {

    FOUNDATION("Foundation Repair Company"),
    ROOFING("Roofing Company"),
    LANDSCAPING("Landscaping Company");

    private final String name;

    @Override
    public CompanyBuilder builder() {
        return new CompanyBuilder().setName(name);
    }

    @Override
    public Company findUsing(final ServiceRegistry serviceRegistry) {
        Companies simpleObjects = serviceRegistry.lookupService(Companies.class).orElse(null);
        return simpleObjects.findByNameExact(name);
    }

    public static class PersistAll
    extends PersonaEnumPersistAll<Company_persona, Company> {

        public PersistAll() {
            super(Company_persona.class);
        }
    }
}
