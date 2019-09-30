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
    public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId(value = "id", type = IdType.AUTO)
    private Long id;

        @TableField("user_id")
    private Long userId;

        @TableField("goods_id")
    private Long goodsId;

        @TableField("delivery_addr_id")
    private Long deliveryAddrId;

        @TableField("goods_name")
    private String goodsName;

        @TableField("goods_count")
    private Integer goodsCount;

        @TableField("goods_price")
    private BigDecimal goodsPrice;

        @TableField("order_channel")
    private Integer orderChannel;

        @TableField("status")
    private Integer status;

        @TableField("create_date")
    private LocalDateTime createDate;

        @TableField("pay_date")
    private LocalDateTime payDate;


}
