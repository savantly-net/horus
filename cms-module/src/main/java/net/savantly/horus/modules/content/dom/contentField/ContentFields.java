package net.savantly.horus.modules.content.dom.contentField;

import java.util.List;

import javax.inject.Inject;
import javax.jdo.JDOQLTypedQuery;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.PromptStyle;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.persistence.jdo.applib.services.IsisJdoSupport_v3_2;

import net.savantly.horus.modules.content.ContentModule;
import net.savantly.horus.modules.content.dom.contentItem.ContentItem;
import net.savantly.horus.modules.content.dom.contentItem.QContentItem;
import net.savantly.horus.modules.content.types.FieldType;

@DomainService(
        nature = NatureOfService.VIEW,
        objectType = "cms.ContentFields"
        )
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class ContentFields {

	private final RepositoryService repositoryService;
	@Inject private IsisJdoSupport_v3_2 isisJdoSupport;

    public static class ActionDomainEvent extends ContentModule.ActionDomainEvent<ContentField> {}

    public static class CreateActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, domainEvent = CreateActionDomainEvent.class)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_MODAL, named = "Create Content Field")
    public ContentField create(
    		@Parameter final String name, 
    		@Parameter final FieldType fieldType) {
    	ContentField item = ContentField.withFields(name, fieldType);
        return repositoryService.persist(item);
    }
    
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "List All Content Fields")
    public List<ContentField> listAll() {
        return repositoryService.allInstances(ContentField.class);
    }
    
    @Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Find Content Field by ID")
	public ContentField findById(String id) {
		try {
		return isisJdoSupport.getJdoPersistenceManager().getObjectById(ContentField.class, id);
		} catch (Exception ex) {
			return null;
		}
	}
}
