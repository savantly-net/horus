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

@Column(length = WebsiteUrl.MAX_LEN, allowsNull = "true")
@Property(editing = Editing.ENABLED, maxLength = WebsiteUrl.MAX_LEN)
@PropertyLayout(named = "Website Url")
@Parameter(maxLength = WebsiteUrl.MAX_LEN)
@ParameterLayout(named = "Website Url")
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface WebsiteUrl {
	int MAX_LEN = 500;
}
