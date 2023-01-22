package cat.institutmarianao.ticketingws.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cat.institutmarianao.ticketingws.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
