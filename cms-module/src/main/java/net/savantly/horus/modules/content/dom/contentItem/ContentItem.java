package domainapp.modules.content.dom.contentItem;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.jaxbadapters.PersistentEntityAdapter;

import domainapp.modules.content.contentFieldData.ContentFieldData;
import domainapp.modules.content.types.Name;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "cms")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
@DomainObject()
@DomainObjectLayout()
public class ContentItem {
	
	public static ContentItem withFields(
			@Name String name) {
		return new ContentItem(name);
	}
	
	@Title
	@Name
	@Getter @Setter
	private String name;

	@Getter @Setter
	private SortedSet<ContentFieldData> fields = new TreeSet<>();
	
	private ContentItem(String name) {
		this.setName(name);
	}
}
