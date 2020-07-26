package net.savantly.horus.webapp.application.services.homepage;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.HomePage;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.Title;

import lombok.Getter;
import lombok.Setter;

@DomainObject(
        nature = Nature.VIEW_MODEL,
        objectType = "horus.HomePageViewModel"
        )
@HomePage
@DomainObjectLayout()
public class HomePageViewModel {
	
	@Title
	@Getter @Setter
	private String title = "Home";
	
}
