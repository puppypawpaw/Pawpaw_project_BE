package com.puppy.pawpaw_project_be.webchat.service;

import com.puppy.pawpaw_project_be.webchat.entity.ChatRoom;
import com.puppy.pawpaw_project_be.webchat.vo.ChatRoomVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChatService {

    public List<ChatRoomVO> findAll(){
        List<Optional<ChatRoomVO>> chatrooms = new ArrayList<>();
        return chatrooms.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
