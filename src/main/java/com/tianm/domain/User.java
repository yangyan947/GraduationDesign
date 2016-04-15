package com.tianm.domain;



import com.tianm.domain.base.BaseObject;

import javax.persistence.Entity;

/**
 * Created by SunYi on 2016/3/24/0024.
 */

@Entity
//@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class User extends BaseObject{

    private String email;
    private String name;
    private String password;


}

