package com.example.utkarshbackend.repository;

import com.example.utkarshbackend.entity.NonTeaching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NonTeachingRepo extends JpaRepository<NonTeaching, Long> {

}
