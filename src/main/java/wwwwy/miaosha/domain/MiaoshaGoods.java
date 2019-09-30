package wwwwy.miaosha.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    public class MiaoshaGoods implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId(value = "id", type = IdType.AUTO)
    private Long id;

        @TableField("goods_id")
    private Long goodsId;

        @TableField("miaosha_price")
    private BigDecimal miaoshaPrice;

        @TableField("stock_count")
    private Integer stockCount;

        @TableField("start_date")
    private LocalDateTime startDate;

        @TableField("end_date")
    private LocalDateTime endDate;


}
