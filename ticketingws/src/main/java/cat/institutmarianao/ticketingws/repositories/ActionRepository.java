package cat.institutmarianao.ticketingws.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import cat.institutmarianao.ticketingws.model.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long>, JpaSpecificationExecutor<Action> {

	List<Action> findByTicketIdOrderByDateDesc(Long ticketId);
}
