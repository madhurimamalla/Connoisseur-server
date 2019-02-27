package com.github.madhurimamalla.connoisseur.server.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.github.madhurimamalla.connoisseur.server.model.JobHistory;

public interface JobRepository extends CrudRepository<JobHistory, Long> {
	
	Optional<JobHistory> findById(Long id);
	
	boolean existsById(Long id);
	
	void deleteById(Long id);
	
	void deleteAll();
	
	List<JobHistory> findByjobStatus(String jobStatus);
	
	List<JobHistory> findByjobType(Enum jobType);
	
	long count();
	
	@Query("select min(jobId) from JobHistory")
	long findMinJobId();
	
	@Query("Select min(jobId) from JobHistory where jobStatus = 'QUEUED'")
	long findNextMinJobId();
	
	@Query("Select count(*) from JobHistory where jobStatus = 'QUEUED'")
	long findNumberOfQueuedJobs();
}
