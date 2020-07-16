package domainapp.modules.simple.dom.company;

import static org.apache.isis.applib.annotation.SemanticsOf.IDEMPOTENT;
import static org.apache.isis.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

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
import org.apache.isis.applib.annotation.CommandReification;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.PromptStyle;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.jaxbadapters.PersistentEntityAdapter;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;
import org.apache.isis.applib.value.Blob;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.lead.Lead;
import domainapp.modules.simple.dom.lead.Leads;
import domainapp.modules.simple.dom.leadSource.LeadSource;
import domainapp.modules.simple.types.Attachment;
import domainapp.modules.simple.types.EmailAddress;
import domainapp.modules.simple.types.Name;
import domainapp.modules.simple.types.Notes;
import domainapp.modules.simple.types.WebsiteUrl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@javax.jdo.annotations.Unique(name="Company_name_UNQ", members = {"name"})
@DomainObject(bounding = Bounding.BOUNDED)
@DomainObjectLayout()
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Company implements Comparable<Company> {

    public static Company withName(String name) {
        val simpleObject = new Company();
        simpleObject.setName(name);
        return simpleObject;
    }

    public static class ActionDomainEvent extends SimpleModule.ActionDomainEvent<Company> {}

    @Inject RepositoryService repositoryService;
    @Inject TitleService titleService;
    @Inject MessageService messageService;
    @Inject Leads leadService;

    private Company() {
    }

    @Title
    @Name
    @MemberOrder(sequence = "0")
    @Getter @Setter @ToString.Include
    private String name;

    @Notes
    @Getter @Setter
    private String notes;
    
    @EmailAddress
    @Getter @Setter
    private String emailAddress;
    
    @WebsiteUrl
    @Getter @Setter
    private String websiteUrl;
    
    @Attachment
    @Getter @Setter
    private Blob logoFile;
    public static class UpdateLogoFileActionDomainEvent extends Company.ActionDomainEvent {}
    @Action(semantics = IDEMPOTENT,
            command = CommandReification.ENABLED, publishing = Publishing.ENABLED,
            associateWith = "logoFile", domainEvent = UpdateLogoFileActionDomainEvent.class)
    public Company updateLogoFile(
            @Name final Blob file) {
        setLogoFile(file);
        return this;
    }
    
    @Collection
    @Persistent(mappedBy = "company")
    @Getter @Setter
    private SortedSet<Lead> leads = new TreeSet<>();
    @Action(associateWith = "leads", semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_MODAL)
    public Company createLead(
    		@Parameter LeadSource leadSource, 
    		@Parameter @Notes String leadNotes) {
    	final Lead lead = leadService.create(leadSource, leadNotes);
        final String title = titleService.titleOf(lead);
        messageService.informUser(String.format("Created '%s'", title));
    	this.getLeads().add(lead);
        repositoryService.persistAndFlush(lead);
    	return this;
    }
    @Action(associateWith = "leads")
    public Company removeLead(final Lead lead) {
    	this.getLeads().remove(lead);
    	return this;
    }

    public static class UpdateNameActionDomainEvent extends Company.ActionDomainEvent {}
    @Action(semantics = IDEMPOTENT,
            command = CommandReification.ENABLED, publishing = Publishing.ENABLED,
            associateWith = "name", domainEvent = UpdateNameActionDomainEvent.class)
    public Company updateName(
            @Name final String name) {
        setName(name);
        return this;
    }

    public String default0UpdateName() {
        return getName();
    }

    public static class DeleteActionDomainEvent extends Company.ActionDomainEvent {}
    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE, domainEvent = DeleteActionDomainEvent.class)
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }

    private final static Comparator<Company> comparator =
            Comparator.comparing(Company::getName);

    @Override
    public int compareTo(final Company other) {
        return comparator.compare(this, other);
    }

}