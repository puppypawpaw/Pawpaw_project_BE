package helper;

import kr.co.pawpaw.domainrdb.term.dto.request.CreateTermRequest;

import java.lang.reflect.Field;

import static io.hypersistence.utils.hibernate.util.ReflectionUtils.setFieldValue;

public class CreateTermRequestBuilder {
    private String title;
    private String content;
    private Boolean required;
    private Long order;

    public static CreateTermRequestBuilder builder() {
        return new CreateTermRequestBuilder();
    }

    public CreateTermRequestBuilder title(final String title) {
        this.title = title;
        return this;
    }

    public CreateTermRequestBuilder content(final String content) {
        this.content = content;
        return this;
    }

    public CreateTermRequestBuilder required(final Boolean required) {
        this.required = required;
        return this;
    }

    public CreateTermRequestBuilder order(final Long order) {
        this.order = order;
        return this;
    }

    public CreateTermRequest build() {
        CreateTermRequest createTermRequest = new CreateTermRequest();
        for (Field field : CreateTermRequestBuilder.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                setFieldValue(createTermRequest, field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return createTermRequest;
    }
}
