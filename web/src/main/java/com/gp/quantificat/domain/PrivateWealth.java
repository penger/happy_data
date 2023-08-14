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
 * @TableName private_wealth
 */
@TableName(value ="private_wealth")
@Data
public class PrivateWealth implements Serializable {
    /**
     * 
     */
    @TableId
    private String date;

    /**
     * 
     */
    private BigDecimal btcPrice;

    /**
     * 
     */
    private BigDecimal close;

    /**
     * 
     */
    private BigDecimal open;

    /**
     * 
     */
    private BigDecimal precent;

    /**
     * 
     */
    private BigDecimal earn;

    /**
     * 
     */
    private String comment;

    /**
     * 
     */
    private Date insertTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}