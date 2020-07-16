package domainapp.modules.content.contentFieldData;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.jaxbadapters.PersistentEntityAdapter;

import domainapp.modules.content.dom.contentField.ContentField;
import domainapp.modules.content.dom.contentItem.ContentItem;
import domainapp.modules.content.types.Name;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "cms")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@javax.jdo.annotations.Unique(name="ContentFieldData_rel_UNQ", members = {"contentField", "contentItem"})
@DomainObject()
@DomainObjectLayout()
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class ContentFieldData {

	public static ContentFieldData withFields(ContentField contentField, ContentItem contentItem, byte[] data) {
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

	@Getter @Setter
	private byte[] data;
	
	private ContentFieldData(ContentField contentField, ContentItem contentItem, byte[] data) {
		this.contentField = contentField;
		this.contentItem = contentItem;
		this.setData(data);
	}
}