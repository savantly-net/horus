package domainapp.modules.content.fixture.contentType;

import javax.inject.Inject;

import org.apache.isis.testing.fixtures.applib.fixturescripts.BuilderScriptWithResult;

import domainapp.modules.content.contentType.ContentType;
import domainapp.modules.content.contentType.ContentTypes;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class ContentTypeBuilder extends BuilderScriptWithResult<ContentType> {

    @Getter @Setter
    private String name;

    @Override
    protected ContentType buildResult(final ExecutionContext ec) {
        
        checkParam("name", ec, String.class);
        
        return wrap(simpleObjects).createContentType(name);
    }
    
    // -- DEPENDENCIES

    @Inject ContentTypes simpleObjects;

}
