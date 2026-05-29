package com.scheduler.modules.user.domain;

import com.scheduler.infrastructure.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户模块预留，便于后续 RBAC / 个性化排程扩展。
 */
@Getter
@Setter
@Entity
@Table(name = "user_profiles")
public class UserProfileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_code", unique = true, length = 64)
    private String userCode;

    @Column(name = "display_name")
    private String displayName;
}
