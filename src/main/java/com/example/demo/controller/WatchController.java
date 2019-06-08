/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.controller;

import com.example.demo.domain.UserComment;
import com.example.demo.domain.Watch;
import com.example.demo.service.UserCommentService;
import com.example.demo.service.WatchService;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author STEFAN94
 */
@Controller
@RequestMapping("/adminportal/watch")
public class WatchController {

    @Autowired
    private WatchService watchService;

    @Autowired
    private UserCommentService userCommentService;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addwatch(Model model) {
        Watch watch = new Watch();
        model.addAttribute("watch", watch);
        return "addWatch";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addWatchPost(
            @ModelAttribute("watch") Watch watch,
            HttpServletRequest request,
            @RequestParam("photos") ArrayList<MultipartFile> phts,
            HttpSession session,
            Model model) {
        Watch savedWatch = new Watch();
        try {
            savedWatch = watchService.save(watch);
        } catch (Exception e) {
            model.addAttribute("WatchSaveException", true);
            return "errorPageAdmin";
        }
        ArrayList<String> fileNames = null;
        if (watch.getAlbums().length > 0) {
            fileNames = new ArrayList<>();
            int br = 0;
            for (MultipartFile file : watch.getAlbums()) {
                char ch = 'a';
                switch (br) {
                    case 0:
                        ch = 'a';
                        break;
                    case 1:
                        ch = 'b';
                        break;
                    case 2:
                        ch = 'c';
                        break;
                    case 3:
                        ch = 'd';
                        break;
                    case 4:
                        ch = 'e';
                        break;
                    default:
                        break;
                }
                try {
                    byte[] bytes = file.getBytes();
                    String name = watch.getId() + "_" + ch + ".png";
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/static/image/watch/" + name)));
                    stream.write(bytes);
                    br++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        watch.setWatchComments(new HashSet<UserComment>());

        model.addAttribute("SaveSuccess", true);
        return "redirect:newWatchInfo?id=" + savedWatch.getId();
    }

    @RequestMapping("/watchInfo")
    public String adminsWatchInfo(@RequestParam("id") Long id, Model model) {

        Watch watch = new Watch();
        try {
            Optional<Watch> w = watchService.findOne(id);
            watch = w.get();
        } catch (Exception e) {
            model.addAttribute("WatchDetailsException", true);
            return "errorPageAdmin";
        }

        model.addAttribute("watch", watch);

        File f1 = new File("src/main/resources/static/image/watch/" + id + "_a.png");
        File f2 = new File("src/main/resources/static/image/watch/" + id + "_b.png");
        File f3 = new File("src/main/resources/static/image/watch/" + id + "_c.png");
        File f4 = new File("src/main/resources/static/image/watch/" + id + "_d.png");
        File f5 = new File("src/main/resources/static/image/watch/" + id + "_e.png");

        List<String> album = new ArrayList<>();
        try {
            if (Files.readAllBytes(f1.toPath()) != null) {
                album.add("/image/watch/" + id + "_a.png");
            }
        } catch (Exception e) {
            System.out.println("No such file exception");
        }
        try {
            if (Files.readAllBytes(f2.toPath()) != null) {
                album.add("/image/watch/" + id + "_b.png");
            }
        } catch (Exception e) {
            System.out.println("No such file exception");
        }
        try {
            if (Files.readAllBytes(f3.toPath()) != null) {
                album.add("/image/watch/" + id + "_c.png");
            }
        } catch (Exception e) {
            System.out.println("No such file exception");
        }
        try {
            if (Files.readAllBytes(f4.toPath()) != null) {
                album.add("/image/watch/" + id + "_d.png");
            }
        } catch (Exception e) {
            System.out.println("No such file exception");
        }
        try {
            if (Files.readAllBytes(f5.toPath()) != null) {
                album.add("/image/watch/" + id + "_e.png");
            }
        } catch (Exception e) {
            System.out.println("No such file exception");
        }
        model.addAttribute("album", album);

        Set<UserComment> userCommentList = userCommentService.findByWatch(watch);

        model.addAttribute("userCommentList", userCommentList);

        return "watchInfo";
    }

    @RequestMapping("/newWatchInfo")
    public String newWatchInfo(@RequestParam("id") Long id, Model model) {

        Watch watch = new Watch();
        try {
            Optional<Watch> w = watchService.findOne(id);
            watch = w.get();
        } catch (Exception e) {
            model.addAttribute("WatchDetailsException", true);
            return "errorPageAdmin";
        }

        model.addAttribute("watch", watch);

        File f1 = new File("src/main/resources/static/image/watch/" + id + "_a.png");
        File f2 = new File("src/main/resources/static/image/watch/" + id + "_b.png");
        File f3 = new File("src/main/resources/static/image/watch/" + id + "_c.png");
        File f4 = new File("src/main/resources/static/image/watch/" + id + "_d.png");
        File f5 = new File("src/main/resources/static/image/watch/" + id + "_e.png");

        List<String> album = new ArrayList<>();
        try {
            if (Files.readAllBytes(f1.toPath()) != null) {
                album.add("/image/watch/" + id + "_a.png");
            }
        } catch (Exception e) {
            System.out.println("No such file exception");
        }
        try {
            if (Files.readAllBytes(f2.toPath()) != null) {
                album.add("/image/watch/" + id + "_b.png");
            }
        } catch (Exception e) {
            System.out.println("No such file exception");
        }
        try {
            if (Files.readAllBytes(f3.toPath()) != null) {
                album.add("/image/watch/" + id + "_c.png");
            }
        } catch (Exception e) {
            System.out.println("No such file exception");
        }
        try {
            if (Files.readAllBytes(f4.toPath()) != null) {
                album.add("/image/watch/" + id + "_d.png");
            }
        } catch (Exception e) {
            System.out.println("No such file exception");
        }
        try {
            if (Files.readAllBytes(f5.toPath()) != null) {
                album.add("/image/watch/" + id + "_e.png");
            }
        } catch (Exception e) {
            System.out.println("No such file exception");
        }
        model.addAttribute("album", album);

        Set<UserComment> userCommentList = userCommentService.findByWatch(watch);

        System.out.println("User comments: " + userCommentList);
        model.addAttribute("userCommentList", userCommentList);
        model.addAttribute("SaveSuccess", true);

        return "watchInfo";
    }

    @RequestMapping("/removeComment")
    public String removeComment(
            @ModelAttribute("id") long id,
            Model model) {

        UserComment userComment = null;
        try {
            Optional<UserComment> userCommentOpt = userCommentService.findOne(id);
            userComment = userCommentOpt.get();
        } catch (Exception e) {
        }

        try {
            userCommentService.removeById(id);
        } catch (Exception e) {
            model.addAttribute("removeCommentException", true);
            return "errorPageAdmin";
        }

        model.addAttribute("commentRemoveSuccess", true);
        return "forward:watchInfo?id=" + userComment.getWatch().getId();
    }

    @RequestMapping("/updateWatch")
    public String updateWatch(@RequestParam("id") Long id, Model model) {
        Optional<Watch> w = watchService.findOne(id);
        Watch watch = w.get();
        model.addAttribute("watch", watch);

        File f1 = new File("src/main/resources/static/image/watch/" + id + "_a.png");
        File f2 = new File("src/main/resources/static/image/watch/" + id + "_b.png");
        File f3 = new File("src/main/resources/static/image/watch/" + id + "_c.png");
        File f4 = new File("src/main/resources/static/image/watch/" + id + "_d.png");
        File f5 = new File("src/main/resources/static/image/watch/" + id + "_e.png");

        List<String> album = new ArrayList<>();
        try {
            if (Files.readAllBytes(f1.toPath()) != null) {
                album.add("/image/watch/" + id + "_a.png");
            }
        } catch (Exception e) {
            System.out.println("No such file exception");
        }
        try {
            if (Files.readAllBytes(f2.toPath()) != null) {
                album.add("/image/watch/" + id + "_b.png");
            }
        } catch (Exception e) {
            System.out.println("No such file exception");
        }
        try {
            if (Files.readAllBytes(f3.toPath()) != null) {
                album.add("/image/watch/" + id + "_c.png");
            }
        } catch (Exception e) {
            System.out.println("No such file exception");
        }
        try {
            if (Files.readAllBytes(f4.toPath()) != null) {
                album.add("/image/watch/" + id + "_d.png");
            }
        } catch (Exception e) {
            System.out.println("No such file exception");
        }
        try {
            if (Files.readAllBytes(f5.toPath()) != null) {
                album.add("/image/watch/" + id + "_e.png");
            }
        } catch (Exception e) {
            System.out.println("No such file exception");
        }
        model.addAttribute("album", album);

        return "updateWatch";
    }

    @PostMapping("/updateWatch")
    public String updateWatchPost(@ModelAttribute("watch") Watch watch,
            HttpServletRequest request,
            @ModelAttribute("imgSrc") String imgSrc,
            @RequestParam("photos") ArrayList<MultipartFile> phts,
            HttpSession session,
            Model model) {
        try {
            watchService.save(watch);
        } catch (Exception e) {
            model.addAttribute("WatchUpdateException", true);
            return "errorPageAdmin";
        }

        if (watch.getAlbums().length > 0 && watch.getAlbums()[0].getSize() > 0 && !watch.getAlbums()[0].getOriginalFilename().equals("")) {
            try {
                if (Files.readAllBytes(new File("src/main/resources/static/image/watch/" + watch.getId() + "_a.png").toPath()).length != 0) {
                    Files.delete(Paths.get("src/main/resources/static/image/watch/" + watch.getId() + "_a.png"));
                }
            } catch (IOException ex) {
                Logger
                        .getLogger(WatchController.class
                                .getName()).log(Level.SEVERE, null, ex);
            }

            try {
                if (Files.readAllBytes(new File("src/main/resources/static/image/watch/" + watch.getId() + "_b.png").toPath()).length != 0) {
                    Files.delete(Paths.get("src/main/resources/static/image/watch/" + watch.getId() + "_b.png"));
                }
            } catch (IOException ex) {
                Logger
                        .getLogger(WatchController.class
                                .getName()).log(Level.SEVERE, null, ex);
            }

            try {
                if (Files.readAllBytes(new File("src/main/resources/static/image/watch/" + watch.getId() + "_c.png").toPath()).length != 0) {
                    Files.delete(Paths.get("src/main/resources/static/image/watch/" + watch.getId() + "_c.png"));
                }
            } catch (IOException ex) {
                Logger
                        .getLogger(WatchController.class
                                .getName()).log(Level.SEVERE, null, ex);
            }

            try {
                if (Files.readAllBytes(new File("src/main/resources/static/image/watch/" + watch.getId() + "_d.png").toPath()).length != 0) {
                    Files.delete(Paths.get("src/main/resources/static/image/watch/" + watch.getId() + "_d.png"));
                }
            } catch (IOException ex) {
                Logger
                        .getLogger(WatchController.class
                                .getName()).log(Level.SEVERE, null, ex);
            }

            try {
                if (Files.readAllBytes(new File("src/main/resources/static/image/watch/" + watch.getId() + "_e.png").toPath()).length != 0) {
                    Files.delete(Paths.get("src/main/resources/static/image/watch/" + watch.getId() + "_e.png"));
                }
            } catch (IOException ex) {
                Logger
                        .getLogger(WatchController.class
                                .getName()).log(Level.SEVERE, null, ex);
            }
            ArrayList<String> fileNames = null;
            if (watch.getAlbums().length > 0) {
                fileNames = new ArrayList<String>();
                int br = 0;
                for (MultipartFile file : watch.getAlbums()) {
                    char ch = 'a';
                    switch (br) {
                        case 0:
                            ch = 'a';
                            break;
                        case 1:
                            ch = 'b';
                            break;
                        case 2:
                            ch = 'c';
                            break;
                        case 3:
                            ch = 'd';
                            break;
                        case 4:
                            ch = 'e';
                            break;
                        default:
                            break;
                    }
                    try {
                        byte[] bytes = file.getBytes();
                        String name = watch.getId() + "_" + ch + ".png";
                        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/static/image/watch/" + name)));
                        stream.write(bytes);
                        br++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        model.addAttribute("UpdateSuccess", true);
        return "forward:watchInfo?id=" + watch.getId();
    }

    @RequestMapping("/adminsWatchList")
    public String watchList(Model model) {
        try {
            List<Watch> watchList = watchService.findAll();
            model.addAttribute("watchList", watchList);
        } catch (Exception e) {
            model.addAttribute("watchList", new ArrayList<>());
            return "adminsWatchList";
        }

        return "adminsWatchList";
    }

    @PostMapping("/remove")
    public String removeWatch(
            @ModelAttribute("id") String id,
            Model model) {
        long watchId = Long.parseLong(id.substring(9));
        try {

            watchService.removeOne(watchId);
        } catch (Exception e) {
            model.addAttribute("WatchDeleteException", true);
            return "errorPageAdmin";
        }
        try {
            if (Files.readAllBytes(new File("src/main/resources/static/image/watch/" + watchId + "_a.png").toPath()).length != 0) {
                Files.delete(Paths.get("src/main/resources/static/image/watch/" + watchId + "_a.png"));
            }
        } catch (IOException ex) {
            Logger
                    .getLogger(WatchController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (Files.readAllBytes(new File("src/main/resources/static/image/watch/" + watchId + "_b.png").toPath()).length != 0) {
                Files.delete(Paths.get("src/main/resources/static/image/watch/" + watchId + "_b.png"));
            }
        } catch (IOException ex) {

            Logger
                    .getLogger(WatchController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (Files.readAllBytes(new File("src/main/resources/static/image/watch/" + watchId + "_c.png").toPath()).length != 0) {
                Files.delete(Paths.get("src/main/resources/static/image/watch/" + watchId + "_c.png"));
            }
        } catch (IOException ex) {

            Logger
                    .getLogger(WatchController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (Files.readAllBytes(new File("src/main/resources/static/image/watch/" + watchId + "_d.png").toPath()).length != 0) {
                Files.delete(Paths.get("src/main/resources/static/image/watch/" + watchId + "_d.png"));
            }
        } catch (IOException ex) {

            Logger
                    .getLogger(WatchController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (Files.readAllBytes(new File("src/main/resources/static/image/watch/" + watchId + "_e.png").toPath()).length != 0) {
                Files.delete(Paths.get("src/main/resources/static/image/watch/" + watchId + "_e.png"));
            }
        } catch (IOException ex) {

            Logger
                    .getLogger(WatchController.class
                            .getName()).log(Level.SEVERE, null, ex);
        }

        List<Watch> watchList = watchService.findAll();
        model.addAttribute("watchList", watchList);
        model.addAttribute("DeleteSuccess", true);
        return "adminsWatchList";

    }
}
