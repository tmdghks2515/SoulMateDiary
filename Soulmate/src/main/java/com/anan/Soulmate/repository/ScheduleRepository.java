package com.anan.Soulmate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.anan.Soulmate.model.Schedule;

public interface ScheduleRepository extends CrudRepository<Schedule, Long>{
	@Query(value="select * from schedule where Year(scheduleTime) = :year and"
			+ " Month(scheduleTime) = :month and soulmateId = :soulmateId", nativeQuery = true)
	public List<Schedule> findByYearAndMonth(int year, int month, Long soulmateId);
}
