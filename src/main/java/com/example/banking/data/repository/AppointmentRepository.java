package com.example.banking.data.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.banking.data.entity.Appointment;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {

	@Override
	public List<Appointment> findAll();

	public List<Appointment> findByDate(Date date);

}
