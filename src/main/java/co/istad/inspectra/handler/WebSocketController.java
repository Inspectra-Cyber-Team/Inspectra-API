//package co.istad.inspectra.handler;
//
//import co.istad.inspectra.features.blog.dto.BlogResponseDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//
//public class WebSocketController {
//
//    private final SimpMessagingTemplate messagingTemplate;
//
//    public void sendBlogCreatedMessage(BlogResponseDto blogResponseDto) {
//        // Sends the message to all subscribers on /topic/blogs
//        messagingTemplate.convertAndSend("/topic/blogs", blogResponseDto);
//    }
//}
