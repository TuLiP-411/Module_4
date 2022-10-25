package com.codegym.controller;

import com.codegym.model.Song;
import com.codegym.model.SongSubmitForm;
import com.codegym.service.SongService;
import com.codegym.service.impl.HibernateSongServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/music")
public class SongController {
    @Autowired
    private HibernateSongServiceImpl songService;
    @Value("${file-upload}")
    private String fileUpload;

    @GetMapping("")
    public String index(Model model) {
        List<Song> songs = songService.findAll();
        model.addAttribute("songs", songs);
        return "/index";
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/create");
        modelAndView.addObject("songSubmitForm", new SongSubmitForm());
        return modelAndView;
    }

    @PostMapping("/save")
    public ModelAndView saveSong(@ModelAttribute SongSubmitForm songSubmitForm) {
        ModelAndView modelAndView = new ModelAndView("/create");
        MultipartFile multipartFile = songSubmitForm.getSong();
        String fileName = multipartFile.getOriginalFilename();
        String[] prefix = new String[]{".mp3", ".wav", ".org", ".mp4"};
        boolean isMusicFile = false;
        for (String str : prefix) {
            if (fileName.substring(fileName.indexOf(".")).equals(str)) {
                isMusicFile = true;
            }
        }
        if (!isMusicFile) {
            modelAndView.addObject("message", "File is not music file!");
        } else {
            try {
                FileCopyUtils.copy(songSubmitForm.getSong().getBytes(), new File(fileUpload + fileName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Song song = new Song(songSubmitForm.getId(), songSubmitForm.getName(), songSubmitForm.getArtist(),
                    songSubmitForm.getGenre(), fileName);
            songService.save(song);

            modelAndView.addObject("songSubmitForm", songSubmitForm);
            modelAndView.addObject("message", "Uploaded new music successfully");
        }
        return modelAndView;
    }
}


