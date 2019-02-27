package com.github.madhurimamalla.connoisseur.server.persistence;

import org.springframework.data.repository.CrudRepository;

import com.github.madhurimamalla.connoisseur.server.model.JobQueue;
import com.github.madhurimamalla.connoisseur.server.model.JobType;

public interface JobQueueRepository extends CrudRepository<JobQueue, Long> {

	JobQueue findByJobType(JobType jobType);

}
