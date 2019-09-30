package wwwwy.miaosha.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* <p>
    * 
    * </p>
*
* @author wwwwy
* @since 2019-09-27
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId(value = "id", type = IdType.AUTO)
    private Long id;

        @TableField("goods_name")
    private String goodsName;

        @TableField("goods_title")
    private String goodsTitle;

        @TableField("goods_img")
    private String goodsImg;

        @TableField("goods_detail")
    private String goodsDetail;

        @TableField("goods_price")
    private BigDecimal goodsPrice;

        @TableField("goods_stock")
    private Integer goodsStock;


}
