package com.gp.quantificat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @TableName my_trigger
 */
@TableName(value ="my_trigger")
@Data
@AllArgsConstructor
public class MyTrigger implements Serializable {
    /**
     * 
     */
    @TableId
    private BigDecimal id;

    /**
     * 
     */
    private BigDecimal earn;

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