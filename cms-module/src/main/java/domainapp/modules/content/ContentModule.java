package domainapp.modules.content;

import org.apache.isis.testing.fixtures.applib.fixturescripts.FixtureScript;
import org.apache.isis.testing.fixtures.applib.modules.ModuleWithFixtures;
import org.apache.isis.testing.fixtures.applib.teardown.TeardownFixtureAbstract;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.validation.annotation.Validated;

import domainapp.modules.content.contentFieldData.ContentFieldData;
import domainapp.modules.content.contentType.ContentType;
import domainapp.modules.content.dom.contentField.ContentField;
import domainapp.modules.content.dom.contentItem.ContentItem;
import lombok.Data;

@org.springframework.context.annotation.Configuration
@Import({})
@ComponentScan
@EnableConfigurationProperties({
	ContentModule.Configuration.class,
})
public class ContentModule implements ModuleWithFixtures {

    @Override
    public FixtureScript getTeardownFixture() {
        return new TeardownFixtureAbstract() {
            @Override
            protected void execute(ExecutionContext executionContext) {
                deleteFrom(ContentFieldData.class);
                deleteFrom(ContentItem.class);
                deleteFrom(ContentField.class);
                deleteFrom(ContentType.class);
            }
        };
    }

    public static class PropertyDomainEvent<S,T>
            extends org.apache.isis.applib.events.domain.PropertyDomainEvent<S,T> {}
    
    public static class CollectionDomainEvent<S,T>
            extends org.apache.isis.applib.events.domain.CollectionDomainEvent<S,T> {}
    
    public static class ActionDomainEvent<S>
            extends org.apache.isis.applib.events.domain.ActionDomainEvent<S> {}

    @ConfigurationProperties("app.content-module")
    @Data
    @Validated
    public static class Configuration {
    }
}
