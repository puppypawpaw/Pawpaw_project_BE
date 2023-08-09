package com.puppy.pawpaw_project_be.component;

import com.puppy.pawpaw_project_be.domain.term.dto.request.CreateTermRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;

import static io.hypersistence.utils.hibernate.util.ReflectionUtils.setFieldValue;

@Builder
@AllArgsConstructor
public class CreateTermRequestBuilder {
    private String title;
    private String content;
    private Boolean required;
    private Long order;

    public CreateTermRequest convert() {
        CreateTermRequest createTermRequest = new CreateTermRequest();

        setFieldValue(createTermRequest, "title", title);
        setFieldValue(createTermRequest, "content", content);
        setFieldValue(createTermRequest, "required", required);
        setFieldValue(createTermRequest, "order", order);

        return createTermRequest;
    }
}
