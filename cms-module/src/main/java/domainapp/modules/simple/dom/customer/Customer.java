package domainapp.modules.simple.dom.customer;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Bounding;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.PromptStyle;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.jaxbadapters.PersistentEntityAdapter;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;
import org.apache.isis.applib.services.wrapper.WrapperFactory;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.customerContact.CustomerContact;
import domainapp.modules.simple.dom.customerContact.CustomerContacts;
import domainapp.modules.simple.dom.location.Location;
import domainapp.modules.simple.dom.location.Locations;
import domainapp.modules.simple.dom.phone.PhoneEntries;
import domainapp.modules.simple.dom.phone.PhoneEntry;
import domainapp.modules.simple.types.ContactType;
import domainapp.modules.simple.types.EmailAddress;
import domainapp.modules.simple.types.Name;
import domainapp.modules.simple.types.Notes;
import domainapp.modules.simple.types.PhoneLabel;
import domainapp.modules.simple.types.USState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@DomainObject(editing = Editing.ENABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout()
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Customer implements Comparable<Customer> {

    public static Customer withName(String firstName, String lastName) {
        val simpleObject = new Customer();
        simpleObject.setFirstName(firstName);
        simpleObject.setLastName(lastName);
        return simpleObject;
    }

    public static class ActionDomainEvent extends SimpleModule.ActionDomainEvent<Customer> {}

    @Inject RepositoryService repositoryService;
    @Inject TitleService titleService;
    @Inject MessageService messageService;

    private Customer() {
    }

    public String title() {
        return String.format("%s %s", getFirstName(), getLastName());
    }

    @Name
    @MemberOrder(sequence = "0")
    @Getter @Setter @ToString.Include
    private String firstName;
    
    @Name
    @MemberOrder(sequence = "1")
    @Getter @Setter @ToString.Include
    private String lastName;
    
    @EmailAddress
    @Getter @Setter
    private String emailAddress;
    
    @Collection
    @Getter @Setter
    @MemberOrder(sequence = "2")
    private SortedSet<Location> locations = new TreeSet<>();
    public void addToLocations(final Location location) {
        getLocations().add(location);
    }
    public void removeFromLocations(final Location location) {
        getLocations().remove(location);
    }
    @Action(associateWith = "locations", associateWithSequence = "2")
    public Customer addLocation(final Location location) {
        // By wrapping the call, Isis will detect that the collection is modified
        // and it will automatically send CollectionInteractionEvents to the Event Bus.
        this.addToLocations(location);
        return this;
    }
    @Action(associateWith = "locations", associateWithSequence = "2")
    public Customer removeLocation(final Location location) {
        // By wrapping the call, Isis will detect that the collection is modified
        // and it will automatically send a CollectionInteractionEvent to the Event Bus.
        this.removeFromLocations(location);
        return this;
    }
    @Action
    @ActionLayout(cssClassFa = "fa fa-plus")
    @MemberOrder(sequence = "5")
    public Location newLocation(
            @Parameter(maxLength = 100) final String name,
            @Parameter(maxLength = 100) final String address, 
            @Parameter(maxLength = 100, optionality=Optionality.OPTIONAL) final String address2, 
            @Parameter(maxLength = 100) final String city,
            @Parameter(maxLength = 2) final USState state,
            @Parameter(maxLength = 5) final String zipCode) {
    	Location location = locationService.create(name);
        location.setAddress1(address);
        location.setAddress2(address2);
        location.setCity(city);
        location.setState(state);
        location.setZipCode(zipCode);
        location.getCustomers().add(this);
        repositoryService.persistAndFlush(location);
        addLocation(location);
        return location;
    }

    @Collection
    @Getter @Setter
    @MemberOrder(name = "phoneEntries", sequence = "3")
    private SortedSet<PhoneEntry> phoneEntries = new TreeSet<>();
    public void addToPhoneEntries(final PhoneEntry phoneEntry) {
    	this.getPhoneEntries().add(phoneEntry);
    }
    public void removeFromPhoneEntries(final PhoneEntry phoneEntry) {
    	this.getPhoneEntries().remove(phoneEntry);
    }
    @Action(associateWith = "phoneEntries", associateWithSequence = "3")
    @MemberOrder(name = "phoneEntries", sequence = "0")
    public Customer addPhoneEntry(final PhoneEntry phoneEntry) {
    	addToPhoneEntries(phoneEntry);
    	return this;
    }
    @Action(associateWith = "phoneEntries", associateWithSequence = "3")
    public Customer removePhoneEntry(final PhoneEntry phoneEntry) {
    	removeFromPhoneEntries(phoneEntry);
    	return this;
    }
    @Action
    @ActionLayout(cssClassFa = "fa fa-plus")
    @MemberOrder(sequence = "6")
    public PhoneEntry newPhoneEntry(
            @Parameter final PhoneLabel label,
            @Parameter(maxLength = 10) final String number) {
    	PhoneEntry obj = phoneEntryService.create(label, number);
        repositoryService.persistAndFlush(obj);
        addPhoneEntry(obj);
        return obj;
    }

    @Collection
    @Persistent(mappedBy = "customer")
    @Getter @Setter
    private SortedSet<CustomerContact> contacts = new TreeSet<>();
    @Action(associateWith = "contacts", semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_MODAL)
    public Customer createContact(
    		@Parameter ContactType contactType, 
    		@Parameter @Notes String notes) {
    	final CustomerContact obj = contactService.create(contactType, notes, this);
    	final String title = titleService.titleOf(obj);
        messageService.informUser(String.format("Created '%s'", title));
    	this.getContacts().add(obj);
        repositoryService.persistAndFlush(obj);
    	return this;
    }

    public static class DeleteActionDomainEvent extends Customer.ActionDomainEvent {}
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE, domainEvent = DeleteActionDomainEvent.class)
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }
    
    

    private final static Comparator<Customer> comparator =
            Comparator.comparing(Customer::getLastName);

    @Override
    public int compareTo(final Customer other) {
        return comparator.compare(this, other);
    }

    @javax.inject.Inject
    WrapperFactory wrapperFactory;
    @Inject
    Locations locationService;
    @Inject
    PhoneEntries phoneEntryService;
    @Inject
    CustomerContacts contactService;
}