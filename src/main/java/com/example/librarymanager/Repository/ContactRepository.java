package com.example.librarymanager.Repository;

import com.example.librarymanager.Entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
    List<ContactEntity> findByUid(Long uid);
    List<ContactEntity> findByEnquiryId(Long enquiryId);

}
