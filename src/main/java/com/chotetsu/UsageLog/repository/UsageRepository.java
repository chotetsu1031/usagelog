package com.chotetsu.UsageLog.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.chotetsu.UsageLog.entity.Usage;

public interface UsageRepository extends CrudRepository<Usage, UUID> {

}
