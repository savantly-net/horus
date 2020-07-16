package domainapp.modules.simple.dom.lead;

import static org.apache.isis.applib.annotation.SemanticsOf.IDEMPOTENT;
import static org.apache.isis.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

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
import org.apache.isis.core.metamodel.specloader.SpecificationLoader;
import org.joda.time.DateTime;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.company.Company;
import domainapp.modules.simple.dom.customer.Customer;
import domainapp.modules.simple.dom.leadSource.LeadSource;
import domainapp.modules.simple.types.Notes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.bytebuddy.ByteBuddy;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@DomainObject()
@DomainObjectLayout()
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Lead implements Comparable<Lead> {

    public static Lead withFields(LeadSource source, String notes) {
        val simpleObject = new Lead();
        simpleObject.setDate(DateTime.now());
        simpleObject.setLeadSource(source);
        simpleObject.setNotes(notes);
        return simpleObject;
    }

    public static class ActionDomainEvent extends SimpleModule.ActionDomainEvent<Lead> {}

    @Inject RepositoryService repositoryService;
    @Inject TitleService titleService;
    @Inject MessageService messageService;
    @Inject SpecificationLoader specLoader;

    private Lead() {
    }

    @Title
    @MemberOrder(sequence = "0")
    @Getter @Setter
    private DateTime date;

    @Notes
    @Getter @Setter
    private String notes;
    
    @Getter @Setter
    private LeadSource leadSource;
    
    @Getter @Setter
    private Company company;
    public static class UpdateCompanyActionDomainEvent extends Lead.ActionDomainEvent {}
    @Action(semantics = IDEMPOTENT,
    		
            command = CommandReification.ENABLED, publishing = Publishing.ENABLED,
            associateWith = "company", domainEvent = UpdateCompanyActionDomainEvent.class)
    public Lead updateCompany(final Company company) {
        setCompany(company);
        return this;
    }
    
    @Getter @Setter
    private Customer customer;
    public static class UpdateCustomerActionDomainEvent extends Lead.ActionDomainEvent {}
    @Action(semantics = IDEMPOTENT,
            command = CommandReification.ENABLED, publishing = Publishing.ENABLED,
            associateWith = "customer", domainEvent = UpdateCustomerActionDomainEvent.class)
    public Lead updateCustomer(final Customer customer) {
        setCustomer(customer);
        return this;
    }
    
    /*
    @Setter
    private Object dynamicProp;
    public static class UpdateDynamicPropActionDomainEvent extends Lead.ActionDomainEvent {}
    @Action(semantics = IDEMPOTENT,
            command = CommandReification.ENABLED, publishing = Publishing.ENABLED,
            associateWith = "dynamicProp", domainEvent = UpdateDynamicPropActionDomainEvent.class)
    public Lead updateDynamicProp(final Object dynamicProp) {
        setDynamicProp(dynamicProp);
        return this;
    }
    public Object getDynamicProp() {
    	//new ByteBuddy().redefine(this.getClass()).defineField("testField", type.getType())
		//.getClass()
    	return dynamicProp;
    }
    */

    public static class DeleteActionDomainEvent extends Lead.ActionDomainEvent {}
    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE, domainEvent = DeleteActionDomainEvent.class)
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }

    private final static Comparator<Lead> comparator =
    		Comparator.comparing(Lead::getDate);

    @Override
    public int compareTo(final Lead other) {
        return comparator.compare(this, other);
    }
   

}