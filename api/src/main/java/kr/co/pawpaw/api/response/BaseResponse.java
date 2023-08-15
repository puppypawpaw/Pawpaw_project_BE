package kr.co.pawpaw.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BaseResponse<T> {

    private HttpStatus httpStatus;
    private String message;
    private T data;
    private T response;

    public BaseResponse(String message, T data) {
        this.httpStatus = HttpStatus.OK;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(String message) {
        this.httpStatus = HttpStatus.OK;
        this.message = message;
    }

    public BaseResponse(T data) {
        this.httpStatus = HttpStatus.OK;
    }



    public static <T>BaseResponse<T> of(String message, T data){
        return new BaseResponse<>(message, data);
    }

    public static <T>BaseResponse<T> of(T data){
        return new BaseResponse<>(data);
    }

    public static <T> BaseResponse<T> of(String message){
        return new BaseResponse<>(message);
    }
}
