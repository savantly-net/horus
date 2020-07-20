package net.savantly.horus.modules.content.contentType;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Bounding;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.PromptStyle;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.jaxbadapters.PersistentEntityAdapter;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.savantly.horus.modules.content.ContentModule;
import net.savantly.horus.modules.content.dom.contentField.ContentField;
import net.savantly.horus.modules.content.types.FieldType;
import net.savantly.horus.modules.content.types.Name;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "cms")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@javax.jdo.annotations.Unique(name="ContentType_name_UNQ", members = {"name"})
@DomainObject(bounding = Bounding.BOUNDED)
@DomainObjectLayout()
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class ContentType implements Comparable<ContentType> {

	public static ContentType withFields(
			@Name String name) {
		return new ContentType(name);
	}
	public static class ActionDomainEvent extends ContentModule.ActionDomainEvent<ContentType>{}
	
	@Inject RepositoryService repositoryService;
    @Inject TitleService titleService;
    @Inject MessageService messageService;
	
	private ContentType(String name) {
		this.setName(name);
	}
	
	@Title
	@Name
	@Getter @Setter
	private String name;

	@Collection
	@Getter @Setter
	private SortedSet<ContentField> fields = new TreeSet<>();
    @MemberOrder(sequence = "1")
    @Action(associateWith = "fields", semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_MODAL)
    public ContentType addField(final ContentField field) {
        getFields().add(field);
        return this;
    }
    @MemberOrder(sequence = "2")
    @Action(associateWith = "fields", semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_MODAL)
    public ContentType removeField(final ContentField field) {
    	getFields().remove(field);
        return this;
    }
    @MemberOrder(sequence = "0")
    @Action(associateWith = "fields", semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_MODAL)
    public ContentType createField(
    		@Parameter final String name, 
    		@Parameter final FieldType fieldType) {
    	ContentField item = ContentField.withFields(name, fieldType);
    	this.addField(item);
        repositoryService.persist(item);
        return this;
    }
	

	
	public static class DeleteActionDomainEvent extends ContentType.ActionDomainEvent {}
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE, domainEvent = DeleteActionDomainEvent.class)
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }

    private final static Comparator<ContentType> comparator =
            Comparator.comparing(ContentType::getName);

    @Override
    public int compareTo(final ContentType other) {
        return comparator.compare(this, other);
    }
	
}
