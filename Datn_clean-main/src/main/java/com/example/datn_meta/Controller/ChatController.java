package com.example.datn_meta.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ChatController.java
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    record ChatReq(String message, String sessionId) {}
    record ChatResp(String answer, boolean handoff) {}

    @PostMapping
    public ChatResp chat(@RequestBody ChatReq req) {
        String q = (req.message() == null ? "" : req.message()).toLowerCase();

        // TODO: gọi LLM/RAG ở đây.
        // Mẫu rule-based tối giản:
        if (q.contains("đổi") && q.contains("trả")) {
            return new ChatResp(
                    "Chính sách đổi trả: trong 7 ngày, còn tag & chưa sử dụng. Mang hóa đơn hoặc mã đơn hàng giúp mình nhé.",
                    false
            );
        }
        if (q.contains("ship") || q.contains("giao hàng") || q.contains("vận chuyển")) {
            return new ChatResp(
                    "Giao hàng 24–48h nội thành; 3–5 ngày ngoại tỉnh. Free ship đơn từ 1.000.000đ.",
                    false
            );
        }

        // fallback → đề nghị chuyển người thật
        return new ChatResp(
                "Mình chưa chắc câu này. Bạn muốn chuyển sang nhân viên CSKH chứ?",
                true
        );
    }
}
