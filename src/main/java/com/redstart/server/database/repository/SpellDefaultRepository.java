package com.redstart.server.database.repository;

import com.redstart.server.database.entity.SpellDefaultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpellDefaultRepository extends JpaRepository<SpellDefaultEntity, String> {
}
