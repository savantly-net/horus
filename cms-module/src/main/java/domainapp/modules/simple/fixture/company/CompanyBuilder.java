package domainapp.modules.simple.fixture.company;

import javax.inject.Inject;

import org.apache.isis.testing.fixtures.applib.fixturescripts.BuilderScriptWithResult;

import domainapp.modules.simple.dom.company.Company;
import domainapp.modules.simple.dom.company.Companies;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class CompanyBuilder extends BuilderScriptWithResult<Company> {

    @Getter @Setter
    private String name;

    @Override
    protected Company buildResult(final ExecutionContext ec) {
        
        checkParam("name", ec, String.class);
        
        return wrap(simpleObjects).create(name);
    }
    
    // -- DEPENDENCIES

    @Inject Companies simpleObjects;

}
