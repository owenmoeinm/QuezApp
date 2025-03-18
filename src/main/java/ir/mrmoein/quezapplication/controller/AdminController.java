package ir.mrmoein.quezapplication.controller;

import ir.mrmoein.quezapplication.model.dto.*;
import ir.mrmoein.quezapplication.service.CourseService;
import ir.mrmoein.quezapplication.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {

    private final CourseService courseService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(CourseService service, UserService userService) {
        this.courseService = service;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView start() {
        return new ModelAndView("admin");
    }


    //admin state user and change

    @PostMapping("/convert")
    public ResponseEntity<Void> convertState(@ModelAttribute StatusDTO dto) {
        userService.changeState(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/state")
    public ModelAndView stateList(){
        List<StatusDTO> allStatus = userService.getAllStatus();
        return new ModelAndView("status_list").addObject("states" , allStatus);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StatusDTO>> liveSearch(@RequestParam("query") String query) {
        List<StatusDTO> statusDTOS = userService.liveSearchWithFullName(query);
        return ResponseEntity.ok(statusDTOS);
    }

    @GetMapping("/liveSearch")
    public ModelAndView liveSearchPage() {
        return new ModelAndView("live_search");
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getProfile(@RequestParam String nationalCode) {
        ProfileDTO profile = userService.getProfileUser(nationalCode);
        return ResponseEntity.ok(profile);
    }
    //search

}
