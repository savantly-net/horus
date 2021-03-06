package net.savantly.horus.modules.content.dom.contentFieldData;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.jaxbadapters.PersistentEntityAdapter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.savantly.horus.modules.content.dom.contentField.ContentField;
import net.savantly.horus.modules.content.dom.contentItem.ContentItem;
import net.savantly.horus.modules.content.types.Name;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "cms")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@javax.jdo.annotations.Unique(name="ContentFieldData_rel_UNQ", members = {"contentField", "contentItem"})
@DomainObject()
@DomainObjectLayout()
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class ContentFieldData implements Comparable<ContentFieldData> {

	public static ContentFieldData withFields(ContentField contentField, ContentItem contentItem, String data) {
		return new ContentFieldData(contentField, contentItem, data);
	}
	
	@Title
	@Name
	@Getter
	private ContentField contentField;
	
	@Title
	@Name
	@Getter
	private ContentItem contentItem;

	@Property(editing = Editing.ENABLED)
	@Getter @Setter
	private String data;
	
	private ContentFieldData(ContentField contentField, ContentItem contentItem, String data) {
		this.contentField = contentField;
		this.contentItem = contentItem;
		this.setData(data);
	}
	
	@Override
	public int compareTo(ContentFieldData o) {
		// TODO Implement actual sorting
		return 0;
	}
}