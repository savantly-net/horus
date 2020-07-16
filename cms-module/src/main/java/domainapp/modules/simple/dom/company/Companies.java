package domainapp.modules.simple.dom.company;

import java.util.List;

import javax.inject.Inject;
import javax.jdo.JDOQLTypedQuery;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.persistence.jdo.applib.services.IsisJdoSupport_v3_2;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.company.QCompany;
import domainapp.modules.simple.types.Name;

@DomainService(
        nature = NatureOfService.VIEW,
        objectType = "simple.Companies"
        )
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class Companies {

    private final RepositoryService repositoryService;
    private final IsisJdoSupport_v3_2 isisJdoSupport;

    public static class ActionDomainEvent extends SimpleModule.ActionDomainEvent<Companies> {}

    public static class CreateActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, domainEvent = CreateActionDomainEvent.class)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Company create(
            @Name final String name) {
        return repositoryService.persist(Company.withName(name));
    }

    public static class FindByNameActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.SAFE, domainEvent = FindByNameActionDomainEvent.class)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public List<Company> findByName(
            @Name final String name
            ) {
        JDOQLTypedQuery<Company> q = isisJdoSupport.newTypesafeQuery(Company.class);
        final QCompany cand = QCompany.candidate();
        q = q.filter(
                cand.name.indexOf(q.stringParameter("name")).ne(-1)
                );
        return q.setParameter("name", name)
                .executeList();
    }

    @Programmatic
    public Company findByNameExact(final String name) {
        JDOQLTypedQuery<Company> q = isisJdoSupport.newTypesafeQuery(Company.class);
        final QCompany cand = QCompany.candidate();
        q = q.filter(
                cand.name.eq(q.stringParameter("name"))
                );
        return q.setParameter("name", name)
                .executeUnique();
    }

    public static class ListAllActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<Company> listAll() {
        return repositoryService.allInstances(Company.class);
    }

    @Programmatic
    public void ping() {
        JDOQLTypedQuery<Company> q = isisJdoSupport.newTypesafeQuery(Company.class);
        final QCompany candidate = QCompany.candidate();
        q.range(0,2);
        q.orderBy(candidate.name.asc());
        q.executeList();
    }


}
