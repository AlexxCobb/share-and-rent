package ru.practicum.shareit.booking.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.DAO.BookingRepository;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.DAO.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.DAO.UserRepository;
import ru.practicum.shareit.user.model.User;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingRepositoryTest {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        var userOwner = new User();
        userOwner.setEmail("userOwner@ya.ru");
        userOwner.setName("userOwner");

        var userBooker1 = new User();
        userBooker1.setEmail("userBooker1@ya.ru");
        userBooker1.setName("userBooker1");

        var userBooker2 = new User();
        userBooker2.setEmail("userBooker2@ya.ru");
        userBooker2.setName("userBooker2");

        userRepository.save(userOwner);
        userRepository.save(userBooker1);
        userRepository.save(userBooker2);

        var item = new Item();
        item.setName("Spoon");
        item.setAvailable(true);
        item.setOwner(userOwner);
        item.setDescription("description");

        itemRepository.save(item);

        var booking1 = new Booking();
        booking1.setItem(item);
        booking1.setStatus(Status.WAITING);
        booking1.setBooker(userBooker1);

        var booking2 = new Booking();
        booking2.setItem(item);
        booking2.setStatus(Status.WAITING);
        booking2.setBooker(userBooker2);

        var booking3 = new Booking();
        booking3.setItem(item);
        booking3.setStatus(Status.WAITING);
        booking3.setBooker(userBooker1);
    }

    @Test
    public void test() {

    }

}
