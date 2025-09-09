package com.example.utkarshbackend.repository;

import com.example.utkarshbackend.entity.ContactPageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactPageDataRepo extends JpaRepository<ContactPageData, Long> {
}
