package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    @Query(value = "SELECT * FROM tb_problem JOIN tb_pl WHERE id=problemid AND labelid=?1 ORDER BY createtime DESC",nativeQuery = true)
    Page<Problem> newlist(String labelid, Pageable pageable);//最新问答列表

    @Query(value = "SELECT * FROM tb_problem JOIN tb_pl WHERE id=problemid AND labelid=?1 ORDER BY reply DESC",nativeQuery = true)
    Page<Problem> hotlist(String labelid, Pageable pageable);//热门问答列表

    @Query(value = "SELECT * FROM tb_problem JOIN tb_pl WHERE id=problemid AND labelid=?1 AND reply=0 ORDER BY createtime DESC",nativeQuery = true)
    Page<Problem> waitlist(String labelid, Pageable pageable);//等待回答列表
}
