package net.savantly.horus.modules.content.dom.contentType;

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
import org.apache.isis.persistence.jdo.applib.services.IsisJdoSupport_v3_2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.savantly.horus.modules.content.ContentModule;

@DomainService(
        nature = NatureOfService.VIEW,
        objectType = "cms.ContentTypes"
        )
public class ContentTypes {
	
	static private Logger log = LoggerFactory.getLogger(ContentTypes.class);

	@Inject private RepositoryService repositoryService;
	@Inject private IsisJdoSupport_v3_2 isisJdoSupport;
	
	public static class ActionDomainEvent extends ContentModule.ActionDomainEvent<ContentType> {}

	public static class CreateActionDomainEvent extends ActionDomainEvent {}

	@Action(semantics = SemanticsOf.NON_IDEMPOTENT, domainEvent = CreateActionDomainEvent.class)
	@ActionLayout(promptStyle = PromptStyle.DIALOG_MODAL)
	public ContentType create(String name) {
		ContentType item = ContentType.withFields(name);
	    return repositoryService.persist(item);
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	public List<ContentType> listAll() {
	    return repositoryService.allInstances(ContentType.class);
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	public ContentType findById(String id) {
		try {
		return isisJdoSupport.getJdoPersistenceManager().getObjectById(ContentType.class, id);
		} catch (Exception ex) {
			log.info("ContentType: %s was not found", id);
			return null;
		}
		/*
		JDOQLTypedQuery<ContentType> q = isisJdoSupport.newTypesafeQuery(ContentType.class);
        final QContentType cand = QContentType.candidate();
        q = q.filter(
                cand.variable("id").eq(q.stringParameter("id"))
                );
        return q.setParameter("id", id).executeUnique();
        */
	}

}
