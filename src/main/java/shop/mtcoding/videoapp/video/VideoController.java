package shop.mtcoding.videoapp.video;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/videos")
public class VideoController {

    @Autowired
    private VideoRepository videoRepository;

    @GetMapping
    public String listVideos(Model model) {
        model.addAttribute("videos", videoRepository.findAll());
        return "videos";
    }

    @GetMapping("/{id}")
    public String getVideo(@PathVariable Long id, Model model) {
        Optional<Video> video = videoRepository.findById(id);
        if (video.isPresent()) {
            model.addAttribute("video", video.get());
            return "video";
        } else {
            return "404";
        }
    }

    @PostMapping
    public String uploadVideo(@RequestParam("file") MultipartFile file,
                              @RequestParam("title") String title,
                              @RequestParam("description") String description) {
        // 파일을 저장하고 URL을 생성
        String url = saveFileAndGetUrl(file);

        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setUrl(url);

        videoRepository.save(video);

        return "redirect:/videos";
    }

    private String saveFileAndGetUrl(MultipartFile file) {
        // 파일 저장 로직 (예: AWS S3에 업로드)
        return "https://example.com/videos/" + file.getOriginalFilename();
    }
}
