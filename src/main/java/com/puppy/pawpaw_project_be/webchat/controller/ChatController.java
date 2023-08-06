package com.puppy.pawpaw_project_be.webchat.controller;

import com.puppy.pawpaw_project_be.webchat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private ChatService chatService;
}
