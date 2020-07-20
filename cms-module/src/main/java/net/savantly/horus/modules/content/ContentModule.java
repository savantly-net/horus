package net.savantly.horus.modules.content;

import org.apache.isis.testing.fixtures.applib.fixturescripts.FixtureScript;
import org.apache.isis.testing.fixtures.applib.modules.ModuleWithFixtures;
import org.apache.isis.testing.fixtures.applib.teardown.TeardownFixtureAbstract;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.validation.annotation.Validated;

import lombok.Data;
import net.savantly.horus.modules.content.contentType.ContentType;
import net.savantly.horus.modules.content.dom.contentField.ContentField;
import net.savantly.horus.modules.content.dom.contentFieldData.ContentFieldData;
import net.savantly.horus.modules.content.dom.contentItem.ContentItem;

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

    @ConfigurationProperties("horus.modules.content")
    @Data
    @Validated
    public static class Configuration {
    }
}
