package com.scheduler.modules.plan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "plan_resources")
public class PlanResourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plan_item_code", length = 64)
    private String planItemCode;

    @Column(name = "resource_name")
    private String resourceName;

    @Column(name = "resource_url", columnDefinition = "TEXT")
    private String resourceUrl;
}
