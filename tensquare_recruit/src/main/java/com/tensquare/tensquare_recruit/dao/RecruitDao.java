package com.tensquare.tensquare_recruit.dao;

import com.tensquare.tensquare_recruit.pojo.Recruit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface RecruitDao extends JpaRepository<Recruit,String>,JpaSpecificationExecutor<Recruit>{
	public List<Recruit> findTop4ByStateOrderByCreatetimeDesc(String state);

	public List<Recruit> findByStateNotOrderByCreatetimeDesc(String state);

	@Query(value = "SELECT * FROM tb_recruit",
			countQuery = "SELECT count(*) FROM tb_recruit",
			nativeQuery = true)
	Page<Recruit> findByLastname(String jobname, Pageable pageable);//jobname可以查询条件分页，不过这里没用
}
