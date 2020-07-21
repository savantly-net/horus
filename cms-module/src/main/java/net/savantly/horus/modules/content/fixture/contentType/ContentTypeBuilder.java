package net.savantly.horus.modules.content.fixture.contentType;

import javax.inject.Inject;

import org.apache.isis.testing.fixtures.applib.fixturescripts.BuilderScriptWithResult;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.savantly.horus.modules.content.dom.contentType.ContentType;
import net.savantly.horus.modules.content.dom.contentType.ContentTypes;

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
