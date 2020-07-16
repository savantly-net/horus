package domainapp.modules.simple.dom.customerContact;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.PromptStyle;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.dom.customer.Customer;
import domainapp.modules.simple.types.ContactType;
import domainapp.modules.simple.types.Name;

@DomainService(
        nature = NatureOfService.VIEW,
        objectType = "simple.CustomerContacts"
        )
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class CustomerContacts {
	
	private final RepositoryService repositoryService;

    public static class ActionDomainEvent extends SimpleModule.ActionDomainEvent<CustomerContact> {}

    public static class CreateActionDomainEvent extends ActionDomainEvent {}
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, domainEvent = CreateActionDomainEvent.class)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_MODAL, hidden = Where.EVERYWHERE)
    public CustomerContact create(
    		ContactType contactType,
            @Name final String notes,
            Customer customer) {
        return repositoryService.persist(CustomerContact.withFields(notes, contactType, customer));
    }
}
