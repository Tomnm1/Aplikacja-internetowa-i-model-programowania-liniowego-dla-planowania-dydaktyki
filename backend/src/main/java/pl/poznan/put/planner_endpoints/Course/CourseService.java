package pl.poznan.put.planner_endpoints.Course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for courses
 */
@Service
public class CourseService {
    /**
     * Courses repository
     */
    @Autowired
    private CourseRepository courseRepository;

    /**
     * Returns all Courses
     * @return list of Course
     */
    public List<Course> getAllCourse(){
        return courseRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * For pagination - returns courses from given page
     * @param page number of page to return
     * @param size total number of pages
     * @return Page object containing all found course objects
     */
    public Page<Course> getCoursePage(Integer page, Integer size){
        return courseRepository.findAll(PageRequest.of(page,size));
    }

    /**
     * Finds Course by id or empty Optional
     * @param id course id
     * @return Optional - empty or with Course
     */
    public Optional<Course> getCourseByID(Integer id){
        return courseRepository.findById(id);
    }

    /**
     * Creates a course
     * @param course object to be inserted into DB
     * @return saved course
     */
    public Course createCourse(Course course){
        return courseRepository.save(course);
    }

    /**
     * Updates existing Course if it exists
     * @param id id
     * @param courseParams new values in JSON format
     * @return saved course or null
     */
    public Course updateCourseByID(Integer id, Course courseParams){
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            Course oldCourse = course.get();
            oldCourse.course = courseParams.course;
            oldCourse.specialization = courseParams.specialization;
            oldCourse.semester = courseParams.semester;
            oldCourse.numOfStudents = courseParams.numOfStudents;
            return courseRepository.save(oldCourse);
        } else {
            return null;
        }
    }

    /**
     * Deletes course by ID
     * @param id id
     */
    public void deleteCourseByID(Integer id){
        courseRepository.deleteById(id);
    }

    /**
     * Deletes all courses
     */
    public void deleteAllCourses(){
        courseRepository.deleteAll();
    }
}
