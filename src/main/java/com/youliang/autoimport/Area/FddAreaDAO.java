package com.youliang.autoimport.Area;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lixubo
 * @version Id: AreaDAO.java, v0.1 2019/5/6 14:29 lixubo Exp $$
 */
@Repository
public interface FddAreaDAO extends JpaRepository<Area, Long> {

    Optional<Area> findByIdAndLevel(Long id, Integer level);

    List<Area> findAllByLevel(Integer level);
}
