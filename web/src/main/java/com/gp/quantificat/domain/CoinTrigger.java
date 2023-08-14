package com.gp.quantificat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 
 * @TableName coin_trigger
 */
@TableName(value ="coin_trigger")
@Data
public class CoinTrigger implements Serializable {
    /**
     * 
     */
    @TableId
    private BigDecimal id;

    /**
     * 
     */
    private String coinName;

    /**
     * 
     */
    private BigDecimal coinPrice;

    /**
     * 
     */
    private String message;

    /**
     * 
     */
    private Boolean status;

    /**
     * 
     */
    private String type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}