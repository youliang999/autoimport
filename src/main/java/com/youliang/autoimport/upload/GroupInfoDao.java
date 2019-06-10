package com.youliang.autoimport.upload;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupInfoDao  extends JpaRepository<GroupInfoDTO, Long> {

    GroupInfoDTO findFirstByName(String name);

    GroupInfoDTO findFirstByNameLike(String name);

}
