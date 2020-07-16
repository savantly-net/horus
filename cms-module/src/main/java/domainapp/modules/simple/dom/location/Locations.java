package domainapp.modules.simple.dom.location;

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
import domainapp.modules.simple.types.Name;

@DomainService(
        nature = NatureOfService.VIEW,
        objectType = "simple.Locations"
        )
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class Locations {

    private final RepositoryService repositoryService;
    private final IsisJdoSupport_v3_2 isisJdoSupport;

    public static class ActionDomainEvent extends SimpleModule.ActionDomainEvent<Locations> {}

    public static class CreateActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, domainEvent = CreateActionDomainEvent.class)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Location create(
            @Name final String name) {
        return repositoryService.persist(Location.withName(name));
    }

    public static class FindByAddressActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.SAFE, domainEvent = FindByAddressActionDomainEvent.class)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public List<Location> findByAddress(
            @Name final String address
            ) {
        JDOQLTypedQuery<Location> q = isisJdoSupport.newTypesafeQuery(Location.class);
        final QLocation cand = QLocation.candidate();
        q = q.filter(
                cand.address1.indexOf(q.stringParameter("address1")).ne(-1)
                );
        return q.setParameter("address1", address)
                .executeList();
    }

    public static class ListAllActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<Location> listAll() {
        return repositoryService.allInstances(Location.class);
    }

    @Programmatic
    public void ping() {
        JDOQLTypedQuery<Location> q = isisJdoSupport.newTypesafeQuery(Location.class);
        final QLocation candidate = QLocation.candidate();
        q.range(0,2);
        q.orderBy(candidate.name.asc());
        q.executeList();
    }


}
