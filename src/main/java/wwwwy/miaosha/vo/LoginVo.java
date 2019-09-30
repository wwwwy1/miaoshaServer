package wwwwy.miaosha.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import wwwwy.miaosha.validator.IsMobile;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LoginVo {
	@NotNull
	@IsMobile
	private String mobile;
	@NotNull
	@Length(min = 32)
	private String password;
}
