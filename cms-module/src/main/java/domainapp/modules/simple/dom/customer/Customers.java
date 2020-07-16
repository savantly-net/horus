package domainapp.modules.simple.dom.customer;

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
        objectType = "simple.Customers"
        )
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class Customers {

    private final RepositoryService repositoryService;
    private final IsisJdoSupport_v3_2 isisJdoSupport;

    public static class ActionDomainEvent extends SimpleModule.ActionDomainEvent<Customers> {}

    public static class CreateActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, domainEvent = CreateActionDomainEvent.class)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Customer create(
            @Name final String firstName, @Name final String lastName) {
        return repositoryService.persist(Customer.withName(firstName, lastName));
    }

    public static class FindByNameActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.SAFE, domainEvent = FindByNameActionDomainEvent.class)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public List<Customer> findByLastName(
            @Name final String lastName
            ) {
        JDOQLTypedQuery<Customer> q = isisJdoSupport.newTypesafeQuery(Customer.class);
        final QCustomer cand = QCustomer.candidate();
        q = q.filter(
                cand.lastName.indexOf(q.stringParameter("lastName")).ne(-1)
                );
        return q.setParameter("lastName", lastName)
                .executeList();
    }

    public static class ListAllActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<Customer> listAll() {
        return repositoryService.allInstances(Customer.class);
    }

    @Programmatic
    public void ping() {
        JDOQLTypedQuery<Customer> q = isisJdoSupport.newTypesafeQuery(Customer.class);
        final QCustomer candidate = QCustomer.candidate();
        q.range(0,2);
        q.orderBy(candidate.lastName.asc());
        q.executeList();
    }


}
