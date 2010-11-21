package com.xebia.devradar.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.xebia.devradar.domain.EventSource;
import com.xebia.devradar.domain.Workspace;

@Controller
@RequestMapping("/workspaces")
@SessionAttributes
@Transactional
public class WorkspacesResource {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView showWorkspaces() {
        List<Workspace> workspaces = workspaceRepository.getAll();
        ModelAndView mav = new ModelAndView("workspaces");
        mav.addObject("workspaces", workspaces);
        return mav;
    }

    @RequestMapping(value = "/{workspaceName}/index", method = RequestMethod.GET)
    public ModelAndView showWorkspace(@PathVariable String workspaceName) {
        Workspace workspace = workspaceRepository.findWorkspaceByName(workspaceName);
        ModelAndView mav = new ModelAndView("workspace");
        mav.addObject("workspace", workspace);
        return mav;
    }
    
    @RequestMapping(value = "/{workspaceName}/delete", method = RequestMethod.GET)
    public String deleteWorkspace(@PathVariable String workspaceName) {
        Workspace workspace = workspaceRepository.findWorkspaceByName(workspaceName);
        workspaceRepository.deleteWorkspace(workspace);
        return "redirect:../index.html";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView createWorkspace() {
        return new ModelAndView("createWorkspace", "workspace", new Workspace());
    }

    @RequestMapping(value = "/onCreate", method = RequestMethod.POST)
    public String createWorkspace(@ModelAttribute("workspace") Workspace workspace, BindingResult result) {
        workspaceRepository.createWorkspace(workspace);
        return "redirect:" + workspace.getName() + "/index.html";
    }

    @RequestMapping(value = "/{workspaceName}/eventSources/create", method = RequestMethod.GET)
    public ModelAndView createEventSource(@PathVariable String workspaceName) {
        ModelAndView mav = new ModelAndView("createEventSource");
        mav.addObject("workspaceName", workspaceName);
        mav.addObject("eventSource", new EventSource());
        return mav;
    }

    @RequestMapping(value = "/{workspaceName}/eventSources/onCreate", method = RequestMethod.POST)
    public String createWorkspace(@PathVariable String workspaceName, @ModelAttribute("eventSource") EventSource eventSource, BindingResult result) {
        Workspace workspace = workspaceRepository.findWorkspaceByName(workspaceName);
        workspace.addEventSource(eventSource);
        return "redirect:../index.html";
    }


}