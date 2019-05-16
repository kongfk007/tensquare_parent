package com.tensquare.tensquare_base.dao;

import com.tensquare.tensquare_base.pojo.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CityDao extends JpaRepository<City,String>, JpaSpecificationExecutor {
}
