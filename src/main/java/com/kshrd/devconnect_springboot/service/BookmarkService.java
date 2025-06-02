package com.kshrd.devconnect_springboot.service;

import com.kshrd.devconnect_springboot.model.entity.*;
import com.kshrd.devconnect_springboot.model.enums.BookmarkEnum;

import java.util.List;
import java.util.UUID;

public interface BookmarkService {
    Bookmark createBookmark(UUID targetId);
    void deleteBookmark(UUID targetId);
    List<?> bookmarkByType(BookmarkEnum targetType, Integer page, Integer size);
    Integer bookmarkPagination(BookmarkEnum targetType);

}
