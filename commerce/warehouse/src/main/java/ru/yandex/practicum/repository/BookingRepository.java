package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, String> {
}
