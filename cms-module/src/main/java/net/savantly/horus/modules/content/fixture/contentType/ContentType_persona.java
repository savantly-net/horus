package domainapp.modules.content.fixture.contentType;

import org.apache.isis.applib.services.registry.ServiceRegistry;
import org.apache.isis.testing.fixtures.applib.api.PersonaWithBuilderScript;
import org.apache.isis.testing.fixtures.applib.api.PersonaWithFinder;
import org.apache.isis.testing.fixtures.applib.setup.PersonaEnumPersistAll;

import domainapp.modules.content.contentType.ContentType;
import domainapp.modules.content.contentType.ContentTypes;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum ContentType_persona 
implements PersonaWithBuilderScript<ContentTypeBuilder>, PersonaWithFinder<ContentType> {

    TEST1("Test 1"),
    TEST2("Test 2"),
    TEST3("Test 3");

    private final String name;

    @Override
    public ContentTypeBuilder builder() {
        return new ContentTypeBuilder().setName(name);
    }

    @Override
    public ContentType findUsing(final ServiceRegistry serviceRegistry) {
    	ContentTypes simpleObjects = serviceRegistry.lookupService(ContentTypes.class).orElse(null);
        return simpleObjects.listAllContentTypes().stream().filter(o->o.getName().contentEquals(name)).findFirst().get();
    }

    public static class PersistAll
    extends PersonaEnumPersistAll<ContentType_persona, ContentType> {

        public PersistAll() {
            super(ContentType_persona.class);
        }
    }
}
