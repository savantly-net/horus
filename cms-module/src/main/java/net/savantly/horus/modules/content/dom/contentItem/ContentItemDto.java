package net.savantly.horus.modules.content.dom.contentItem;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.savantly.horus.modules.content.dom.contentFieldData.ContentFieldDataDto;

@Data
@NoArgsConstructor
public class ContentItemDto {
	
	private String name;
	@Nonnull
	private String contentTypeId;
	private Set<ContentFieldDataDto> fields = new HashSet();

}
