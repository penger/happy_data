package com.gp.quantificat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName coin_history_detail
 */
@TableName(value ="coin_history_detail")
@Data
public class CoinHistoryDetail implements Serializable {
    /**
     * 
     */
    private BigDecimal amount;

    /**
     * 
     */
    private BigDecimal close;

    /**
     * 
     */
    private BigDecimal count;

    /**
     * 
     */
    private BigDecimal high;

    /**
     * 
     */
    private BigDecimal low;

    /**
     * 
     */
    private Double maxDiePrecent;

    /**
     * 
     */
    private Double maxPrecent;

    /**
     * 
     */
    private Double maxZhangPrecent;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private BigDecimal open;

    /**
     * 
     */
    private Double precent;

    /**
     * 
     */
    private String time;

    /**
     * 
     */
    private BigDecimal vol;

    /**
     * 
     */
    private Date insertTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}