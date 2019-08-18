package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

@Repository("alphaHibernate")
//访问数据库的Bean
public class AlphaDaoHibernatelmpl implements AlphaDao {
    @Override
    public String select() {
        return "Hibernate";
    }
}
