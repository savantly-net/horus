package domainapp.modules.simple.dom.location;

import static org.apache.isis.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.jaxbadapters.PersistentEntityAdapter;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.company.Company;
import domainapp.modules.simple.dom.customer.Customer;
import domainapp.modules.simple.types.Name;
import domainapp.modules.simple.types.TextLine;
import domainapp.modules.simple.types.USState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@DomainObject(editing = Editing.ENABLED)
@DomainObjectLayout()
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Location implements Comparable<Location> {

    public static Location withName(String name) {
        val simpleObject = new Location();
        simpleObject.setName(name);
        return simpleObject;
    }

    public static class ActionDomainEvent extends SimpleModule.ActionDomainEvent<Company> {}

    @Inject RepositoryService repositoryService;
    @Inject TitleService titleService;
    @Inject MessageService messageService;

    private Location() {
    }

    public String title() {
        return String.format("%s - %s %s", getName(), getAddress1(), getAddress2());
    }

    @Name
    @MemberOrder(sequence = "0")
    @Getter @Setter @ToString.Include
    private String name;

    @TextLine
    @Getter @Setter
    private String address1;
    
    @TextLine
    @Getter @Setter
    private String address2;

    @TextLine
    @Getter @Setter
    private String city;

    @Getter @Setter
    private USState state;

    @javax.jdo.annotations.Column(length = 10)
    @Property(regexPattern = "^\\d{5}(?:[-\\s]\\d{4})?$")
    @Getter
    @Setter
    private String zipCode;
    
    @Collection
    @Getter
    @Setter
    private Set<Customer> customers = new HashSet<>();
    

    public static class DeleteActionDomainEvent extends Company.ActionDomainEvent {}
    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE, domainEvent = DeleteActionDomainEvent.class)
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }

    private final static Comparator<Location> comparator =
            Comparator.comparing(Location::getName);

    @Override
    public int compareTo(final Location other) {
        return comparator.compare(this, other);
    }

}