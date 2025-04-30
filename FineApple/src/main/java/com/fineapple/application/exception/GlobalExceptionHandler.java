package com.fineapple.application.exception;

import com.fineapple.domain.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Locale;

/**
 * 전역 예외 처리 핸들러
 * 모든 전역 컨트롤러 예외를 처리해서 요청 URI를 기반으로 API 요청과 뷰 요청을 구분하여 다른 형태로 응답
 * Validation, 404, 403, 500 등 다양한 예외를 세분화 처리
 * error.html 에서 화면 을 제공
 */
@ControllerAdvice
@Hidden
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;
    private final CategoryService categoryService;

    /**
     * 요청 url 확인해서 api 혹은 rest 요청인 확인해서 반환
     *
     * @param request 요청
     * @return boolean
     */
    private boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/api/") || uri.startsWith("/rest/");
    }



    /**
     * 뷰 + 에러 객체 메세지 및 코드를 반환
     *
     * @param errorResponse 에러 객체
     * @return ModelAndView
     */
    private ModelAndView buildErrorView(ErrorResponse errorResponse) {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("errorResponse", errorResponse);
        mv.addObject("mainCategories", categoryService.getCategory());
        return mv;
    }
    /**
     * 다국어 메시지를 반환
     *
     * @param code 메시지 코드
     * @param locale 사용자 로케일
     * @return 메시지 문자열
     */
    private String getMessage(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }

    /**
     * BindException (주로 폼 데이터 바인딩 오류) 처리
     */
    @ExceptionHandler(BindException.class)
    public Object handleBindException(BindException e, HttpServletRequest request, Locale locale) {
        StringBuilder sb = new StringBuilder();
        for (FieldError error : e.getFieldErrors()) {
            sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }

        ErrorResponse error = new ErrorResponse(400, "Validation Error", sb.toString().trim());
        return isApiRequest(request) ? ResponseEntity.badRequest().body(error) : buildErrorView(error);
    }

    /**
     * @Valid 유효성 검사 실패 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request, Locale locale) {
        StringBuilder sb = new StringBuilder();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }

        ErrorResponse error = new ErrorResponse(400, "Validation Error", sb.toString().trim());
        return isApiRequest(request) ? ResponseEntity.badRequest().body(error) : buildErrorView(error);
    }

    /**
     * 사용자 정의 NotFoundException 처리
     */
    @ExceptionHandler(com.fineapple.application.exception.NotFoundException.class)
    public Object customHandleNotFound(com.fineapple.application.exception.NotFoundException e, HttpServletRequest request, Locale locale) {
        ErrorResponse error = new ErrorResponse(404, "Not Found", getMessage("error.not_found", locale));
        return isApiRequest(request) ? ResponseEntity.status(404).body(error) : buildErrorView(error);
    }

    /**
     * 상품 업로드 실패 처리
     */
    @ExceptionHandler(ProductUploadException.class)
    public Object handleProductUploadError(ProductUploadException e, HttpServletRequest request, Locale locale) {
        ErrorResponse error = new ErrorResponse(400, "Product Upload Error", getMessage("error.product.upload", locale));
        return isApiRequest(request) ? ResponseEntity.status(400).body(error) : buildErrorView(error);
    }

    /**
     * 잘못된 URL 접근 (404) 처리
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handle404(NoHandlerFoundException ex, HttpServletRequest request, Locale locale) {
        ErrorResponse error = new ErrorResponse(404, "Page Not Found", getMessage("error.page.not_found", locale));
        return isApiRequest(request) ? ResponseEntity.status(404).body(error) : buildErrorView(error);
    }

    /**
     * 외부 라이브러리 NotFoundException 처리
     */
    @ExceptionHandler(NotFoundException.class)
    public Object handleNotFound(NotFoundException e, HttpServletRequest request, Locale locale) {
        ErrorResponse error = new ErrorResponse(404, "Not Found", getMessage("error.not_found", locale));
        return isApiRequest(request) ? ResponseEntity.status(404).body(error) : buildErrorView(error);
    }

    /**
     * 사용자 x 예외 처리
     */
    @ExceptionHandler(UserNotFoundException.class)
    public Object handleUserNotFound(UserNotFoundException e, HttpServletRequest request, Locale locale) {
        ErrorResponse error = new ErrorResponse(404, "User Not Found", getMessage("error.user.not_found", locale));
        return isApiRequest(request) ? ResponseEntity.status(404).body(error) : buildErrorView(error);
    }

    /**
     * 주문 상세 정보 없음 처리
     */
    @ExceptionHandler(OrderItemDetailNotFoundException.class)
    public Object handleOrderItemDetailNotFound(OrderItemDetailNotFoundException e, HttpServletRequest request, Locale locale) {
        ErrorResponse error = new ErrorResponse(404, "Order Detail Not Found", getMessage("error.order.detail.not_found", locale));
        return isApiRequest(request) ? ResponseEntity.status(404).body(error) : buildErrorView(error);
    }

    /**
     * 잘못된 요청 파라미터 등 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleBadRequest(IllegalArgumentException e, HttpServletRequest request, Locale locale) {
        ErrorResponse error = new ErrorResponse(400, "Bad Request", getMessage("error.bad_request", locale));
        return isApiRequest(request) ? ResponseEntity.status(400).body(error) : buildErrorView(error);
    }

    /**
     * 인증은 되었지만 권한이 부족한 경우 처리
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Object handleForbidden(AccessDeniedException e, HttpServletRequest request, Locale locale) {
        ErrorResponse error = new ErrorResponse(403, "Access Denied", getMessage("error.access_denied", locale));
        return isApiRequest(request) ? ResponseEntity.status(403).body(error) : buildErrorView(error);
    }

    /**
     * AI 예측 서비스 오류 처리
     */
    @ExceptionHandler(PredictServiceException.class)
    public Object handlePredictError(PredictServiceException e, HttpServletRequest request, Locale locale) {
        ErrorResponse error = new ErrorResponse(500, "Predict Service Error", getMessage("error.predict", locale));
        return isApiRequest(request) ? ResponseEntity.status(500).body(error) : buildErrorView(error);
    }
    /**
     * 모든 예외의 최종 처리 핸들러
     */
    @ExceptionHandler(Exception.class)
    public Object handleInternalError(Exception e, HttpServletRequest request, Locale locale) {
        ErrorResponse error = new ErrorResponse(500, "Internal Server Error", getMessage("error.internal", locale));
        return isApiRequest(request) ? ResponseEntity.status(500).body(error) : buildErrorView(error);
    }

    /**
     * 애플리케이션의 상태가 잘못된 경우 처리
     */
    @ExceptionHandler(IllegalStateException.class)
    public Object handleIllegalState(IllegalStateException e, HttpServletRequest request, Locale locale) {
        ErrorResponse error = new ErrorResponse(400, "Illegal State", e.getMessage());
        return isApiRequest(request) ? ResponseEntity.status(400).body(error) : buildErrorView(error);
    }

}
