package net.savantly.horus.modules.content.dom.contentItem;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.jaxbadapters.PersistentEntityAdapter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.savantly.horus.modules.content.ContentModule;
import net.savantly.horus.modules.content.dom.contentFieldData.ContentFieldData;
import net.savantly.horus.modules.content.dom.contentType.ContentType;
import net.savantly.horus.modules.content.types.Name;


@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "cms")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
@DomainObject()
@DomainObjectLayout()
public class ContentItem implements Comparable<ContentItem> {
	
	public static ContentItem withFields(
			@Name String name,
			ContentType contentType) {
		return new ContentItem(name, contentType);
	}

	public static class ActionDomainEvent extends ContentModule.ActionDomainEvent<ContentType>{}
	
	@Title
	@Name
	@Getter @Setter
	private String name;

	@Getter @Setter
	private ContentType contentType;
	
	@Collection
	@Getter @Setter
	private SortedSet<ContentFieldData> fields = new TreeSet<>();
	
	private ContentItem(String name, ContentType contentType) {
		this.setName(name);
		this.setContentType(contentType);
	}

	@Override
	public int compareTo(ContentItem o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
