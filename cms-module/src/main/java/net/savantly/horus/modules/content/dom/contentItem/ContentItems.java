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

import net.savantly.horus.modules.content.ContentModule;

@DomainService(
        nature = NatureOfService.VIEW,
        objectType = "cms.ContentItems"
        )
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class ContentItems {
	
	private final RepositoryService repositoryService;

    public static class ActionDomainEvent extends ContentModule.ActionDomainEvent<ContentItem> {}

    public static class CreateActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, domainEvent = CreateActionDomainEvent.class)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_MODAL)
    public ContentItem createContentItem(final String name) {
    	ContentItem item = ContentItem.withFields(name);
        return repositoryService.persist(item);
    }
    
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<ContentItem> listAllContentItems() {
        return repositoryService.allInstances(ContentItem.class);
    }

}
