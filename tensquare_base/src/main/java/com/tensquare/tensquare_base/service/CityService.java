package com.tensquare.tensquare_base.service;

import com.tensquare.tensquare_base.dao.CityDao;
import com.tensquare.tensquare_base.pojo.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * city数据逻辑层
 */
@Service
public class CityService {
    @Autowired
    private CityDao cityDao;

    /**
     * 新增城市
     */
    public void addcity(City city){
        cityDao.save(city);
    }

    /**
     * 返回城市列表
     */
    public List<City> findAll(){
        return cityDao.findAll();
    }

    /**
     * 修改城市
     */
    public void updateCity(City city,String id){
        city.setId(id);
        cityDao.save(city);
    }

    /**
     * 删除城市
     */
    public void deleteCity(String id){
        cityDao.deleteById(id);
    }

    /**
     * 根据ID查询城市
     */
    public City findById(String id){
        return cityDao.findById(id).get();
    }
    public Specification<City> createSpecification(City city){
        return new Specification<City>() {
            @Override
            public Predicate toPredicate(Root<City> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //new 一个list集合，来存放所有的条件
                List<Predicate> predicateList = new ArrayList<>();
                return null;
            }
        };

    }
    /**
     * 根据条件查询城市列表
     */

    /**
     * 根据条件查询城市列表
     */
}
