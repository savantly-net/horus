package domainapp.modules.simple.fixture.leadSource;

import javax.inject.Inject;

import org.apache.isis.testing.fixtures.applib.fixturescripts.BuilderScriptWithResult;

import domainapp.modules.simple.dom.leadSource.LeadSource;
import domainapp.modules.simple.dom.leadSource.LeadSources;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class LeadSourceBuilder extends BuilderScriptWithResult<LeadSource> {

    @Getter @Setter
    private String name;

    @Override
    protected LeadSource buildResult(final ExecutionContext ec) {
        
        checkParam("name", ec, String.class);
        
		return wrap(service).create(name);
    }
    
    // -- DEPENDENCIES

    @Inject LeadSources service;

}
