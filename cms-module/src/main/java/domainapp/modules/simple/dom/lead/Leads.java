package domainapp.modules.simple.dom.lead;

import java.util.List;

import javax.inject.Inject;
import javax.jdo.JDOQLTypedQuery;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.PromptStyle;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.persistence.jdo.applib.services.IsisJdoSupport_v3_2;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.leadSource.LeadSource;
import domainapp.modules.simple.types.Notes;

@DomainService(
        nature = NatureOfService.VIEW,
        objectType = "simple.Leads"
        )
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class Leads {

    private final RepositoryService repositoryService;
    private final IsisJdoSupport_v3_2 isisJdoSupport;

    public static class ActionDomainEvent extends SimpleModule.ActionDomainEvent<Leads> {}

    public static class CreateActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, domainEvent = CreateActionDomainEvent.class)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Lead create(
    		final LeadSource source,
            @Notes final String notes) {
        return repositoryService.persist(Lead.withFields(source, notes));
    }

    public static class ListAllActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<Lead> listAll() {
        return repositoryService.allInstances(Lead.class);
    }

    @Programmatic
    public void ping() {
        JDOQLTypedQuery<Lead> q = isisJdoSupport.newTypesafeQuery(Lead.class);
        final QLead candidate = QLead.candidate();
        q.range(0,2);
        q.executeList();
    }


}