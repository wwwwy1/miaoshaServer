package wwwwy.miaosha.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import wwwwy.miaosha.domain.Goods;
import wwwwy.miaosha.domain.MiaoshaGoods;
import wwwwy.miaosha.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wwwwy
 * @since 2019-09-27
 */
public interface GoodsMapper extends BaseMapper<Goods> {
	@Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from  miaosha_goods mg left join   goods g ON mg.goods_id = g.id")
	public List<GoodsVo> listGoodsVo();
	@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
	public GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

	@Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count>0")
	public int reduceStock(MiaoshaGoods g);
}
