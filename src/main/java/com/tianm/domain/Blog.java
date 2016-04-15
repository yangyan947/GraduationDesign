package com.tianm.domain;

import com.tianm.domain.base.BaseObject;

import javax.persistence.Entity;

/**
 * Created by SunYi on 2016/4/15/0015.
 */
@Entity
public class Blog extends BaseObject {
    private String titile;
    private String context;

}
