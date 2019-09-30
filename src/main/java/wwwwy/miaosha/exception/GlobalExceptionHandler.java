package wwwwy.miaosha.exception;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import wwwwy.miaosha.result.CodeMsg;
import wwwwy.miaosha.result.Result;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
	@ExceptionHandler(value = Exception.class)
	public Result<String> exceptionHandler(HttpServletRequest request,Exception e){
		e.printStackTrace();
		if (e instanceof GlobalException){
			GlobalException ge=(GlobalException) e;
			return Result.error(ge.getCm());
		}else if (e instanceof BindException){
			BindException be=(BindException) e;
			String message = be.getAllErrors().get(0).getDefaultMessage();
			return Result.error(CodeMsg.BIND_ERROR.fillArgs(message));
		}else {
			return Result.error(CodeMsg.SERVER_ERROR);
		}
	}
}
