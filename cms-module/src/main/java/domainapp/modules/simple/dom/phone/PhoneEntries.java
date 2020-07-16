package domainapp.modules.simple.dom.phone;

import java.util.List;

import javax.inject.Inject;
import javax.jdo.JDOQLTypedQuery;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.PromptStyle;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.persistence.jdo.applib.services.IsisJdoSupport_v3_2;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.types.Name;
import domainapp.modules.simple.types.PhoneLabel;

@DomainService(
        nature = NatureOfService.VIEW,
        objectType = "simple.PhoneEntries"
        )
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class PhoneEntries {

    private final RepositoryService repositoryService;
    private final IsisJdoSupport_v3_2 isisJdoSupport;

    public static class ActionDomainEvent extends SimpleModule.ActionDomainEvent<PhoneEntries> {}

    public static class CreateActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, domainEvent = CreateActionDomainEvent.class)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR, named = "Create Phone Entry")
    public PhoneEntry create(
    		@Parameter final PhoneLabel label,
    		@Parameter final String number) {
        return repositoryService.persist(PhoneEntry.withFields(label, number));
    }

    public static class FindByNumberActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.SAFE, domainEvent = FindByNumberActionDomainEvent.class)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public List<PhoneEntry> findByNumber(
            @Name final String name
            ) {
        JDOQLTypedQuery<PhoneEntry> q = isisJdoSupport.newTypesafeQuery(PhoneEntry.class);
        final QPhoneEntry cand = QPhoneEntry.candidate();
        q = q.filter(
                cand.number.indexOf(q.stringParameter("number")).ne(-1)
                );
        return q.setParameter("number", name)
                .executeList();
    }


    public static class ListAllActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, named = "List All Phone Entries")
    public List<PhoneEntry> listAll() {
        return repositoryService.allInstances(PhoneEntry.class);
    }

    @Programmatic
    public void ping() {
        JDOQLTypedQuery<PhoneEntry> q = isisJdoSupport.newTypesafeQuery(PhoneEntry.class);
        final QPhoneEntry candidate = QPhoneEntry.candidate();
        q.range(0,2);
        q.orderBy(candidate.number.asc());
        q.executeList();
    }


}
