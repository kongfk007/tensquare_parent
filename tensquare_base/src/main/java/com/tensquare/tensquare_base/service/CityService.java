package com.tensquare.tensquare_base.service;

import com.tensquare.tensquare_base.dao.CityDao;
import com.tensquare.tensquare_base.pojo.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
            public Predicate toPredicate(Root<City> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //new 一个list集合，来存放所有的条件
                List<Predicate> predicateList = new ArrayList<>();
                if (city.getName()!=null && !"".equals(city.getName())){
                    Predicate predicate = cb.like(root.get("name").as(String.class),
                            "%"+city.getName()+"%");//where labelname like "%小明%"(label.getLabelname()就是小明,as(String.class)指定类型)
                    predicateList.add(predicate);
                }
                //new一个数组作为最终返回值的条件
                Predicate[] parr = new Predicate[predicateList.size()];
                //把list转为数组
                parr = predicateList.toArray(parr);
                //cb.or等于or；；；；cb.and等于and
                return cb.and(parr);//where name like "%小明%"
            }
        };

    }
    /**
     * 条件查询
     * @return List<Label>
     */
    public List<City> fingSearch(City city) {
        return cityDao.findAll(createSpecification(city));
    }


    /**
     * 分页条件查询
     * @return List<Label>
     */
    public Page<City> pageQuery(City city, int page, int size) {
    //封装分页对象
        Pageable pageable = PageRequest.of(page-1,size);
        return cityDao.findAll(createSpecification(city),pageable);
    }
}
