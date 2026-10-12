package kz.bitlab.courseplatform.repository;

import kz.bitlab.courseplatform.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
