package ru.practicum.shareit.booking.DAO;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllBookingsByBookerIdOrderByStartDesc(Long id);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long id, LocalDateTime start, LocalDateTime end);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime time);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long id, LocalDateTime time);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long id, Status status);

    @Query("select b from Booking b where b.item.owner.id = ?1 order by b.start DESC")
    List<Booking> findAllBookingsByItemOwnerId(Long id);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start < ?2 and b.end > ?2 order by b.start DESC")
    List<Booking> findAllCurrentBookingsByItemOwner(Long id, LocalDateTime time);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.end < ?2 order by b.start DESC")
    List<Booking> findAllPastBookingsByItemOwner(Long id, LocalDateTime time);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start > ?2 order by b.start DESC")
    List<Booking> findAllFutureBookingsByItemOwner(Long id, LocalDateTime time);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.status = ?2 order by b.start DESC")
    List<Booking> findAllBookingsByItemOwnerAndStatus(Long id, Status status);

    @Query("select b from Booking b where b.item.id = ?1 and ( b.end < ?2 or b.start < ?2) and b.status <> ?3 order by b.end DESC")
    List<Booking> findLastBookingByItemId(Long itemId, LocalDateTime time, Status status, Pageable pageable);

    @Query("select b from Booking b where b.item.id = ?1 and b.start > ?2 and b.status <> ?3 order by b.start ASC")
    List<Booking> findFutureBookingByItemId(Long itemId, LocalDateTime time, Status status, Pageable pageable);
}
