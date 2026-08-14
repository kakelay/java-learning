package com.in28minutes.springboot.myfirstwebapp.repository;

import com.in28minutes.springboot.myfirstwebapp.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByAuthor(String author);

    List<Course> findByCategory(String category);

    List<Course> findByDifficultyLevel(String difficultyLevel);

    List<Course> findByActiveTrue();

    List<Course> findByPriceLessThanEqual(BigDecimal price);

    List<Course> findByDurationHoursLessThanEqual(Integer hours);

    @Query("SELECT c FROM Course c WHERE c.name LIKE %:keyword% OR c.description LIKE %:keyword%")
    List<Course> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT c FROM Course c WHERE c.category = :category AND c.difficultyLevel = :level")
    List<Course> findByCategoryAndDifficulty(@Param("category") String category,
                                           @Param("level") String level);

    @Query("SELECT AVG(c.price) FROM Course c WHERE c.active = true")
    BigDecimal findAveragePriceOfActiveCourses();

    @Query("SELECT c FROM Course c ORDER BY c.createdDate DESC")
    List<Course> findAllOrderByNewest();
}