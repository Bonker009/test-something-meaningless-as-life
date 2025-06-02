package com.kshrd.devconnect_springboot.service.implementation;

import com.kshrd.devconnect_springboot.exception.BadRequestException;
import com.kshrd.devconnect_springboot.exception.NotFoundException;
import com.kshrd.devconnect_springboot.model.entity.*;
import com.kshrd.devconnect_springboot.model.enums.BookmarkEnum;
import com.kshrd.devconnect_springboot.respository.*;
import com.kshrd.devconnect_springboot.service.BookmarkService;
import com.kshrd.devconnect_springboot.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImplement implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public Bookmark createBookmark(UUID targetId) {
        if (bookmarkRepository.getBookmarkById(targetId, CurrentUser.appUserId) != null) {
            throw new BadRequestException("Already exist");
        }
        return bookmarkRepository.createBookmark(targetId, CurrentUser.appUserId);
    }

    @Override
    public void deleteBookmark(UUID targetId) {
        if (bookmarkRepository.getBookmarkById(targetId, CurrentUser.appUserId) == null) {
            throw new NotFoundException("Bookmark not exist");
        }
        bookmarkRepository.deleteBookmark(targetId, CurrentUser.appUserId);
    }

    @Override
    public List<?> bookmarkByType(BookmarkEnum targetType, Integer page, Integer size) {
        page = (page - 1) * size;
        switch (targetType) {
            case PROJECTS -> {
                List<Project> projects = bookmarkRepository.getAllBookmarkProject(CurrentUser.appUserId, page, size);
                if (projects.isEmpty()) {
                    throw new NotFoundException("No project bookmark");
                }
                return projects;
            }
            case JOBS -> {
                List<Jobs> jobs = bookmarkRepository.getAllBookmarkJob(CurrentUser.appUserId, page, size);
                if (jobs.isEmpty()) {
                    throw new NotFoundException("No job bookmark");
                }
                return jobs;
            }
            case HACKATHONS -> {
                List<Hackathon> hackathons = bookmarkRepository.getAllBookmarkHackathon(CurrentUser.appUserId, page, size);
                if (hackathons.isEmpty()) {
                    throw new NotFoundException("No hackathon bookmark");
                }
                return hackathons;
            }
            case RECRUITERS -> {
                if (appUserRepository.getUserById(CurrentUser.appUserId).getIsRecruiter()) {
                    throw new BadRequestException("Recruiter cannot bookmark recruiter");
                }
                List<Recruiter> recruiters = bookmarkRepository.getAllBookmarkRecruiter(CurrentUser.appUserId, page, size);
                if (recruiters.isEmpty()) {
                    throw new NotFoundException("Recruiter not found");
                }
                return recruiters;
            }
            case DEVELOPERS -> {
                List<Developer> developers = bookmarkRepository.getAllBookmarkDeveloper(CurrentUser.appUserId, page, size);
                if (developers.isEmpty()) {
                    throw new NotFoundException("No developer found");
                }
                return developers;
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }

    @Override
    public Integer bookmarkPagination(BookmarkEnum targetType) {
        switch (targetType) {
            case PROJECTS -> {
                return bookmarkRepository.getCountAllBookmarkProject(CurrentUser.appUserId);
            }
            case JOBS -> {
                return bookmarkRepository.getCountAllBookmarkJob(CurrentUser.appUserId);
            }
            case HACKATHONS -> {
                return bookmarkRepository.getCountAllBookmarkHackathon(CurrentUser.appUserId);
            }
            case RECRUITERS -> {
                if (appUserRepository.getUserById(CurrentUser.appUserId).getIsRecruiter()) {
                    throw new BadRequestException("Recruiter cannot bookmark recruiter");
                }
                return bookmarkRepository.getCountAllBookmarkRecruiter(CurrentUser.appUserId);
            }
            case DEVELOPERS -> {
                return bookmarkRepository.getCountAllBookmarkDeveloper(CurrentUser.appUserId);
            }
            default -> {
                return 0;
            }
        }
    }


}
