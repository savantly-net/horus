package net.savantly.horus.modules.content.dom.contentItem;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.PromptStyle;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.value.Clob;
import org.apache.isis.persistence.jdo.applib.services.IsisJdoSupport_v3_2;

import net.savantly.horus.modules.content.ContentModule;
import net.savantly.horus.modules.content.dom.contentType.ContentType;

@DomainService(
        nature = NatureOfService.VIEW,
        objectType = "cms.ContentItems"
        )
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class ContentItems {
	
	private final RepositoryService repositoryService;
	@Inject private IsisJdoSupport_v3_2 isisJdoSupport;

    public static class ActionDomainEvent extends ContentModule.ActionDomainEvent<ContentItem> {}

    public static class CreateActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, domainEvent = CreateActionDomainEvent.class)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_MODAL, named = "Create Content Item")
    public ContentItem create(final String name, final ContentType contentType) {
    	ContentItem item = ContentItem.withFields(name, contentType);
        return repositoryService.persist(item);
    }
    
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "List All Content Items")
    public List<ContentItem> listAll() {
        return repositoryService.allInstances(ContentItem.class);
    }
    
    @Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "Find Content Item by ID")
	public ContentItem findById(String id) {
		try {
		return isisJdoSupport.getJdoPersistenceManager().getObjectById(ContentItem.class, id);
		} catch (Exception ex) {
			return null;
		}
	}

}
