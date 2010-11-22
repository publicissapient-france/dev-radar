package com.xebia.devradar.web.controller;

import java.util.List;
import java.util.Set;

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
import com.xebia.devradar.pollers.PollerDescriptor;
import com.xebia.devradar.pollers.PollerServiceLocator;
import com.xebia.devradar.web.WorkspaceRepository;

@Controller
@RequestMapping("/workspaces")
@SessionAttributes({"workspace","eventSource"})
@Transactional
public class WorkspaceController {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView showWorkspaces() {
        final List<Workspace> workspaces = this.workspaceRepository.getAll();
        final ModelAndView mav = new ModelAndView("workspaces");
        mav.addObject("workspaces", workspaces);
        return mav;
    }

    @RequestMapping(value = "/{workspaceName}/index", method = RequestMethod.GET)
    public ModelAndView showWorkspace(@PathVariable final String workspaceName) {
        final Workspace workspace = this.workspaceRepository.findWorkspaceByName(workspaceName);
        final ModelAndView mav = new ModelAndView("workspace");
        mav.addObject("workspace", workspace);
        return mav;
    }

    @RequestMapping(value = "/{workspaceName}/delete", method = RequestMethod.GET)
    public String deleteWorkspace(@PathVariable final String workspaceName) {
        final Workspace workspace = this.workspaceRepository.findWorkspaceByName(workspaceName);
        this.workspaceRepository.deleteWorkspace(workspace);
        return "redirect:../index.html";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView createWorkspace() {
        return new ModelAndView("createWorkspace", "workspace", new Workspace());
    }

    @RequestMapping(value = "/onCreate", method = RequestMethod.POST)
    public String createWorkspace(@ModelAttribute("workspace") final Workspace workspace, final BindingResult result) {
        this.workspaceRepository.createWorkspace(workspace);
        return "redirect:" + workspace.getName() + "/index.html";
    }

    @RequestMapping(value = "/{workspaceName}/eventSources/create", method = RequestMethod.GET)
    public ModelAndView createEventSource(@PathVariable final String workspaceName)  {
        final ModelAndView mav = new ModelAndView("createEventSource");
        final Set<PollerDescriptor> pollerDescriptors = PollerServiceLocator.getSupportedPollers();
        mav.addObject("eventSource", new EventSource());
        mav.addObject("workspaceName", workspaceName);
        mav.addObject("pollerDescriptors", pollerDescriptors);
        return mav;
    }

    @RequestMapping(value = "/{workspaceName}/eventSources/onCreate", method = RequestMethod.POST)
    public String createWorkspace(@PathVariable final String workspaceName, @ModelAttribute("eventSource") final EventSource eventSource, final BindingResult result) {
        final Workspace workspace = this.workspaceRepository.findWorkspaceByName(workspaceName);
        workspace.addEventSource(eventSource);
        return "redirect:../index.html";
    }


}