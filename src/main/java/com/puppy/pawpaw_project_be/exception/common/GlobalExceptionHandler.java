package com.puppy.pawpaw_project_be.exception.common;

import com.puppy.pawpaw_project_be.exception.board.BoardException.BoardDeleteException;
import com.puppy.pawpaw_project_be.exception.board.BoardException.BoardNotFoundException;
import com.puppy.pawpaw_project_be.exception.board.BoardException.BoardRegisterException;
import com.puppy.pawpaw_project_be.exception.board.BoardException.BoardUpdateException;
import com.puppy.pawpaw_project_be.exception.board.BoardImgException.BoardImgCanNotRemoveException;
import com.puppy.pawpaw_project_be.exception.board.BoardImgException.BoardImgCanNotUploadException;
import com.puppy.pawpaw_project_be.exception.board.BoardImgException.BoardImgCanNotViewException;
import com.puppy.pawpaw_project_be.exception.board.BoardLikeException.BoardDeleteLikeFailException;
import com.puppy.pawpaw_project_be.exception.board.BoardLikeException.BoardLikeFailException;
import com.puppy.pawpaw_project_be.exception.comment.CommentException.CommentDeleteException;
import com.puppy.pawpaw_project_be.exception.comment.CommentException.CommentRegisterException;
import com.puppy.pawpaw_project_be.exception.comment.CommentException.CommentUpdaterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException e) {

        log.error("handleHttpRequestMethodNotSupportedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        log.error("handleBusinessException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handleAuthenticationException(final AuthenticationException e) {
        log.error("handleAuthenticationException", e);
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.HANDLE_AUTHENTICATION_ENTRYPOINT), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BoardNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleBoardNotFoundException(BoardNotFoundException e){
        log.error("BoardRegisterException", e);
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.BOARD_NOTFOUND_EXCEPTION), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BoardRegisterException.class)
    protected ResponseEntity<ErrorResponse> handleBoardRegistrationException(BoardRegisterException e){
        log.error("BoardRegisterException", e);
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.BOARD_REGISTRATION_EXCEPTION), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BoardUpdateException.class)
    protected ResponseEntity<ErrorResponse> handleBoardUpdateException(BoardUpdateException e){
        log.error("BoardUpdateException", e);
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.BOARD_UPDATE_EXCEPTION), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BoardDeleteException.class)
    protected ResponseEntity<ErrorResponse> handleBoardDeletionException(BoardDeleteException e){
        log.error("BoardDeleteException", e);
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.BOARD_DELETE_EXCEPTION), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(BoardImgCanNotUploadException.class)
    protected ResponseEntity<ErrorResponse> handleBoardImgCanNotUploadException(BoardImgCanNotUploadException e){
        log.error("BoardDeleteLikeFailException", e);
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.BOARD_IMG_CANNOT_UPLOAD_EXCEPTION), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BoardImgCanNotViewException.class)
    protected ResponseEntity<ErrorResponse> handleBoardImgCanNotViewException(BoardImgCanNotViewException e){
        log.error("BoardImgCanNotViewException", e);
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.BOARD_IMG_CANNOT_VIEW_EXCEPTION), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BoardImgCanNotRemoveException.class)
    protected ResponseEntity<ErrorResponse> handleBoardImgCanNotRemoveException(BoardImgCanNotRemoveException e){
        log.error("BoardImgCanNotRemoveException", e);
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.BOARD_IMG_CANNOT_REMOVE_EXCEPTION), HttpStatus.BAD_REQUEST);
    }
}
