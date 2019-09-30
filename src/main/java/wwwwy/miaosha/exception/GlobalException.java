package wwwwy.miaosha.exception;

import lombok.Getter;
import wwwwy.miaosha.result.CodeMsg;
 @Getter
public class GlobalException extends  RuntimeException{
	private static final long serialVersionUID=1L;
	private CodeMsg cm;
	public GlobalException(CodeMsg cm){
		super(cm.toString());
		this.cm=cm;
	}

}
