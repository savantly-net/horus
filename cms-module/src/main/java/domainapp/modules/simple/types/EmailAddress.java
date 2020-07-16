package domainapp.modules.simple.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.jdo.annotations.Column;

import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;

@Column(length = EmailAddress.MAX_LEN, allowsNull = "true")
@Property(editing = Editing.ENABLED, maxLength = EmailAddress.MAX_LEN)
@PropertyLayout(named = "Email Address")
@Parameter(maxLength = EmailAddress.MAX_LEN)
@ParameterLayout(named = "Email Address")
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailAddress {
	int MAX_LEN = 50;
}
