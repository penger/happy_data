package com.gp.quantificat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName coin
 */
@TableName(value ="coin")
@Data
public class Coin implements Serializable {
    /**
     * 
     */
    @TableId
    private String coinName;

    /**
     * 
     */
    private Integer chosenRank;

    /**
     * 
     */
    private Integer favoriteRank;

    /**
     * 
     */
    private Boolean isChosen;

    /**
     * 
     */
    private Boolean isFavorite;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}