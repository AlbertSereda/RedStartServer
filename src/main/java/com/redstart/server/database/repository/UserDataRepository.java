package com.redstart.server.database.repository;

import com.redstart.server.database.entity.UserDataEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDataRepository extends JpaRepository<UserDataEntity, String> {
    List<UserDataEntity> findAllByOrderByLevelDesc(Pageable pageable);
}
