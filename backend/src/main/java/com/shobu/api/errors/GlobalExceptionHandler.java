package com.shobu.api.errors;

import com.shobu.api.errors.apiExceptions.*;
import com.shobu.domain.errors.CannotPushOwnPieceException;
import com.shobu.domain.errors.InvalidMoveException;
import com.shobu.domain.errors.PieceOutOfBoundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGameNotFound(GameNotFoundException ex) {
        ErrorResponse response = new ErrorResponse("GAME_NOT_FOUND",
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CannotPushOwnPieceException.class)
    public ResponseEntity<ErrorResponse> handleCannotPushOwnPiece(CannotPushOwnPieceException ex) {
        ErrorResponse response = new ErrorResponse("CANNOT_PUSH_OWN_PIECE", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidMoveException.class)
    public ResponseEntity<ErrorResponse> handleInvalidMoveException(InvalidMoveException ex) {
        ErrorResponse response = new ErrorResponse("INVALID_MOVE", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PieceOutOfBoundsException.class)
    public ResponseEntity<ErrorResponse> handlePieceOutOfBoundsException(PieceOutOfBoundsException ex) {
        ErrorResponse response = new ErrorResponse("PIECE_OUT_OF_BOUNDS", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ex.printStackTrace();

        ErrorResponse response = new ErrorResponse(
                "INTERNAL_ERROR_IT",
                ex.getClass().getSimpleName() + ": " + ex.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GameFullException.class)
    public ResponseEntity<ErrorResponse> handleGameFullException(GameFullException ex) {
        ErrorResponse response = new ErrorResponse(
                "GAME_FULL", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ErrorResponse> handleInvalidLoginException(InvalidLoginException ex){
        ErrorResponse response = new ErrorResponse("INVALID_LOGIN", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(SignUpException.class)
    public ResponseEntity<ErrorResponse> handleSignupException(SignUpException ex){
        return new ResponseEntity<>(new ErrorResponse("SIGNUP_ERROR", ex.getMessage()),   HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PlayerNotInGameException.class)
    public ResponseEntity<ErrorResponse> handlePlayerNotInGameExceptio(PlayerNotInGameException ex){
        return new ResponseEntity<>(new ErrorResponse("PLAYER_NOT_IN_GAME", ex.getMessage()), HttpStatus.FORBIDDEN);
    }
}
