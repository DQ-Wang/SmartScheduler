package com.scheduler.modules.user.repository;

import com.scheduler.modules.user.domain.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {
}
