package net.savantly.horus.modules.content.api;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import net.savantly.horus.modules.content.dom.contentField.ContentFields;
import net.savantly.horus.modules.content.dom.contentFieldData.ContentFieldData;
import net.savantly.horus.modules.content.dom.contentFieldData.ContentFieldDataDto;
import net.savantly.horus.modules.content.dom.contentItem.ContentItem;
import net.savantly.horus.modules.content.dom.contentItem.ContentItemDto;
import net.savantly.horus.modules.content.dom.contentItem.ContentItems;
import net.savantly.horus.modules.content.dom.contentType.ContentType;
import net.savantly.horus.modules.content.dom.contentType.ContentTypes;

@Api(value = "cms")
@Service
@Path("/api/cms")
@AllArgsConstructor
public class ContentApi {
	
	private final ContentTypes contentTypes;
	private final ContentItems contentItems;
	private final ContentFields contentFields;
	
	@ApiOperation(value = "get all content types")
	@GET
	@Produces({ MediaType.APPLICATION_JSON})
	@Path("/contentTypes")
	public Response getContentTypes(){
		return Response.ok(contentTypes.listAll()).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/contentItems")
	public Response createNewContentItem(final ContentItemDto dto) {
		ContentType contentType = contentTypes.findById(dto.getContentTypeId());
		Assert.notNull(contentType, "An existing content type is required");
    	ContentItem item = contentItems.create(dto.getName(), contentType);
    	item.setFields(fromDto(dto.getFields(), item));
        return Response.ok().build();
	}
    
	private SortedSet<ContentFieldData> fromDto(Set<ContentFieldDataDto> fields, ContentItem item) {
		SortedSet<ContentFieldData> response = new TreeSet<>();
		for (ContentFieldDataDto dto : fields) {
			response.add(ContentFieldData.withFields(contentFields.findById(dto.getContentFieldId()), item, dto.getData()));
		}
		return response;
	}

}
