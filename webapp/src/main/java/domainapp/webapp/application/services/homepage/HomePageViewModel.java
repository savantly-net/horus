package domainapp.webapp.application.services.homepage;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.HomePage;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.services.i18n.TranslatableString;

import domainapp.modules.simple.dom.lead.Lead;
import domainapp.modules.simple.dom.lead.Leads;

@DomainObject(
        nature = Nature.VIEW_MODEL,
        objectType = "domainapp.HomePageViewModel"
        )
@HomePage
@DomainObjectLayout()
public class HomePageViewModel {

    public TranslatableString title() {
        return TranslatableString.tr("{num} leads", "num", getLeads().size());
    }

    public List<Lead> getLeads() {
        return simpleObjects.listAll();
    }

    @Inject Leads simpleObjects;
}
