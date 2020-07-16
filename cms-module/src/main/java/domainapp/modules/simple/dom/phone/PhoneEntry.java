package domainapp.modules.simple.dom.phone;

import static org.apache.isis.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import java.util.Comparator;

import javax.inject.Inject;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Bounding;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.jaxbadapters.PersistentEntityAdapter;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.types.PhoneLabel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "simple")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@DomainObject(bounding = Bounding.BOUNDED)
@DomainObjectLayout()
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class PhoneEntry implements Comparable<PhoneEntry> {

    public static PhoneEntry withFields(PhoneLabel label, String number) {
        val simpleObject = new PhoneEntry();
        simpleObject.setLabel(label);
        simpleObject.setNumber(number);
        return simpleObject;
    }

    public static class ActionDomainEvent extends SimpleModule.ActionDomainEvent<PhoneEntry> {}

    @Inject RepositoryService repositoryService;
    @Inject TitleService titleService;
    @Inject MessageService messageService;

    private PhoneEntry() {
    }

    @MemberOrder(sequence = "0")
    @Getter @Setter @ToString.Include
    private PhoneLabel label;


    @Title
    @Property(maxLength = 10)
    @Getter @Setter
    private String number;

    public static class DeleteActionDomainEvent extends PhoneEntry.ActionDomainEvent {}
    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE, domainEvent = DeleteActionDomainEvent.class)
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }

    private final static Comparator<PhoneEntry> comparator =
    		Comparator.comparing(PhoneEntry::getNumber);

    @Override
    public int compareTo(final PhoneEntry other) {
        return comparator.compare(this, other);
    }

}