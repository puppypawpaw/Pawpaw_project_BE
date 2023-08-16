package helper;

import kr.co.pawpaw.domainrdb.term.dto.request.UpdateTermRequest;

import java.lang.reflect.Field;

import static io.hypersistence.utils.hibernate.util.ReflectionUtils.setFieldValue;

public class UpdateTermRequestBuilder {
    private String title;
    private String content;
    private Boolean required;
    private Long order;

    public static UpdateTermRequestBuilder builder() {
        return new UpdateTermRequestBuilder();
    }

    public UpdateTermRequestBuilder title(final String title) {
        this.title = title;
        return this;
    }

    public UpdateTermRequestBuilder content(final String content) {
        this.content = content;
        return this;
    }

    public UpdateTermRequestBuilder required(final Boolean required) {
        this.required = required;
        return this;
    }

    public UpdateTermRequestBuilder order(final Long order) {
        this.order = order;
        return this;
    }

    public UpdateTermRequest build() {
        UpdateTermRequest updateTermRequest = new UpdateTermRequest();
        for (Field field : UpdateTermRequestBuilder.class.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                setFieldValue(updateTermRequest, field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return updateTermRequest;
    }
}
