package domainapp.modules.simple.fixture.customer;

import java.util.Optional;

import org.apache.isis.applib.services.registry.ServiceRegistry;
import org.apache.isis.testing.fixtures.applib.api.PersonaWithBuilderScript;
import org.apache.isis.testing.fixtures.applib.api.PersonaWithFinder;
import org.apache.isis.testing.fixtures.applib.setup.PersonaEnumPersistAll;

import domainapp.modules.simple.dom.customer.Customer;
import domainapp.modules.simple.dom.customer.Customers;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Customer_persona 
implements PersonaWithBuilderScript<CustomerBuilder>, PersonaWithFinder<Customer> {

    JOHN_DOE("John", "Doe"),
    JANE_DOE("Jane", "Doe");

    private final String firstName;
    private final String lastName;

    @Override
    public CustomerBuilder builder() {
        return new CustomerBuilder().setFirstName(firstName).setLastName(lastName);
    }

    @Override
    public Customer findUsing(final ServiceRegistry serviceRegistry) {
        Customers service = serviceRegistry.lookupService(Customers.class).orElse(null);
        Optional<Customer> opt = service.findByLastName(lastName).stream().filter(c -> c.getFirstName().contentEquals(firstName)).findFirst();
        return opt.orElse(null);
    }

    public static class PersistAll
    extends PersonaEnumPersistAll<Customer_persona, Customer> {

        public PersistAll() {
            super(Customer_persona.class);
        }
    }
}
