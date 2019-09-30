package wwwwy.miaosha.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
* <p>
    * 
    * </p>
*
* @author wwwwy
* @since 2019-09-24
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class MiaoshaUser implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId("id")
    private Long id;

        @TableField("nickname")
    private String nickname;

        @TableField("password")
    private String password;

        @TableField("salt")
    private String salt;

        @TableField("head")
    private String head;

        @TableField("register_date")
    private Date registerDate;

        @TableField("last_login_date")
    private Date lastLoginDate;

        @TableField("login_count")
    private Integer loginCount;


}
