package domainapp.modules.content.dom.contentItem;

import java.lang.reflect.Method;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.PromptStyle;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.repository.RepositoryService;

import domainapp.modules.content.ContentModule;
import domainapp.modules.content.experimental.ExperimentalObject;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodCall;

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
    @ActionLayout
    public Object dynamicExperiment() {
    	return getDynamicClass();
    	//return new ExperimentalObject();
    }
    
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<ContentItem> listAllContentItems() {
        return repositoryService.allInstances(ContentItem.class);
    }
    
    private Object getDynamicClass() {
    	Implementation implementation = MethodCall.call(()->{
        	return "Im dynamic";
        });
    	try {
			final Object item = new ByteBuddy()
					.subclass(ExperimentalObject.class)
					 .annotateType(AnnotationDescription.Builder.ofType(DomainObject.class)
							.define("nature", Nature.INMEMORY_ENTITY)
							.define("objectType", "cms.DynamicEntity").build())
					.defineMethod("getDynamicString", String.class)
					.intercept(implementation)
					.make()
					.load(ExperimentalObject.class.getClassLoader())
					.getLoaded().newInstance();
			
			try {
				Method[] methods = item.getClass().getMethods();
				for (Method method : methods) {
					System.out.println("method name:" + method.getName());
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return item;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	throw new RuntimeException("Failed to create dynamic class");
    }

}
