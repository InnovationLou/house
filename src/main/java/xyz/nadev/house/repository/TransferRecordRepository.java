package xyz.nadev.house.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.nadev.house.entity.TransferRecord;

@Repository
public interface TransferRecordRepository extends JpaRepository<TransferRecord,Integer> {
}
