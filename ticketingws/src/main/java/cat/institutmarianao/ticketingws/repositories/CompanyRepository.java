package cat.institutmarianao.ticketingws.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cat.institutmarianao.ticketingws.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
}
