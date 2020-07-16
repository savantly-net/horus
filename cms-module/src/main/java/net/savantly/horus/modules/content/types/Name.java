package domainapp.modules.content.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Inject;
import javax.jdo.annotations.Column;

import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.spec.AbstractSpecification2;

import domainapp.modules.content.ContentModule;

@Column(length = Name.MAX_LEN, allowsNull = "false")
@Property(mustSatisfy = Name.Specification.class, maxLength = Name.MAX_LEN)
@Parameter(mustSatisfy = Name.Specification.class, maxLength = Name.MAX_LEN)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {

    int MAX_LEN = 40;

    class Specification extends AbstractSpecification2<String> {

        @Inject
        private ContentModule.Configuration configuration;

        @Override
        public TranslatableString satisfiesTranslatableSafely(final String name) {
            return null;
        }
    }
}
