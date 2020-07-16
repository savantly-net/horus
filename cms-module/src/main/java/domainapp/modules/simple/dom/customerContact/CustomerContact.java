package domainapp.modules.simple.dom.customerContact;

import static org.apache.isis.applib.annotation.SemanticsOf.IDEMPOTENT;

import java.util.Comparator;

import javax.inject.Inject;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.CommandReification;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.jaxbadapters.PersistentEntityAdapter;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;
import org.joda.time.DateTime;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.customer.Customer;
import domainapp.modules.simple.types.ContactType;
import domainapp.modules.simple.types.Notes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@DomainObject()
@DomainObjectLayout()
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class CustomerContact implements Comparable<CustomerContact> {
	
	public static CustomerContact withFields(String notes, ContactType contactType, Customer customer) {
        val obj = new CustomerContact();
        obj.setContactType(contactType);
        obj.setDate(DateTime.now());
        obj.setNotes(notes);
        obj.setCustomer(customer);
        return obj;
    }
	
	@Notes
	@Getter @Setter
	private String notes;

	@Title(sequence = "0", append = " ")
	@Getter @Setter
	private ContactType contactType;

	@Title(sequence = "1")
    @MemberOrder(sequence = "0")
    @Getter @Setter
    private DateTime date;
	
	@Getter @Setter
    private Customer customer;
    public static class UpdateCustomerActionDomainEvent extends CustomerContact.ActionDomainEvent {}
    @Action(semantics = IDEMPOTENT,
            command = CommandReification.ENABLED, publishing = Publishing.ENABLED,
            associateWith = "customer", domainEvent = UpdateCustomerActionDomainEvent.class)
    public CustomerContact updateCustomer(final Customer customer) {
        setCustomer(customer);
        return this;
    }


    public static class ActionDomainEvent extends SimpleModule.ActionDomainEvent<CustomerContact> {}

    @Inject RepositoryService repositoryService;
    @Inject TitleService titleService;
    @Inject MessageService messageService;

    private CustomerContact() {
    }
    
    private final static Comparator<CustomerContact> comparator =
    		Comparator.comparing(CustomerContact::getDate);

    @Override
    public int compareTo(final CustomerContact other) {
        return comparator.compare(this, other);
    }
}