package domainapp.modules.simple.fixture.customer;

import javax.inject.Inject;

import org.apache.isis.testing.fixtures.applib.fixturescripts.BuilderScriptWithResult;

import domainapp.modules.simple.dom.customer.Customer;
import domainapp.modules.simple.dom.customer.Customers;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class CustomerBuilder extends BuilderScriptWithResult<Customer> {

    @Getter @Setter
    private String firstName;
    @Getter @Setter
    private String lastName;

    @Override
    protected Customer buildResult(final ExecutionContext ec) {
        
        checkParam("firstName", ec, String.class);
        checkParam("lastName", ec, String.class);
        
		return wrap(service).create(firstName, lastName);
    }
    
    // -- DEPENDENCIES

    @Inject Customers service;

}
