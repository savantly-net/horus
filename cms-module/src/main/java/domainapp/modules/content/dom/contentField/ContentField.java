package domainapp.modules.content.dom.contentField;

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
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.jaxbadapters.PersistentEntityAdapter;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import domainapp.modules.content.types.FieldType;
import domainapp.modules.simple.SimpleModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "cms")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@DomainObject(bounding = Bounding.BOUNDED)
@DomainObjectLayout()
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class ContentField implements Comparable<ContentField> {
	
	@Title(sequence = "0")
	@Getter @Setter
	private String name;
	
	@Title(prepend = " (", append = ")")
	@Getter @Setter
	private FieldType fieldType;

	public static ContentField withFields(String name, FieldType fieldType) {
        val obj = new ContentField();
        obj.setFieldType(fieldType);
        obj.setName(name);
        return obj;
    }

    public static class ActionDomainEvent extends SimpleModule.ActionDomainEvent<ContentField> {}

    @Inject RepositoryService repositoryService;
    @Inject TitleService titleService;
    @Inject MessageService messageService;

    private ContentField() {
    }
    
    public static class DeleteActionDomainEvent extends ContentField.ActionDomainEvent {}
    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE, domainEvent = DeleteActionDomainEvent.class)
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }

    private final static Comparator<ContentField> comparator =
            Comparator.comparing(ContentField::getName);

    @Override
    public int compareTo(final ContentField other) {
        return comparator.compare(this, other);
    }
}
