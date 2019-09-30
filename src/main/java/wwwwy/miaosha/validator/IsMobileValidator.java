package wwwwy.miaosha.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.ValidationUtils;
import wwwwy.miaosha.util.ValidatorUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
	private boolean required = false;

	@Override
	public void initialize(IsMobile constraintAnnotation) {
		required=constraintAnnotation.required();
	}

	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		if (required){
			return ValidatorUtil.isMobile(s);
		}else{
			if (StringUtils.isEmpty(s))
				return true;
			else return ValidatorUtil.isMobile(s);
		}
	}
}
